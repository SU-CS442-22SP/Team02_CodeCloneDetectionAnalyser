    public static void main(String[] args) throws Exception {
        String ftpHostIP = System.getProperty(RuntimeConstants.FTP_underscoreHOST_underscoreIP.toString());
        String ftpUsername = System.getProperty(RuntimeConstants.FTP_underscoreUSERNAME.toString());
        String ftpPassword = System.getProperty(RuntimeConstants.FTP_underscorePASSWORD.toString());
        String ftpWorkingDirectory = System.getProperty(RuntimeConstants.FTP_underscoreWORKING_underscoreDIRECTORY_underscorePATH.toString());
        String ftpSenderDirectory = System.getProperty(RuntimeConstants.FTP_underscoreSENDER_underscoreDIRECTORY_underscoreFULL_underscorePATH.toString());
        if (ftpHostIP == null) {
            System.err.println("The FTP_underscoreHOST_underscoreIP system property must be filled out.");
            System.exit(1);
        }
        if (ftpUsername == null) {
            System.err.println("The FTP_underscoreUSERNAME system property must be filled out.");
            System.exit(1);
        }
        if (ftpPassword == null) {
            System.err.println("The FTP_underscorePASSWORD system property must be filled out.");
            System.exit(1);
        }
        if (ftpWorkingDirectory == null) {
            System.err.println("The FTP_underscoreWORKING_underscoreDIRECTORY_underscorePATH system property must be filled out.");
            System.exit(1);
        }
        if (ftpSenderDirectory == null) {
            System.err.println("The FTP_underscoreSENDER_underscoreDIRECTORY_underscoreFULL_underscorePATH system property must be filled out.");
            System.exit(1);
        }
        FTPClient ftp = new FTPClient();
        ftp.connect(ftpHostIP);
        ftp.login(ftpUsername, ftpPassword);
        ftp.changeWorkingDirectory(ftpWorkingDirectory);
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[1024]);
        ftp.storeFile("sampleFile.txt", bais);
        IFileDescriptor fileDescriptor = FileTransferUtil.readAFile(ftpSenderDirectory);
        bais = new ByteArrayInputStream(fileDescriptor.getFileContent());
        long initTime = System.currentTimeMillis();
        ftp.storeFile(fileDescriptor.getFileName(), bais);
        long endTime = System.currentTimeMillis();
        System.out.println("File " + fileDescriptor.getFileName() + " transfer by FTP in " + (endTime - initTime) + " miliseconds.");
    }

