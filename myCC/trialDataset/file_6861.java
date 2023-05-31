    public void save() throws IOException {
        CodeTimer saveTimer;
        if (!dirty) {
            return;
        }
        saveTimer = new CodeTimer("PackedFile.save");
        saveTimer.setEnabled(log.isDebugEnabled());
        File newFile = new File(tmpDir.getAbsolutePath() + "/" + new GUID() + ".pak");
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(newFile)));
        zout.setLevel(1);
        try {
            saveTimer.start("contentFile");
            if (hasFile(CONTENT_underscoreFILE)) {
                zout.putNextEntry(new ZipEntry(CONTENT_underscoreFILE));
                InputStream is = getFileAsInputStream(CONTENT_underscoreFILE);
                IOUtils.copy(is, zout);
                zout.closeEntry();
            }
            saveTimer.stop("contentFile");
            saveTimer.start("propertyFile");
            if (getPropertyMap().isEmpty()) {
                removeFile(PROPERTY_underscoreFILE);
            } else {
                zout.putNextEntry(new ZipEntry(PROPERTY_underscoreFILE));
                xstream.toXML(getPropertyMap(), zout);
                zout.closeEntry();
            }
            saveTimer.stop("propertyFile");
            saveTimer.start("addFiles");
            addedFileSet.remove(CONTENT_underscoreFILE);
            for (String path : addedFileSet) {
                zout.putNextEntry(new ZipEntry(path));
                InputStream is = getFileAsInputStream(path);
                IOUtils.copy(is, zout);
                zout.closeEntry();
            }
            saveTimer.stop("addFiles");
            saveTimer.start("copyFiles");
            if (file.exists()) {
                Enumeration<? extends ZipEntry> entries = zFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (!entry.isDirectory() && !addedFileSet.contains(entry.getName()) && !removedFileSet.contains(entry.getName()) && !CONTENT_underscoreFILE.equals(entry.getName()) && !PROPERTY_underscoreFILE.equals(entry.getName())) {
                        zout.putNextEntry(entry);
                        InputStream is = getFileAsInputStream(entry.getName());
                        IOUtils.copy(is, zout);
                        zout.closeEntry();
                    } else if (entry.isDirectory()) {
                        zout.putNextEntry(entry);
                        zout.closeEntry();
                    }
                }
            }
            try {
                if (zFile != null) zFile.close();
            } catch (IOException e) {
            }
            zFile = null;
            saveTimer.stop("copyFiles");
            saveTimer.start("close");
            zout.close();
            zout = null;
            saveTimer.stop("close");
            saveTimer.start("backup");
            File backupFile = new File(tmpDir.getAbsolutePath() + "/" + new GUID() + ".mv");
            if (file.exists()) {
                backupFile.delete();
                if (!file.renameTo(backupFile)) {
                    FileUtil.copyFile(file, backupFile);
                    file.delete();
                }
            }
            saveTimer.stop("backup");
            saveTimer.start("finalize");
            if (!newFile.renameTo(file)) FileUtil.copyFile(newFile, file);
            if (backupFile.exists()) backupFile.delete();
            saveTimer.stop("finalize");
            dirty = false;
        } finally {
            saveTimer.start("cleanup");
            try {
                if (zFile != null) zFile.close();
            } catch (IOException e) {
            }
            if (newFile.exists()) newFile.delete();
            try {
                if (zout != null) zout.close();
            } catch (IOException e) {
            }
            saveTimer.stop("cleanup");
            if (log.isDebugEnabled()) log.debug(saveTimer);
            saveTimer = null;
        }
    }

