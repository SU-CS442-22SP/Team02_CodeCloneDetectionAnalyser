    @Override
    public void end() {
        m_underscorezipFormatter.end();
        IOUtils.closeQuietly(m_underscoreoutputStream);
        final FTPClient ftp = new FTPClient();
        FileInputStream fis = null;
        try {
            if (m_underscoreurl.getPort() == -1 || m_underscoreurl.getPort() == 0 || m_underscoreurl.getPort() == m_underscoreurl.getDefaultPort()) {
                ftp.connect(m_underscoreurl.getHost());
            } else {
                ftp.connect(m_underscoreurl.getHost(), m_underscoreurl.getPort());
            }
            if (m_underscoreurl.getUserInfo() != null && m_underscoreurl.getUserInfo().length() > 0) {
                final String[] userInfo = m_underscoreurl.getUserInfo().split(":", 2);
                ftp.login(userInfo[0], userInfo[1]);
            } else {
                ftp.login("anonymous", "opennmsftp@");
            }
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                LogUtils.errorf(this, "FTP server refused connection.");
                return;
            }
            String path = m_underscoreurl.getPath();
            if (path.endsWith("/")) {
                LogUtils.errorf(this, "Your FTP URL must specify a filename.");
                return;
            }
            File f = new File(path);
            path = f.getParent();
            if (!ftp.changeWorkingDirectory(path)) {
                LogUtils.infof(this, "unable to change working directory to %s", path);
                return;
            }
            LogUtils.infof(this, "uploading %s to %s", f.getName(), path);
            ftp.setFileType(FTP.BINARY_underscoreFILE_underscoreTYPE);
            fis = new FileInputStream(m_underscorezipFile);
            if (!ftp.storeFile(f.getName(), fis)) {
                LogUtils.infof(this, "unable to store file");
                return;
            }
            LogUtils.infof(this, "finished uploading");
        } catch (final Exception e) {
            LogUtils.errorf(this, e, "Unable to FTP file to %s", m_underscoreurl);
        } finally {
            IOUtils.closeQuietly(fis);
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }

