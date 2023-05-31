    public void upload() throws UnknownHostException, SocketException, FTPConnectionClosedException, LoginFailedException, DirectoryChangeFailedException, CopyStreamException, RefusedConnectionException, IOException {
        final int NUM_underscoreXML_underscoreFILES = 2;
        final String META_underscoreXML_underscoreSUFFIX = "_underscoremeta.xml";
        final String FILES_underscoreXML_underscoreSUFFIX = "_underscorefiles.xml";
        final String username = getUsername();
        final String password = getPassword();
        if (getFtpServer() == null) {
            throw new IllegalStateException("ftp server not set");
        }
        if (getFtpPath() == null) {
            throw new IllegalStateException("ftp path not set");
        }
        if (username == null) {
            throw new IllegalStateException("username not set");
        }
        if (password == null) {
            throw new IllegalStateException("password not set");
        }
        final String metaXmlString = serializeDocument(getMetaDocument());
        final String filesXmlString = serializeDocument(getFilesDocument());
        final byte[] metaXmlBytes = metaXmlString.getBytes();
        final byte[] filesXmlBytes = filesXmlString.getBytes();
        final int metaXmlLength = metaXmlBytes.length;
        final int filesXmlLength = filesXmlBytes.length;
        final Collection files = getFiles();
        final int totalFiles = NUM_underscoreXML_underscoreFILES + files.size();
        final String[] fileNames = new String[totalFiles];
        final long[] fileSizes = new long[totalFiles];
        final String metaXmlName = getIdentifier() + META_underscoreXML_underscoreSUFFIX;
        fileNames[0] = metaXmlName;
        fileSizes[0] = metaXmlLength;
        final String filesXmlName = getIdentifier() + FILES_underscoreXML_underscoreSUFFIX;
        fileNames[1] = filesXmlName;
        fileSizes[1] = filesXmlLength;
        int j = 2;
        for (Iterator i = files.iterator(); i.hasNext(); ) {
            final ArchiveFile f = (ArchiveFile) i.next();
            fileNames[j] = f.getRemoteFileName();
            fileSizes[j] = f.getFileSize();
            j++;
        }
        for (int i = 0; i < fileSizes.length; i++) {
            _underscorefileNames2Progress.put(fileNames[i], new UploadFileProgress(fileSizes[i]));
            _underscoretotalUploadSize += fileSizes[i];
        }
        FTPClient ftp = new FTPClient();
        try {
            if (isCancelled()) {
                return;
            }
            ftp.enterLocalPassiveMode();
            if (isCancelled()) {
                return;
            }
            ftp.connect(getFtpServer());
            final int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                throw new RefusedConnectionException(getFtpServer() + "refused FTP connection");
            }
            if (isCancelled()) {
                return;
            }
            if (!ftp.login(username, password)) {
                throw new LoginFailedException();
            }
            try {
                if (!ftp.changeWorkingDirectory(getFtpPath())) {
                    if (!isFtpDirPreMade() && !ftp.makeDirectory(getFtpPath())) {
                        throw new DirectoryChangeFailedException();
                    }
                    if (isCancelled()) {
                        return;
                    }
                    if (!ftp.changeWorkingDirectory(getFtpPath())) {
                        throw new DirectoryChangeFailedException();
                    }
                }
                if (isCancelled()) {
                    return;
                }
                connected();
                uploadFile(metaXmlName, new ByteArrayInputStream(metaXmlBytes), ftp);
                uploadFile(filesXmlName, new ByteArrayInputStream(filesXmlBytes), ftp);
                if (isCancelled()) {
                    return;
                }
                ftp.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
                for (final Iterator i = files.iterator(); i.hasNext(); ) {
                    final ArchiveFile f = (ArchiveFile) i.next();
                    uploadFile(f.getRemoteFileName(), new FileInputStream(f.getIOFile()), ftp);
                }
            } catch (InterruptedIOException ioe) {
                return;
            } finally {
                ftp.logout();
            }
        } finally {
            try {
                ftp.disconnect();
            } catch (IOException e) {
            }
        }
        if (isCancelled()) {
            return;
        }
        checkinStarted();
        if (isCancelled()) {
            return;
        }
        checkin();
        if (isCancelled()) {
            return;
        }
        checkinCompleted();
    }

