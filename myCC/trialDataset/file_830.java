        private boolean downloadFile() {
            FTPClient ftp = new FTPClient();
            try {
                int reply;
                ftp.connect(this.server);
                ResourcePool.LogMessage(this, ResourcePool.INFO_underscoreMESSAGE, "Connected to " + this.server);
                reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    ResourcePool.LogMessage(this, ResourcePool.ERROR_underscoreMESSAGE, "FTP server refused connection.");
                    return false;
                }
            } catch (IOException e) {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException f) {
                        return false;
                    }
                }
                ResourcePool.LogMessage(this, ResourcePool.ERROR_underscoreMESSAGE, "FTP Could not connect to server.");
                ResourcePool.LogException(e, this);
                return false;
            }
            try {
                if (!ftp.login(this.user, this.password)) {
                    ftp.logout();
                    ResourcePool.LogMessage(this, ResourcePool.ERROR_underscoreMESSAGE, "FTP login failed.");
                    return false;
                }
                ResourcePool.LogMessage(this, ResourcePool.INFO_underscoreMESSAGE, "Remote system is " + ftp.getSystemName());
                if ((this.transferType != null) && (this.transferType.compareTo(FTPWorkerThread.ASCII) == 0)) {
                    ftp.setFileType(FTP.ASCII_underscoreFILE_underscoreTYPE);
                } else {
                    ftp.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
                }
                if ((this.passiveMode != null) && this.passiveMode.equalsIgnoreCase(FTPWorkerThread.FALSE)) {
                    ftp.enterLocalActiveMode();
                } else {
                    ftp.enterLocalPassiveMode();
                }
            } catch (FTPConnectionClosedException e) {
                ResourcePool.LogMessage(this, ResourcePool.ERROR_underscoreMESSAGE, "Server closed connection.");
                ResourcePool.LogException(e, this);
                return false;
            } catch (IOException e) {
                ResourcePool.LogException(e, this);
                return false;
            }
            OutputStream output;
            try {
                java.util.Date startDate = new java.util.Date();
                output = new FileOutputStream(this.destFileName);
                ftp.retrieveFile(this.fileName, output);
                File f = new File(this.destFileName);
                if (f.exists() && (this.lastModifiedDate != null)) {
                    f.setLastModified(this.lastModifiedDate.longValue());
                }
                java.util.Date endDate = new java.util.Date();
                this.downloadTime = endDate.getTime() - startDate.getTime();
                double iDownLoadTime = ((this.downloadTime + 1) / 1000) + 1;
                ResourcePool.LogMessage(this, ResourcePool.INFO_underscoreMESSAGE, "Download Complete, Rate = " + (this.fileSize / (iDownLoadTime * 1024)) + " Kb/s, Seconds = " + iDownLoadTime);
                this.downloadTime = (this.downloadTime + 1) / 1000;
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            } catch (FTPConnectionClosedException e) {
                ResourcePool.LogMessage(this, ResourcePool.ERROR_underscoreMESSAGE, e.getMessage());
                ResourcePool.LogException(e, this);
                return false;
            } catch (IOException e) {
                ResourcePool.LogException(e, this);
                return false;
            }
            return true;
        }

