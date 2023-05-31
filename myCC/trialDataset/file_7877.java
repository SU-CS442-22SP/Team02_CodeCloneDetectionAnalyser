    private void sendToFtp(String outputText) {
        String uid = this.properties.get(PROPERTY_underscoreOUTPUT_underscoreFTP_underscoreUSERNAME);
        String pwd = this.properties.get(PROPERTY_underscoreOUTPUT_underscoreFTP_underscorePASSWORD);
        String address = this.properties.get(PROPERTY_underscoreOUTPUT_underscoreFTP_underscoreADDRESS);
        int port = 21;
        try {
            port = Integer.valueOf(this.properties.get(PROPERTY_underscoreOUTPUT_underscoreFTP_underscorePORT));
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Could not read FTP port from properties. Using port 21");
        }
        String location = this.properties.get(PROPERTY_underscoreOUTPUT_underscoreFTP_underscoreLOCATION);
        String filename = this.properties.get(PROPERTY_underscoreOUTPUT_underscoreFTP_underscoreFILENAME);
        LOG.log(Level.INFO, "Uploading text output to {0}:{1}/{2}/{3}", new Object[] { address, port, location, filename });
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(address, port);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                LOG.log(Level.SEVERE, "Could not connect to FTP server: {0}", reply);
                return;
            }
            if (!ftpClient.login(uid, pwd)) {
                LOG.log(Level.SEVERE, "Could not login to FTP server ({0}) with given credentials.", address);
                return;
            }
            ftpClient.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
            ftpClient.enterLocalPassiveMode();
            if (ftpClient.storeFile(location + "/" + filename, new ByteArrayInputStream(outputText.getBytes()))) {
                LOG.log(Level.INFO, "Transfer complete");
            } else {
                LOG.log(Level.WARNING, "Transfer incomplete");
            }
        } catch (SocketException ex) {
            LOG.log(Level.SEVERE, "Could not transfer file.", ex.getMessage());
            LOG.log(Level.FINE, "", ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not transfer file.", ex.getMessage());
            LOG.log(Level.FINE, "", ex);
        }
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Could not disconnect from FTP.", ex.getMessage());
                LOG.log(Level.FINE, "", ex);
            }
        }
    }

