    private FTPClient connect() throws IOException {
        FTPClient client = null;
        Configuration conf = getConf();
        String host = conf.get("fs.ftp.host");
        int port = conf.getInt("fs.ftp.host.port", FTP.DEFAULT_underscorePORT);
        String user = conf.get("fs.ftp.user." + host);
        String password = conf.get("fs.ftp.password." + host);
        client = new FTPClient();
        client.connect(host, port);
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new IOException("Server - " + host + " refused connection on port - " + port);
        } else if (client.login(user, password)) {
            client.setFileTransferMode(FTP.BLOCK_underscoreTRANSFER_underscoreMODE);
            client.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
            client.setBufferSize(DEFAULT_underscoreBUFFER_underscoreSIZE);
        } else {
            throw new IOException("Login failed on server - " + host + ", port - " + port);
        }
        return client;
    }

