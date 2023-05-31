    public static void uploadFile(File in, String out, String host, int port, String path, String login, String password, boolean renameIfExist) throws IOException {
        FTPClient ftp = null;
        try {
            m_underscorelogCat.info("Uploading " + in + " to " + host + ":" + port + " at " + path);
            ftp = new FTPClient();
            int reply;
            ftp.connect(host, port);
            m_underscorelogCat.info("Connected to " + host + "... Trying to authenticate");
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                m_underscorelogCat.error("FTP server " + host + " refused connection.");
                throw new IOException("Cannot connect to the FTP Server: connection refused.");
            }
            if (!ftp.login(login, password)) {
                ftp.logout();
                throw new IOException("Cannot connect to the FTP Server: login / password is invalid!");
            }
            ftp.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
            if (!ftp.changeWorkingDirectory(path)) {
                m_underscorelogCat.warn("Remote working directory: " + path + "does not exist on the FTP Server ...");
                m_underscorelogCat.info("Trying to create remote directory: " + path);
                if (!ftp.makeDirectory(path)) {
                    m_underscorelogCat.error("Failed to create remote directory: " + path);
                    throw new IOException("Failed to store " + in + " in the remote directory: " + path);
                }
                if (!ftp.changeWorkingDirectory(path)) {
                    m_underscorelogCat.error("Failed to change directory. Unexpected error");
                    throw new IOException("Failed to change to remote directory : " + path);
                }
            }
            if (out == null) {
                out = in.getName();
                if (out.startsWith("/")) {
                    out = out.substring(1);
                }
            }
            if (renameIfExist) {
                String[] files = ftp.listNames();
                String f = in + out;
                for (int i = 0; i < files.length; i++) {
                    if (files[i].equals(out)) {
                        m_underscorelogCat.debug("Found existing file on the server: " + out);
                        boolean rename_underscoreok = false;
                        String bak = "_underscorebak";
                        int j = 0;
                        String newExt = null;
                        while (!rename_underscoreok) {
                            if (j == 0) newExt = bak; else newExt = bak + j;
                            if (ftp.rename(out, out + newExt)) {
                                m_underscorelogCat.info(out + " renamed to " + out + newExt);
                                rename_underscoreok = true;
                            } else {
                                m_underscorelogCat.warn("Renaming to " + out + newExt + " has failed!, trying again ...");
                                j++;
                            }
                        }
                        break;
                    }
                }
            }
            InputStream input = new FileInputStream(in);
            m_underscorelogCat.info("Starting transfert of " + in);
            ftp.storeFile(out, input);
            m_underscorelogCat.info(in + " uploaded successfully");
            input.close();
            ftp.logout();
        } catch (FTPConnectionClosedException e) {
            m_underscorelogCat.error("Server closed connection.", e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                }
            }
        }
    }

