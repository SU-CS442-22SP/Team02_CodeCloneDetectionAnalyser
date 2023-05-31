    public static File insertFileInto(File zipFile, File toInsert, String targetPath) {
        Zip64File zip64File = null;
        try {
            boolean compress = false;
            zip64File = new Zip64File(zipFile);
            FileEntry testEntry = getFileEntry(zip64File, targetPath);
            if (testEntry != null && testEntry.getMethod() == FileEntry.iMETHOD_underscoreDEFLATED) {
                compress = true;
            }
            processAndCreateFolderEntries(zip64File, parseTargetPath(targetPath, toInsert), compress);
            if (testEntry != null) {
                log.info("[insertFileInto] Entry exists: " + testEntry.getName());
                log.info("[insertFileInto] Will delete this entry before inserting: " + toInsert.getName());
                if (!testEntry.isDirectory()) {
                    zip64File.delete(testEntry.getName());
                } else {
                    log.info("[insertFileInto] Entry is a directory. " + "Will delete all files contained in this entry and insert " + toInsert.getName() + "and all nested files.");
                    if (!targetPath.contains("/")) {
                        targetPath = targetPath + "/";
                    }
                    deleteFileEntry(zip64File, testEntry);
                    log.info("[insertFileInto] Entry successfully deleted.");
                }
                log.info("[insertFileInto] Writing new Entry: " + targetPath);
                EntryOutputStream out = null;
                if (!compress) {
                    out = zip64File.openEntryOutputStream(targetPath, FileEntry.iMETHOD_underscoreSTORED, new Date(toInsert.lastModified()));
                } else {
                    out = zip64File.openEntryOutputStream(targetPath, FileEntry.iMETHOD_underscoreDEFLATED, new Date(toInsert.lastModified()));
                }
                if (toInsert.isDirectory()) {
                    out.flush();
                    out.close();
                    log.info("[insertFileInto] Finished writing entry: " + targetPath);
                    List<String> containedPaths = normalizePaths(toInsert);
                    List<File> containedFiles = listAllFilesAndFolders(toInsert, new ArrayList<File>());
                    log.info("[insertFileInto] Added entry is a folder.");
                    log.info("[insertFileInto] Adding all nested files: ");
                    for (int i = 0; i < containedPaths.size(); i++) {
                        File currentFile = containedFiles.get(i);
                        String currentPath = targetPath.replace("/", "") + File.separator + containedPaths.get(i);
                        EntryOutputStream loop_underscoreout = null;
                        if (!compress) {
                            loop_underscoreout = zip64File.openEntryOutputStream(currentPath, FileEntry.iMETHOD_underscoreSTORED, new Date(currentFile.lastModified()));
                        } else {
                            loop_underscoreout = zip64File.openEntryOutputStream(currentPath, FileEntry.iMETHOD_underscoreDEFLATED, new Date(currentFile.lastModified()));
                        }
                        if (currentFile.isFile()) {
                            InputStream loop_underscorein = new FileInputStream(currentFile);
                            IOUtils.copyLarge(loop_underscorein, loop_underscoreout);
                            loop_underscorein.close();
                        }
                        log.info("[insertFileInto] Added: " + currentPath);
                        loop_underscoreout.flush();
                        loop_underscoreout.close();
                    }
                } else {
                    InputStream in = new FileInputStream(toInsert);
                    IOUtils.copyLarge(in, out);
                    in.close();
                    out.flush();
                    out.close();
                }
            } else {
                EntryOutputStream out = null;
                if (!compress) {
                    out = zip64File.openEntryOutputStream(targetPath, FileEntry.iMETHOD_underscoreSTORED, new Date(toInsert.lastModified()));
                } else {
                    out = zip64File.openEntryOutputStream(targetPath, FileEntry.iMETHOD_underscoreDEFLATED, new Date(toInsert.lastModified()));
                }
                if (toInsert.isDirectory()) {
                    out.flush();
                    out.close();
                    log.info("[insertFileInto] Finished writing entry: " + targetPath);
                    List<String> containedPaths = normalizePaths(toInsert);
                    List<File> containedFiles = listAllFilesAndFolders(toInsert, new ArrayList<File>());
                    log.info("[insertFileInto] Added entry is a folder.");
                    log.info("[insertFileInto] Adding all nested files: ");
                    for (int i = 0; i < containedPaths.size(); i++) {
                        File currentFile = containedFiles.get(i);
                        String currentPath = targetPath.replace("/", "") + File.separator + containedPaths.get(i);
                        EntryOutputStream loop_underscoreout = null;
                        if (!compress) {
                            loop_underscoreout = zip64File.openEntryOutputStream(currentPath, FileEntry.iMETHOD_underscoreSTORED, new Date(currentFile.lastModified()));
                        } else {
                            loop_underscoreout = zip64File.openEntryOutputStream(currentPath, FileEntry.iMETHOD_underscoreDEFLATED, new Date(currentFile.lastModified()));
                        }
                        if (currentFile.isFile()) {
                            InputStream loop_underscorein = new FileInputStream(currentFile);
                            IOUtils.copyLarge(loop_underscorein, loop_underscoreout);
                            loop_underscorein.close();
                        }
                        log.info("[insertFileInto] Added: " + currentPath);
                        loop_underscoreout.flush();
                        loop_underscoreout.close();
                    }
                } else {
                    InputStream in = new FileInputStream(toInsert);
                    IOUtils.copyLarge(in, out);
                    in.close();
                    out.flush();
                    out.close();
                }
            }
            log.info("[insertFileInto] Done! Added " + toInsert.getName() + " to zip.");
            zip64File.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(zip64File.getDiskFile().getFileName());
    }

