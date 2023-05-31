    public void doFTP() throws BuildException {
        FTPClient ftp = null;
        try {
            task.log("Opening FTP connection to " + task.getServer(), Project.MSG_underscoreVERBOSE);
            ftp = new FTPClient();
            if (task.isConfigurationSet()) {
                ftp = FTPConfigurator.configure(ftp, task);
            }
            ftp.setRemoteVerificationEnabled(task.getEnableRemoteVerification());
            ftp.connect(task.getServer(), task.getPort());
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                throw new BuildException("FTP connection failed: " + ftp.getReplyString());
            }
            task.log("connected", Project.MSG_underscoreVERBOSE);
            task.log("logging in to FTP server", Project.MSG_underscoreVERBOSE);
            if ((task.getAccount() != null && !ftp.login(task.getUserid(), task.getPassword(), task.getAccount())) || (task.getAccount() == null && !ftp.login(task.getUserid(), task.getPassword()))) {
                throw new BuildException("Could not login to FTP server");
            }
            task.log("login succeeded", Project.MSG_underscoreVERBOSE);
            if (task.isBinary()) {
                ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_underscoreFILE_underscoreTYPE);
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                    throw new BuildException("could not set transfer type: " + ftp.getReplyString());
                }
            } else {
                ftp.setFileType(org.apache.commons.net.ftp.FTP.ASCII_underscoreFILE_underscoreTYPE);
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                    throw new BuildException("could not set transfer type: " + ftp.getReplyString());
                }
            }
            if (task.isPassive()) {
                task.log("entering passive mode", Project.MSG_underscoreVERBOSE);
                ftp.enterLocalPassiveMode();
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                    throw new BuildException("could not enter into passive " + "mode: " + ftp.getReplyString());
                }
            }
            if (task.getInitialSiteCommand() != null) {
                RetryHandler h = new RetryHandler(task.getRetriesAllowed(), task);
                final FTPClient lftp = ftp;
                executeRetryable(h, new Retryable() {

                    public void execute() throws IOException {
                        doSiteCommand(lftp, task.getInitialSiteCommand());
                    }
                }, "initial site command: " + task.getInitialSiteCommand());
            }
            if (task.getUmask() != null) {
                RetryHandler h = new RetryHandler(task.getRetriesAllowed(), task);
                final FTPClient lftp = ftp;
                executeRetryable(h, new Retryable() {

                    public void execute() throws IOException {
                        doSiteCommand(lftp, "umask " + task.getUmask());
                    }
                }, "umask " + task.getUmask());
            }
            if (task.getAction() == FTPTask.MK_underscoreDIR) {
                RetryHandler h = new RetryHandler(task.getRetriesAllowed(), task);
                final FTPClient lftp = ftp;
                executeRetryable(h, new Retryable() {

                    public void execute() throws IOException {
                        makeRemoteDir(lftp, task.getRemotedir());
                    }
                }, task.getRemotedir());
            } else if (task.getAction() == FTPTask.SITE_underscoreCMD) {
                RetryHandler h = new RetryHandler(task.getRetriesAllowed(), task);
                final FTPClient lftp = ftp;
                executeRetryable(h, new Retryable() {

                    public void execute() throws IOException {
                        doSiteCommand(lftp, task.getSiteCommand());
                    }
                }, "Site Command: " + task.getSiteCommand());
            } else {
                if (task.getRemotedir() != null) {
                    task.log("changing the remote directory", Project.MSG_underscoreVERBOSE);
                    ftp.changeWorkingDirectory(task.getRemotedir());
                    if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                        throw new BuildException("could not change remote " + "directory: " + ftp.getReplyString());
                    }
                }
                if (task.isNewer() && task.isTimeDiffAuto()) {
                    task.setTimeDiffMillis(getTimeDiff(ftp));
                }
                task.log(FTPTask.ACTION_underscoreSTRS[task.getAction()] + " " + FTPTask.ACTION_underscoreTARGET_underscoreSTRS[task.getAction()]);
                transferFiles(ftp);
            }
        } catch (IOException ex) {
            throw new BuildException("error during FTP transfer: " + ex, ex);
        } finally {
            if (ftp != null && ftp.isConnected()) {
                try {
                    task.log("disconnecting", Project.MSG_underscoreVERBOSE);
                    ftp.logout();
                    ftp.disconnect();
                } catch (IOException ex) {
                }
            }
        }
    }

