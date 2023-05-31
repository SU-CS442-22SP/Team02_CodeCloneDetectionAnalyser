    public boolean finish() {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName.getText());
        try {
            project.create(null);
            project.open(null);
            IProjectDescription desc = project.getDescription();
            desc.setNatureIds(new String[] { JavaCore.NATURE_underscoreID });
            project.setDescription(desc, null);
            IJavaProject javaProject = JavaCore.create(project);
            IPath fitLib = project.getFullPath().append(FIT_underscoreLIBRARY);
            javaProject.setRawClasspath(createClassPathEntries(project, fitLib), null);
            copyLibrary(project);
            javaProject.setOutputLocation(createOutputFolder(project, DEFAULT_underscoreOUTPUT_underscoreFOLDER).getFullPath(), null);
            createOutputFolder(project, fitTests.getText());
            createOutputFolder(project, fitResults.getText());
            if (!DEFAULT_underscoreOUTPUT_underscoreFOLDER.equals(fitResults.getText())) {
                DefaultFolderProperties.setDefinedOutputLocation(project, fitResults.getText());
            }
            if (!DEFAULT_underscoreSOURCE_underscoreFOLDER.equals(fitFixtures.getText())) {
                DefaultFolderProperties.setDefinedSourceLocation(project, fitFixtures.getText());
            }
            if (includeExamplesCheck.getSelection()) {
                copySamples(project);
            }
        } catch (CoreException e) {
            handleError(getContainer().getShell(), project, "Could not create project:" + e.getMessage());
            return false;
        } catch (IOException e) {
            handleError(getContainer().getShell(), project, "Could not create project:" + e.getMessage());
            return false;
        }
        return true;
    }

