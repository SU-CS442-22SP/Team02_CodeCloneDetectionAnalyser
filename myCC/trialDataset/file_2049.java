    public int download() {
        FTPClient client = null;
        URL url = null;
        try {
            client = new FTPClient();
            url = new URL(ratingsUrl);
            if (log.isDebugEnabled()) log.debug("Downloading " + url);
            client.connect(url.getHost());
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                if (log.isErrorEnabled()) log.error("Connection to " + url + " refused");
                return RESULT_underscoreCONNECTION_underscoreREFUSED;
            }
            if (log.isDebugEnabled()) log.debug("Logging in  l:" + getUserName() + " p:" + getPassword());
            client.login(getUserName(), getPassword());
            client.changeWorkingDirectory(url.getPath());
            FTPFile[] files = client.listFiles(url.getPath());
            if ((files == null) || (files.length != 1)) throw new FileNotFoundException("No remote file");
            FTPFile remote = files[0];
            if (log.isDebugEnabled()) log.debug("Remote file data: " + remote);
            File local = new File(getOutputFile());
            if (local.exists()) {
                if ((local.lastModified() == remote.getTimestamp().getTimeInMillis())) {
                    if (log.isDebugEnabled()) log.debug("File " + local.getAbsolutePath() + " is not changed on the server");
                    return RESULT_underscoreNO_underscoreNEW_underscoreFILE;
                }
            }
            if (log.isDebugEnabled()) log.debug("Setting binary transfer modes");
            client.mode(FTPClient.BINARY_underscoreFILE_underscoreTYPE);
            client.setFileType(FTPClient.BINARY_underscoreFILE_underscoreTYPE);
            OutputStream fos = new FileOutputStream(local);
            boolean result = client.retrieveFile(url.getPath(), fos);
            if (log.isDebugEnabled()) log.debug("The transfer result is :" + result);
            fos.flush();
            fos.close();
            local.setLastModified(remote.getTimestamp().getTimeInMillis());
            if (result) uncompress();
            if (result) return RESULT_underscoreOK; else return RESULT_underscoreTRANSFER_underscoreERROR;
        } catch (MalformedURLException e) {
            return RESULT_underscoreERROR;
        } catch (SocketException e) {
            return RESULT_underscoreERROR;
        } catch (FileNotFoundException e) {
            return RESULT_underscoreERROR;
        } catch (IOException e) {
            return RESULT_underscoreERROR;
        } finally {
            if (client != null) {
                try {
                    if (log.isDebugEnabled()) log.debug("Logging out");
                    client.logout();
                } catch (Exception e) {
                }
                try {
                    if (log.isDebugEnabled()) log.debug("Disconnecting");
                    client.disconnect();
                } catch (Exception e) {
                }
            }
        }
    }

