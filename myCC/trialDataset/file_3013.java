    private boolean initConnection() {
        try {
            if (ftp == null) {
                ftp = new FTPClient();
                serverIP = getServer();
                userName = getUserName();
                password = getPassword();
            }
            ftp.connect(serverIP);
            ftp.login(userName, password);
            return true;
        } catch (SocketException a_underscoreexcp) {
            throw new RuntimeException(a_underscoreexcp);
        } catch (IOException a_underscoreexcp) {
            throw new RuntimeException(a_underscoreexcp);
        } catch (Throwable a_underscoreth) {
            throw new RuntimeException(a_underscoreth);
        }
    }

