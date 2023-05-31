    private void Connect() throws NpsException {
        try {
            client = new FTPClient();
            client.connect(host.hostname, host.remoteport);
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                client = null;
                com.microfly.util.DefaultLog.error_underscorenoexception("FTP Server:" + host.hostname + "refused connection.");
                return;
            }
            client.login(host.uname, host.upasswd);
            client.enterLocalPassiveMode();
            client.setFileType(FTPClient.BINARY_underscoreFILE_underscoreTYPE);
            client.changeWorkingDirectory(host.remotedir);
        } catch (Exception e) {
            com.microfly.util.DefaultLog.error(e);
        }
    }

