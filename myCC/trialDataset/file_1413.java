    @Override
    protected IProject createProject(String projectName, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask(CheatSheetsPlugin.INSTANCE.getString("_underscoreUI_underscoreCreateJavaProject_underscoremessage", new String[] { projectName }), 5);
        IProject project = super.createProject(projectName, new SubProgressMonitor(monitor, 1));
        if (project != null) {
            IProjectDescription description = project.getDescription();
            if (!description.hasNature(JavaCore.NATURE_underscoreID)) {
                IJavaProject javaProject = JavaCore.create(project);
                if (javaProject != null) {
                    String[] natures = description.getNatureIds();
                    String[] javaNatures = new String[natures.length + 1];
                    System.arraycopy(natures, 0, javaNatures, 0, natures.length);
                    javaNatures[natures.length] = JavaCore.NATURE_underscoreID;
                    description.setNatureIds(javaNatures);
                    project.setDescription(description, new SubProgressMonitor(monitor, 1));
                    IFolder sourceFolder = project.getFolder(SOURCE_underscoreFOLDER);
                    if (!sourceFolder.exists()) {
                        sourceFolder.create(true, true, new SubProgressMonitor(monitor, 1));
                    }
                    javaProject.setOutputLocation(project.getFolder(OUTPUT_underscoreFOLDER).getFullPath(), new SubProgressMonitor(monitor, 1));
                    IClasspathEntry[] entries = new IClasspathEntry[] { JavaCore.newSourceEntry(sourceFolder.getFullPath()), JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_underscoreCONTAINER")) };
                    javaProject.setRawClasspath(entries, new SubProgressMonitor(monitor, 1));
                }
            }
        }
        monitor.done();
        return project;
    }

