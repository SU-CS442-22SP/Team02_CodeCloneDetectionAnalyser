    private FTPClient getFtpClient(Entry parentEntry) throws Exception {
        Object[] values = parentEntry.getValues();
        if (values == null) {
            return null;
        }
        String server = (String) values[COL_underscoreSERVER];
        String baseDir = (String) values[COL_underscoreBASEDIR];
        String user = (String) values[COL_underscoreUSER];
        String password = (String) values[COL_underscorePASSWORD];
        if (password != null) {
            password = getRepository().getPageHandler().processTemplate(password, false);
        } else {
            password = "";
        }
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server);
            if (user != null) {
                ftpClient.login(user, password);
            }
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                return null;
            }
            ftpClient.setFileType(FTP.IMAGE_underscoreFILE_underscoreTYPE);
            ftpClient.enterLocalPassiveMode();
            return ftpClient;
        } catch (Exception exc) {
            System.err.println("Could not connect to ftp server:" + exc);
            return null;
        }
    }

