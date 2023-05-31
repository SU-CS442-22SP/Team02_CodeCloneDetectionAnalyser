    public static void createOutputStructure(String templatePath) throws InterruptedException {
        try {
            templatePath = new File(templatePath).getCanonicalPath();
            templatePath = templatePath.replace('\\', '/');
            File file = null;
            Paths paths = (Paths) GragGenerator.getObjectsFromTree(Paths.class).get(0);
            Config config = (Config) GragGenerator.getObjectsFromTree(Config.class).get(0);
            DirectoryIterator iterator = new DirectoryIterator(templatePath, true, true);
            while ((file = iterator.getNext()) != null) {
                boolean copyFile = false;
                String fullFilename = file.getCanonicalPath();
                int lastDirPos = fullFilename.lastIndexOf(System.getProperty("file.separator"));
                if (CVS_underscoreDIR.equals(file.getCanonicalPath().substring(fullFilename.length() - CVS_underscoreDIR.length(), fullFilename.length())) || CVS_underscoreDIR.equals(fullFilename.substring(lastDirPos - CVS_underscoreDIR.length(), lastDirPos))) {
                    continue;
                }
                if ("readme.txt".equals(file.getName())) {
                    continue;
                }
                String fileOut = outputDir.replace('\\', '/');
                String path = file.getCanonicalPath().replace('\\', '/');
                if (path.indexOf(templatePath) == 0) {
                    path = path.substring(templatePath.length());
                    if (path.startsWith(Paths.CONF_underscoreGENERAL_underscoreDIR)) {
                        path = paths.getConfigOutput() + path.substring(Paths.CONF_underscoreGENERAL_underscoreDIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.CONF_underscoreSTRUTS_underscoreDIR)) {
                        path = paths.getConfigOutput() + path.substring(Paths.CONF_underscoreSTRUTS_underscoreDIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.CONF_underscoreTAPESTRY4_underscoreDIR)) {
                        path = paths.getConfigOutput() + path.substring(Paths.CONF_underscoreTAPESTRY4_underscoreDIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.CONF_underscoreSWING_underscoreDIR)) {
                        path = paths.getConfigOutput() + path.substring(Paths.CONF_underscoreSWING_underscoreDIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.JAVA_underscoreWEB_underscoreSTRUTS_underscoreDIR)) {
                        path = paths.getJspOutput() + path.substring(Paths.JAVA_underscoreWEB_underscoreSTRUTS_underscoreDIR.length());
                        if (config.matchWebTier("struts").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreWEB_underscoreTAPESTRY4_underscoreDIR)) {
                        path = paths.getJspOutput() + path.substring(Paths.JAVA_underscoreWEB_underscoreTAPESTRY4_underscoreDIR.length());
                        if (config.matchWebTier("tapestry").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreSWING_underscoreDIR)) {
                        path = paths.getSwingOutput() + path.substring(Paths.JAVA_underscoreSWING_underscoreDIR.length());
                        if (config.matchWebTier("swing").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreSTRUTS_underscoreDIR)) {
                        path = paths.getWebOutput() + path.substring(Paths.JAVA_underscoreSTRUTS_underscoreDIR.length());
                        if (config.matchWebTier("struts").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreTAPESTRY4_underscoreDIR)) {
                        path = paths.getWebOutput() + path.substring(Paths.JAVA_underscoreTAPESTRY4_underscoreDIR.length());
                        if (config.matchWebTier("tapestry").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreEJB2_underscoreDIR)) {
                        path = paths.getEjbOutput() + path.substring(Paths.JAVA_underscoreEJB2_underscoreDIR.length());
                        if (config.matchBusinessTier("ejb 2").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreEJB3_underscoreDIR)) {
                        path = paths.getEjbOutput() + path.substring(Paths.JAVA_underscoreEJB3_underscoreDIR.length());
                        if (config.matchBusinessTier("ejb 3").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreHIBERNATE2_underscoreDIR)) {
                        path = paths.getHibernateOutput() + path.substring(Paths.JAVA_underscoreHIBERNATE2_underscoreDIR.length());
                        if (config.matchBusinessTier("hibernate 2").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreHIBERNATE3_underscoreDIR)) {
                        path = paths.getHibernateOutput() + path.substring(Paths.JAVA_underscoreHIBERNATE3_underscoreDIR.length());
                        if (config.matchBusinessTier("hibernate 3").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreMOCK_underscoreDIR)) {
                        path = paths.getMockOutput() + path.substring(Paths.JAVA_underscoreMOCK_underscoreDIR.length());
                        if (config.useMock().booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_underscoreSERVICE_underscoreDIR)) {
                        path = paths.getServiceOutput() + path.substring(Paths.JAVA_underscoreSERVICE_underscoreDIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.JAVA_underscoreTEST_underscoreDIR)) {
                        path = paths.getTestOutput() + path.substring(Paths.JAVA_underscoreTEST_underscoreDIR.length());
                        copyFile = true;
                    } else if ((path.indexOf("build.bat") != -1) || ((path.indexOf("deploy.bat") != -1))) {
                        copyFile = true;
                    }
                }
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                if (copyFile) {
                    fileOut += path;
                    path = outputDir + path;
                    if (!file.isDirectory()) {
                        String name = file.getName();
                        path = path.substring(0, (path.length() - name.length()));
                    }
                    new File(path).mkdirs();
                    if (!file.isDirectory()) {
                        byte array[] = new byte[1024];
                        int size = 0;
                        try {
                            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileOut));
                            while ((size = in.read(array)) != -1) out.write(array, 0, size);
                            in.close();
                            out.flush();
                            out.close();
                        } catch (Exception exc) {
                            log("[Error] Copy output file failed : " + fileOut);
                            log(exc.getMessage());
                        }
                    }
                }
            }
        } catch (Exception exc) {
            log.error("Error while copying files: ", exc);
            log(exc.getMessage());
        }
    }

