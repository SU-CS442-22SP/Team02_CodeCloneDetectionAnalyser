    public void initialize(IProgressMonitor monitor) throws JETException {
        IProgressMonitor progressMonitor = monitor;
        progressMonitor.beginTask("", 10);
        progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreGeneratingJETEmitterFor_underscoremessage", new Object[] { getTemplateURI() }));
        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
        try {
            final JETCompiler jetCompiler = getTemplateURIPath() == null ? new MyBaseJETCompiler(getTemplateURI(), getEncoding(), getClassLoader()) : new MyBaseJETCompiler(getTemplateURIPath(), getTemplateURI(), getEncoding(), getClassLoader());
            progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETParsing_underscoremessage", new Object[] { jetCompiler.getResolvedTemplateURI() }));
            jetCompiler.parse();
            progressMonitor.worked(1);
            String packageName = jetCompiler.getSkeleton().getPackageName();
            if (getTemplateURIPath() != null) {
                URI templateURI = URI.createURI(getTemplateURIPath()[0]);
                URLClassLoader theClassLoader = null;
                if (templateURI.isPlatformResource()) {
                    IProject project = workspace.getRoot().getProject(templateURI.segment(1));
                    if (JETNature.getRuntime(project) != null) {
                        List<URL> urls = new ArrayList<URL>();
                        IJavaProject javaProject = JavaCore.create(project);
                        urls.add(new File(project.getLocation() + "/" + javaProject.getOutputLocation().removeFirstSegments(1) + "/").toURI().toURL());
                        for (IClasspathEntry classpathEntry : javaProject.getResolvedClasspath(true)) {
                            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_underscorePROJECT) {
                                IPath projectPath = classpathEntry.getPath();
                                IProject otherProject = workspace.getRoot().getProject(projectPath.segment(0));
                                IJavaProject otherJavaProject = JavaCore.create(otherProject);
                                urls.add(new File(otherProject.getLocation() + "/" + otherJavaProject.getOutputLocation().removeFirstSegments(1) + "/").toURI().toURL());
                            }
                        }
                        theClassLoader = AccessController.doPrivileged(new GetURLClassLoaderSuperAction(urls));
                    }
                } else if (templateURI.isPlatformPlugin()) {
                    final Bundle bundle = Platform.getBundle(templateURI.segment(1));
                    if (bundle != null) {
                        theClassLoader = AccessController.doPrivileged(new GetURLClassLoaderBundleAction(bundle));
                    }
                }
                if (theClassLoader != null) {
                    String className = (packageName.length() == 0 ? "" : packageName + ".") + jetCompiler.getSkeleton().getClassName();
                    if (className.endsWith("_underscore")) {
                        className = className.substring(0, className.length() - 1);
                    }
                    try {
                        Class<?> theClass = theClassLoader.loadClass(className);
                        Class<?> theOtherClass = null;
                        try {
                            theOtherClass = getClassLoader().loadClass(className);
                        } catch (ClassNotFoundException exception) {
                        }
                        if (theClass != theOtherClass) {
                            String methodName = jetCompiler.getSkeleton().getMethodName();
                            Method[] methods = theClass.getDeclaredMethods();
                            for (int i = 0; i < methods.length; ++i) {
                                if (methods[i].getName().equals(methodName)) {
                                    jetEmitter.setMethod(methods[i]);
                                    break;
                                }
                            }
                            return;
                        }
                    } catch (ClassNotFoundException exception) {
                    }
                }
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            jetCompiler.generate(outputStream);
            final InputStream contents = new ByteArrayInputStream(outputStream.toByteArray());
            if (!javaModel.isOpen()) {
                javaModel.open(new SubProgressMonitor(progressMonitor, 1));
            } else {
                progressMonitor.worked(1);
            }
            final IProject project = workspace.getRoot().getProject(jetEmitter.getProjectName());
            progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETPreparingProject_underscoremessage", new Object[] { project.getName() }));
            IJavaProject javaProject;
            if (!project.exists()) {
                progressMonitor.subTask("JET creating project " + project.getName());
                project.create(new SubProgressMonitor(progressMonitor, 1));
                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETCreatingProject_underscoremessage", new Object[] { project.getName() }));
                IProjectDescription description = workspace.newProjectDescription(project.getName());
                description.setNatureIds(new String[] { JavaCore.NATURE_underscoreID });
                description.setLocation(null);
                project.open(new SubProgressMonitor(progressMonitor, 1));
                project.setDescription(description, new SubProgressMonitor(progressMonitor, 1));
            } else {
                project.open(new SubProgressMonitor(progressMonitor, 5));
                IProjectDescription description = project.getDescription();
                description.setNatureIds(new String[] { JavaCore.NATURE_underscoreID });
                project.setDescription(description, new SubProgressMonitor(progressMonitor, 1));
            }
            javaProject = JavaCore.create(project);
            List<IClasspathEntry> classpath = new UniqueEList<IClasspathEntry>(Arrays.asList(javaProject.getRawClasspath()));
            for (int i = 0, len = classpath.size(); i < len; i++) {
                IClasspathEntry entry = classpath.get(i);
                if (entry.getEntryKind() == IClasspathEntry.CPE_underscoreSOURCE && ("/" + project.getName()).equals(entry.getPath().toString())) {
                    classpath.remove(i);
                }
            }
            progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETInitializingProject_underscoremessage", new Object[] { project.getName() }));
            IClasspathEntry classpathEntry = JavaCore.newSourceEntry(new Path("/" + project.getName() + "/src"));
            IClasspathEntry jreClasspathEntry = JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_underscoreCONTAINER"));
            classpath.add(classpathEntry);
            classpath.add(jreClasspathEntry);
            classpath.addAll(getClassPathEntries());
            IFolder sourceFolder = project.getFolder(new Path("src"));
            if (!sourceFolder.exists()) {
                sourceFolder.create(false, true, new SubProgressMonitor(progressMonitor, 1));
            }
            IFolder runtimeFolder = project.getFolder(new Path("bin"));
            if (!runtimeFolder.exists()) {
                runtimeFolder.create(false, true, new SubProgressMonitor(progressMonitor, 1));
            }
            javaProject.setRawClasspath(classpath.toArray(new IClasspathEntry[classpath.size()]), new SubProgressMonitor(progressMonitor, 1));
            javaProject.setOutputLocation(new Path("/" + project.getName() + "/bin"), new SubProgressMonitor(progressMonitor, 1));
            javaProject.close();
            progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETOpeningJavaProject_underscoremessage", new Object[] { project.getName() }));
            javaProject.open(new SubProgressMonitor(progressMonitor, 1));
            IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
            IPackageFragmentRoot sourcePackageFragmentRoot = null;
            for (int j = 0; j < packageFragmentRoots.length; ++j) {
                IPackageFragmentRoot packageFragmentRoot = packageFragmentRoots[j];
                if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_underscoreSOURCE) {
                    sourcePackageFragmentRoot = packageFragmentRoot;
                    break;
                }
            }
            StringTokenizer stringTokenizer = new StringTokenizer(packageName, ".");
            IProgressMonitor subProgressMonitor = new SubProgressMonitor(progressMonitor, 1);
            subProgressMonitor.beginTask("", stringTokenizer.countTokens() + 4);
            subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreCreateTargetFile_underscoremessage"));
            IContainer sourceContainer = sourcePackageFragmentRoot == null ? project : (IContainer) sourcePackageFragmentRoot.getCorrespondingResource();
            while (stringTokenizer.hasMoreElements()) {
                String folderName = stringTokenizer.nextToken();
                sourceContainer = sourceContainer.getFolder(new Path(folderName));
                if (!sourceContainer.exists()) {
                    ((IFolder) sourceContainer).create(false, true, new SubProgressMonitor(subProgressMonitor, 1));
                }
            }
            IFile targetFile = sourceContainer.getFile(new Path(jetCompiler.getSkeleton().getClassName() + ".java"));
            if (!targetFile.exists()) {
                subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETCreating_underscoremessage", new Object[] { targetFile.getFullPath() }));
                targetFile.create(contents, true, new SubProgressMonitor(subProgressMonitor, 1));
            } else {
                subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETUpdating_underscoremessage", new Object[] { targetFile.getFullPath() }));
                targetFile.setContents(contents, true, true, new SubProgressMonitor(subProgressMonitor, 1));
            }
            subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETBuilding_underscoremessage", new Object[] { project.getName() }));
            project.build(IncrementalProjectBuilder.INCREMENTAL_underscoreBUILD, new SubProgressMonitor(subProgressMonitor, 1));
            boolean errors = hasErrors(subProgressMonitor, targetFile);
            if (!errors) {
                subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_underscoreUI_underscoreJETLoadingClass_underscoremessage", new Object[] { jetCompiler.getSkeleton().getClassName() + ".class" }));
                List<URL> urls = new ArrayList<URL>();
                urls.add(new File(project.getLocation() + "/" + javaProject.getOutputLocation().removeFirstSegments(1) + "/").toURI().toURL());
                final Set<Bundle> bundles = new HashSet<Bundle>();
                LOOP: for (IClasspathEntry jetEmitterClasspathEntry : jetEmitter.getClasspathEntries()) {
                    IClasspathAttribute[] classpathAttributes = jetEmitterClasspathEntry.getExtraAttributes();
                    if (classpathAttributes != null) {
                        for (IClasspathAttribute classpathAttribute : classpathAttributes) {
                            if (classpathAttribute.getName().equals(CodeGenUtil.EclipseUtil.PLUGIN_underscoreID_underscoreCLASSPATH_underscoreATTRIBUTE_underscoreNAME)) {
                                Bundle bundle = Platform.getBundle(classpathAttribute.getValue());
                                if (bundle != null) {
                                    bundles.add(bundle);
                                    continue LOOP;
                                }
                            }
                        }
                    }
                    urls.add(new URL("platform:/resource" + jetEmitterClasspathEntry.getPath() + "/"));
                }
                URLClassLoader theClassLoader = AccessController.doPrivileged(new GetURLClassLoaderSuperBundlesAction(bundles, urls));
                Class<?> theClass = theClassLoader.loadClass((packageName.length() == 0 ? "" : packageName + ".") + jetCompiler.getSkeleton().getClassName());
                String methodName = jetCompiler.getSkeleton().getMethodName();
                Method[] methods = theClass.getDeclaredMethods();
                for (int i = 0; i < methods.length; ++i) {
                    if (methods[i].getName().equals(methodName)) {
                        jetEmitter.setMethod(methods[i]);
                        break;
                    }
                }
            }
            subProgressMonitor.done();
        } catch (CoreException exception) {
            throw new JETException(exception);
        } catch (Exception exception) {
            throw new JETException(exception);
        } finally {
            progressMonitor.done();
        }
    }

