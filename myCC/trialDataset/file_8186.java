    @Override
    public void connect() throws Exception {
        if (client != null) {
            _underscorelogger.warn("Already connected.");
            return;
        }
        try {
            _underscorelogger.debug("About to connect to ftp server " + server + " port " + port);
            client = new FTPClient();
            client.connect(server, port);
            if (!FTPReply.isPositiveCompletion(client.getReplyCode())) throw new Exception("Unable to connect to FTP server " + server + " port " + port + " got error [" + client.getReplyString() + "]");
            _underscorelogger.info("Connected to ftp server " + server + " port " + port);
            _underscorelogger.debug(client.getReplyString());
            if (!client.login(username, password)) throw new Exception("Invalid username / password combination for FTP server " + server + " port " + port);
            _underscorelogger.debug("Log in successful.");
            _underscorelogger.info("FTP server is [" + client.getSystemType() + "]");
            if (passiveMode) {
                client.enterLocalPassiveMode();
                _underscorelogger.info("Passive mode selected.");
            } else {
                client.enterLocalActiveMode();
                _underscorelogger.info("Active mode selected.");
            }
            if (binaryMode) {
                client.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
                _underscorelogger.info("BINARY mode selected.");
            } else {
                client.setFileType(FTP.ASCII_underscoreFILE_underscoreTYPE);
                _underscorelogger.info("ASCII mode selected.");
            }
            if (client.changeWorkingDirectory(remoteRootDir)) {
                _underscorelogger.info("Changed directory to " + remoteRootDir);
            } else {
                throw new Exception("Cannot change directory to [" + remoteRootDir + "] on FTP server " + server + " port " + port);
            }
        } catch (Exception e) {
            _underscorelogger.error("Failed to connect to the FTP server " + server + " on port " + port, e);
            disconnect();
            throw e;
        }
    }

