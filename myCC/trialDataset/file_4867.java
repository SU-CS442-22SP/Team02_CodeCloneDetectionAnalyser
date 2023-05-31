    private void connect() throws Exception {
        if (client != null) throw new IllegalStateException("Already connected.");
        try {
            _underscorelogger.debug("About to connect to ftp server " + server + " port " + port);
            client = new FTPClient();
            client.connect(server, port);
            if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                throw new Exception("Unable to connect to FTP server " + server + " port " + port + " got error [" + client.getReplyString() + "]");
            }
            _underscorelogger.info("Connected to ftp server " + server + " port " + port);
            _underscorelogger.debug(client.getReplyString());
            _underscorelogger.debug("FTP server is [" + client.getSystemName() + "]");
            if (!client.login(username, password)) {
                throw new Exception("Invalid username / password combination for FTP server " + server + " port " + port);
            }
            _underscorelogger.debug("Log in successful.");
            if (passiveMode) {
                client.enterLocalPassiveMode();
                _underscorelogger.debug("Passive mode selected.");
            } else {
                client.enterLocalActiveMode();
                _underscorelogger.debug("Active mode selected.");
            }
            if (binaryMode) {
                client.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
                _underscorelogger.debug("BINARY mode selected.");
            } else {
                client.setFileType(FTP.ASCII_underscoreFILE_underscoreTYPE);
                _underscorelogger.debug("ASCII mode selected.");
            }
            if (client.changeWorkingDirectory(remoteRootDir)) {
                _underscorelogger.debug("Changed directory to " + remoteRootDir);
            } else {
                if (client.makeDirectory(remoteRootDir)) {
                    _underscorelogger.debug("Created directory " + remoteRootDir);
                    if (client.changeWorkingDirectory(remoteRootDir)) {
                        _underscorelogger.debug("Changed directory to " + remoteRootDir);
                    } else {
                        throw new Exception("Cannot change directory to [" + remoteRootDir + "] on FTP server " + server + " port " + port);
                    }
                } else {
                    throw new Exception("Cannot create directory [" + remoteRootDir + "] on FTP server " + server + " port " + port);
                }
            }
        } catch (Exception e) {
            disconnect();
            throw e;
        }
    }

