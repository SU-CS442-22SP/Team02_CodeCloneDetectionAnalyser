        public static IProject createEMFProject(IPath javaSource, URI projectLocationURI, List<IProject> referencedProjects, Monitor monitor, int style, List<?> pluginVariables) {
            IProgressMonitor progressMonitor = BasicMonitor.toIProgressMonitor(monitor);
            String projectName = javaSource.segment(0);
            IProject project = null;
            try {
                List<IClasspathEntry> classpathEntries = new UniqueEList<IClasspathEntry>();
                progressMonitor.beginTask("", 10);
                progressMonitor.subTask(CodeGenEcorePlugin.INSTANCE.getString("_underscoreUI_underscoreCreatingEMFProject_underscoremessage", new Object[] { projectName, projectLocationURI != null ? projectLocationURI.toString() : projectName }));
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                project = workspace.getRoot().getProject(projectName);
                if (!project.exists()) {
                    URI location = projectLocationURI;
                    if (location == null) {
                        location = URI.createFileURI(workspace.getRoot().getLocation().append(projectName).toOSString());
                    }
                    location = location.appendSegment(".project");
                    File projectFile = new File(location.toString());
                    if (projectFile.exists()) {
                        projectFile.renameTo(new File(location.toString() + ".old"));
                    }
                }
                IJavaProject javaProject = JavaCore.create(project);
                IProjectDescription projectDescription = null;
                if (!project.exists()) {
                    projectDescription = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);
                    if (projectLocationURI != null) {
                        projectDescription.setLocationURI(new java.net.URI(projectLocationURI.toString()));
                    }
                    project.create(projectDescription, new SubProgressMonitor(progressMonitor, 1));
                    project.open(new SubProgressMonitor(progressMonitor, 1));
                } else {
                    projectDescription = project.getDescription();
                    project.open(new SubProgressMonitor(progressMonitor, 1));
                    if (project.hasNature(JavaCore.NATURE_underscoreID)) {
                        classpathEntries.addAll(Arrays.asList(javaProject.getRawClasspath()));
                    }
                }
                boolean isInitiallyEmpty = classpathEntries.isEmpty();
                {
                    if (referencedProjects.size() != 0 && (style & (EMF_underscorePLUGIN_underscorePROJECT_underscoreSTYLE | EMF_underscoreEMPTY_underscorePROJECT_underscoreSTYLE)) == 0) {
                        projectDescription.setReferencedProjects(referencedProjects.toArray(new IProject[referencedProjects.size()]));
                        for (IProject referencedProject : referencedProjects) {
                            IClasspathEntry referencedProjectClasspathEntry = JavaCore.newProjectEntry(referencedProject.getFullPath());
                            classpathEntries.add(referencedProjectClasspathEntry);
                        }
                    }
                    String[] natureIds = projectDescription.getNatureIds();
                    if (natureIds == null) {
                        natureIds = new String[] { JavaCore.NATURE_underscoreID, "org.eclipse.pde.PluginNature" };
                    } else {
                        if (!project.hasNature(JavaCore.NATURE_underscoreID)) {
                            String[] oldNatureIds = natureIds;
                            natureIds = new String[oldNatureIds.length + 1];
                            System.arraycopy(oldNatureIds, 0, natureIds, 0, oldNatureIds.length);
                            natureIds[oldNatureIds.length] = JavaCore.NATURE_underscoreID;
                        }
                        if (!project.hasNature("org.eclipse.pde.PluginNature")) {
                            String[] oldNatureIds = natureIds;
                            natureIds = new String[oldNatureIds.length + 1];
                            System.arraycopy(oldNatureIds, 0, natureIds, 0, oldNatureIds.length);
                            natureIds[oldNatureIds.length] = "org.eclipse.pde.PluginNature";
                        }
                    }
                    projectDescription.setNatureIds(natureIds);
                    ICommand[] builders = projectDescription.getBuildSpec();
                    if (builders == null) {
                        builders = new ICommand[0];
                    }
                    boolean hasManifestBuilder = false;
                    boolean hasSchemaBuilder = false;
                    for (int i = 0; i < builders.length; ++i) {
                        if ("org.eclipse.pde.ManifestBuilder".equals(builders[i].getBuilderName())) {
                            hasManifestBuilder = true;
                        }
                        if ("org.eclipse.pde.SchemaBuilder".equals(builders[i].getBuilderName())) {
                            hasSchemaBuilder = true;
                        }
                    }
                    if (!hasManifestBuilder) {
                        ICommand[] oldBuilders = builders;
                        builders = new ICommand[oldBuilders.length + 1];
                        System.arraycopy(oldBuilders, 0, builders, 0, oldBuilders.length);
                        builders[oldBuilders.length] = projectDescription.newCommand();
                        builders[oldBuilders.length].setBuilderName("org.eclipse.pde.ManifestBuilder");
                    }
                    if (!hasSchemaBuilder) {
                        ICommand[] oldBuilders = builders;
                        builders = new ICommand[oldBuilders.length + 1];
                        System.arraycopy(oldBuilders, 0, builders, 0, oldBuilders.length);
                        builders[oldBuilders.length] = projectDescription.newCommand();
                        builders[oldBuilders.length].setBuilderName("org.eclipse.pde.SchemaBuilder");
                    }
                    projectDescription.setBuildSpec(builders);
                    project.setDescription(projectDescription, new SubProgressMonitor(progressMonitor, 1));
                    IContainer sourceContainer = project;
                    if (javaSource.segmentCount() > 1) {
                        IPath sourceContainerPath = javaSource.removeFirstSegments(1).makeAbsolute();
                        sourceContainer = project.getFolder(sourceContainerPath);
                        if (!sourceContainer.exists()) {
                            for (int i = sourceContainerPath.segmentCount() - 1; i >= 0; i--) {
                                sourceContainer = project.getFolder(sourceContainerPath.removeLastSegments(i));
                                if (!sourceContainer.exists()) {
                                    ((IFolder) sourceContainer).create(false, true, new SubProgressMonitor(progressMonitor, 1));
                                }
                            }
                        }
                        IClasspathEntry sourceClasspathEntry = JavaCore.newSourceEntry(javaSource);
                        for (Iterator<IClasspathEntry> i = classpathEntries.iterator(); i.hasNext(); ) {
                            IClasspathEntry classpathEntry = i.next();
                            if (classpathEntry.getPath().isPrefixOf(javaSource)) {
                                i.remove();
                            }
                        }
                        classpathEntries.add(0, sourceClasspathEntry);
                    }
                    if (isInitiallyEmpty) {
                        IClasspathEntry jreClasspathEntry = JavaCore.newVariableEntry(new Path(JavaRuntime.JRELIB_underscoreVARIABLE), new Path(JavaRuntime.JRESRC_underscoreVARIABLE), new Path(JavaRuntime.JRESRCROOT_underscoreVARIABLE));
                        for (Iterator<IClasspathEntry> i = classpathEntries.iterator(); i.hasNext(); ) {
                            IClasspathEntry classpathEntry = i.next();
                            if (classpathEntry.getPath().isPrefixOf(jreClasspathEntry.getPath())) {
                                i.remove();
                            }
                        }
                        String jreContainer = JavaRuntime.JRE_underscoreCONTAINER;
                        String complianceLevel = CodeGenUtil.EclipseUtil.getJavaComplianceLevel(project);
                        if ("1.5".equals(complianceLevel)) {
                            jreContainer += "/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5";
                        } else if ("1.6".equals(complianceLevel)) {
                            jreContainer += "/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.6";
                        }
                        classpathEntries.add(JavaCore.newContainerEntry(new Path(jreContainer)));
                    }
                    if ((style & EMF_underscoreEMPTY_underscorePROJECT_underscoreSTYLE) == 0) {
                        if ((style & EMF_underscorePLUGIN_underscorePROJECT_underscoreSTYLE) != 0) {
                            classpathEntries.add(JavaCore.newContainerEntry(new Path("org.eclipse.pde.core.requiredPlugins")));
                            for (Iterator<IClasspathEntry> i = classpathEntries.iterator(); i.hasNext(); ) {
                                IClasspathEntry classpathEntry = i.next();
                                if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_underscoreVARIABLE && !JavaRuntime.JRELIB_underscoreVARIABLE.equals(classpathEntry.getPath().toString()) || classpathEntry.getEntryKind() == IClasspathEntry.CPE_underscorePROJECT) {
                                    i.remove();
                                }
                            }
                        } else {
                            CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_underscoreCORE_underscoreRUNTIME", "org.eclipse.core.runtime");
                            CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_underscoreCORE_underscoreRESOURCES", "org.eclipse.core.resources");
                            CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_underscoreCOMMON", "org.eclipse.emf.common");
                            CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_underscoreECORE", "org.eclipse.emf.ecore");
                            if ((style & EMF_underscoreXML_underscorePROJECT_underscoreSTYLE) != 0) {
                                CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_underscoreECORE_underscoreXMI", "org.eclipse.emf.ecore.xmi");
                            }
                            if ((style & EMF_underscoreMODEL_underscorePROJECT_underscoreSTYLE) == 0) {
                                CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_underscoreEDIT", "org.eclipse.emf.edit");
                                if ((style & EMF_underscoreEDIT_underscorePROJECT_underscoreSTYLE) == 0) {
                                    CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_underscoreSWT", "org.eclipse.swt");
                                    CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_underscoreJFACE", "org.eclipse.jface");
                                    CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_underscoreUI_underscoreVIEWS", "org.eclipse.ui.views");
                                    CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_underscoreUI_underscoreEDITORS", "org.eclipse.ui.editors");
                                    CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_underscoreUI_underscoreIDE", "org.eclipse.ui.ide");
                                    CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "ECLIPSE_underscoreUI_underscoreWORKBENCH", "org.eclipse.ui.workbench");
                                    CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_underscoreCOMMON_underscoreUI", "org.eclipse.emf.common.ui");
                                    CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_underscoreEDIT_underscoreUI", "org.eclipse.emf.edit.ui");
                                    if ((style & EMF_underscoreXML_underscorePROJECT_underscoreSTYLE) == 0) {
                                        CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "EMF_underscoreECORE_underscoreXMI", "org.eclipse.emf.ecore.xmi");
                                    }
                                }
                            }
                            if ((style & EMF_underscoreTESTS_underscorePROJECT_underscoreSTYLE) != 0) {
                                CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, "JUNIT", "org.junit");
                            }
                            if (pluginVariables != null) {
                                for (Iterator<?> i = pluginVariables.iterator(); i.hasNext(); ) {
                                    Object variable = i.next();
                                    if (variable instanceof IClasspathEntry) {
                                        classpathEntries.add((IClasspathEntry) variable);
                                    } else if (variable instanceof String) {
                                        String pluginVariable = (String) variable;
                                        String name;
                                        String id;
                                        int index = pluginVariable.indexOf("=");
                                        if (index == -1) {
                                            name = pluginVariable.replace('.', '_underscore').toUpperCase();
                                            id = pluginVariable;
                                        } else {
                                            name = pluginVariable.substring(0, index);
                                            id = pluginVariable.substring(index + 1);
                                        }
                                        CodeGenUtil.EclipseUtil.addClasspathEntries(classpathEntries, name, id);
                                    }
                                }
                            }
                        }
                    }
                    javaProject.setRawClasspath(classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]), new SubProgressMonitor(progressMonitor, 1));
                }
                if (isInitiallyEmpty) {
                    javaProject.setOutputLocation(new Path("/" + javaSource.segment(0) + "/bin"), new SubProgressMonitor(progressMonitor, 1));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                CodeGenEcorePlugin.INSTANCE.log(exception);
            } finally {
                progressMonitor.done();
            }
            return project;
        }

