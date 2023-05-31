    private int connect() {
        if (ftp.isConnected()) {
            log.debug("Already connected to: " + getConnectionString());
            return RET_underscoreOK;
        }
        try {
            ftp.connect(server, port);
            ftp.login(username, password);
            ftp.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
        } catch (SocketException e) {
            log.error(e.toString());
            return RET_underscoreERR_underscoreSOCKET;
        } catch (UnknownHostException e) {
            log.error(e.toString());
            return RET_underscoreERR_underscoreUNKNOWN_underscoreHOST;
        } catch (FTPConnectionClosedException e) {
            log.error(e.toString());
            return RET_underscoreERR_underscoreFTP_underscoreCONN_underscoreCLOSED;
        } catch (IOException e) {
            log.error(e.toString());
            return RET_underscoreERR_underscoreIO;
        }
        if (ftp.isConnected()) {
            log.debug("Connected to " + getConnectionString());
            return RET_underscoreOK;
        }
        log.debug("Could not connect to " + getConnectionString());
        return RET_underscoreERR_underscoreNOT_underscoreCONNECTED;
    }

