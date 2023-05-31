    public ForkJavaProject(String projectName, Class<?> activatorClass) {
        this.activatorClass = activatorClass;
        try {
            IWorkspaceRoot rootWorkspace = ResourcesPlugin.getWorkspace().getRoot();
            this.prj = rootWorkspace.getProject(projectName);
            if (this.prj.exists()) {
                this.prj.delete(true, true, new NullProgressMonitor());
            }
            this.prj.create(new NullProgressMonitor());
            this.prj.open(new NullProgressMonitor());
            IProjectDescription description = this.prj.getDescription();
            description.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });
            this.prj.setDescription(description, new NullProgressMonitor());
            createProjectDir(Constants.Dirs.DIR_underscoreMAIN_underscoreJAVA);
            createProjectDir(Constants.Dirs.DIR_underscoreCONFIG);
            createProjectDir(Constants.Dirs.DIR_underscoreMAIN_underscoreRESOURCES);
            createProjectDir(Constants.Dirs.DIR_underscoreMODELS);
            createProjectDir(Constants.Dirs.DIR_underscoreTESTS_underscoreJAVA);
            createProjectDir(Constants.Dirs.DIR_underscoreTESTS_underscoreRESOURCES);
            createProjectDir(Constants.Dirs.DIR_underscoreCLASSES);
            createProjectDir(Constants.Dirs.DIR_underscoreLIB);
            this.prj.refreshLocal(IResource.DEPTH_underscoreINFINITE, new NullProgressMonitor());
            this.javaProject = JavaCore.create(this.prj);
            if (this.javaProject.exists() && !this.javaProject.isOpen()) {
                this.javaProject.open(new NullProgressMonitor());
            }
            File javaHome = new File(System.getProperty("java.home"));
            IPath jreLibPath = new Path(javaHome.getPath()).append("lib").append("rt.jar");
            this.javaProject.setOutputLocation(prj.getFolder(Constants.Dirs.DIR_underscoreCLASSES).getFullPath(), new NullProgressMonitor());
            JavaCore.setClasspathVariable("JRE_underscoreLIB", jreLibPath, new NullProgressMonitor());
            this.javaProject.setRawClasspath(getProjectClassPath(), new NullProgressMonitor());
        } catch (CoreException e) {
            Activator.getDefault().logError("An exception has been thrown while creating Project", e);
        }
    }

