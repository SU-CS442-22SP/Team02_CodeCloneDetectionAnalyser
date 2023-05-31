    private void copyTemplates(ProjectPath pPath) {
        String sourceAntPath = pPath.sourceAntPath();
        final String moduleName = projectOperations.getFocusedTopLevelPackage().toString();
        logger.info("Module Name: " + moduleName);
        String targetDirectory = pPath.canonicalFileSystemPath(projectOperations);
        logger.info("Moving into target Directory: " + targetDirectory);
        if (!targetDirectory.endsWith("/")) {
            targetDirectory += "/";
        }
        if (!fileManager.exists(targetDirectory)) {
            fileManager.createDirectory(targetDirectory);
        }
        System.out.println("Target Directory: " + pPath.sourceAntPath());
        String path = TemplateUtils.getTemplatePath(getClass(), sourceAntPath);
        Set<URL> urls = UrlFindingUtils.findMatchingClasspathResources(context.getBundleContext(), path);
        Assert.notNull(urls, "Could not search bundles for resources for Ant Path '" + path + "'");
        if (urls.isEmpty()) {
            logger.info("URLS are empty stopping...");
        }
        for (URL url : urls) {
            logger.info("Stepping into " + url.toExternalForm());
            String fileName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
            fileName = fileName.replace("-template", "");
            String targetFilename = targetDirectory + fileName;
            logger.info("Handling " + targetFilename);
            if (!fileManager.exists(targetFilename)) {
                try {
                    logger.info("Copied file");
                    String input = FileCopyUtils.copyToString(new InputStreamReader(url.openStream()));
                    logger.info("TopLevelPackage: " + projectOperations.getFocusedTopLevelPackage());
                    logger.info("SegmentPackage: " + pPath.canonicalFileSystemPath(projectOperations));
                    String topLevelPackage = projectOperations.getFocusedTopLevelPackage().toString();
                    input = input.replace("_underscore_underscoreTOP_underscoreLEVEL_underscorePACKAGE_underscore_underscore", topLevelPackage);
                    input = input.replace("_underscore_underscoreSEGMENT_underscorePACKAGE_underscore_underscore", pPath.segmentPackage());
                    input = input.replace("_underscore_underscorePROJECT_underscoreNAME_underscore_underscore", projectOperations.getFocusedProjectName());
                    input = input.replace("_underscore_underscoreENTITY_underscoreNAME_underscore_underscore", entityName);
                    MutableFile mutableFile = fileManager.createFile(targetFilename);
                    FileCopyUtils.copy(input.getBytes(), mutableFile.getOutputStream());
                } catch (IOException ioe) {
                    throw new IllegalStateException("Unable to create '" + targetFilename + "'", ioe);
                }
            }
        }
    }

