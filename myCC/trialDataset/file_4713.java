    private static IProject createCopyProject(IProject project, String pName, IWorkspace ws, IProgressMonitor pm) throws Exception {
        pm.beginTask("Creating temp project", 1);
        IPath destination = new Path(pName);
        IJavaProject oldJavaproj = JavaCore.create(project);
        IClasspathEntry[] classPath = oldJavaproj.getRawClasspath();
        IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(pName);
        newProject.create(null);
        newProject.open(null);
        IProjectDescription desc = newProject.getDescription();
        desc.setNatureIds(new String[] { JavaCore.NATURE_underscoreID });
        newProject.setDescription(desc, null);
        List<IClasspathEntry> newClassPath = new ArrayList<IClasspathEntry>();
        for (IClasspathEntry cEntry : classPath) {
            switch(cEntry.getEntryKind()) {
                case IClasspathEntry.CPE_underscoreSOURCE:
                    System.out.println("Source folder " + cEntry.getPath());
                    newClassPath.add(copySourceFolder(project, newProject, cEntry, destination));
                    break;
                case IClasspathEntry.CPE_underscoreLIBRARY:
                    System.out.println("library folder " + cEntry.getPath());
                    newClassPath.add(cEntry);
                    break;
                case IClasspathEntry.CPE_underscorePROJECT:
                    System.out.println("project folder " + cEntry.getPath());
                    newClassPath.add(cEntry);
                    break;
                case IClasspathEntry.CPE_underscoreVARIABLE:
                    System.out.println("variable folder " + cEntry.getPath());
                    newClassPath.add(cEntry);
                    break;
                default:
                    System.out.println("container folder " + cEntry.getPath());
                    newClassPath.add(cEntry);
            }
        }
        copyDir(project.getLocation().toString(), "/translator", newProject.getLocation().toString(), "", new ArrayList<String>() {

            {
                add("generated");
                add("classes");
                add(".svn");
            }
        });
        newProject.refreshLocal(IResource.DEPTH_underscoreINFINITE, pm);
        newProject.build(IncrementalProjectBuilder.AUTO_underscoreBUILD, pm);
        newProject.touch(pm);
        IJavaProject javaproj = JavaCore.create(newProject);
        javaproj.setOutputLocation(new Path("/" + newProject.getName() + "/classes/bin"), null);
        javaproj.setRawClasspath(newClassPath.toArray(new IClasspathEntry[newClassPath.size()]), pm);
        Map opts = oldJavaproj.getOptions(true);
        javaproj.setOptions(opts);
        javaproj.makeConsistent(pm);
        javaproj.save(pm, true);
        return newProject;
    }

