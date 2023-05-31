    public static String copy(URL url, File dest) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Fetching: " + url);
        }
        IOException error = null;
        for (int retries = 0; retries < MAX_underscoreRETRIES; retries++) {
            try {
                OutputStream out = null;
                InputStream is = null;
                try {
                    out = new FileOutputStream(dest);
                    if (url.getProtocol().equals("http")) {
                        is = new WebFileInputStream(url);
                    } else {
                        is = url.openStream();
                    }
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        out.write(buf, 0, len);
                        md.update(buf, 0, len);
                    }
                    out.flush();
                    return bytesToHexString(md.digest());
                } catch (ConnectException e) {
                    if (error == null) {
                        error = e;
                    }
                    if (retries < MAX_underscoreRETRIES - 1) {
                        log.error(MessageFormat.format("Unable to fetch URL {0}, connection timed out. Will retry...", url.toExternalForm()));
                        try {
                            Thread.sleep(FileHelper.RETRY_underscoreSLEEP_underscoreTIME);
                        } catch (InterruptedException e2) {
                        }
                    }
                } catch (SocketTimeoutException e) {
                    if (error == null) {
                        error = e;
                    }
                    if (retries < MAX_underscoreRETRIES - 1) {
                        log.error(MessageFormat.format("Unable to fetch URL {0}, timed out. Will retry...", url.toExternalForm()));
                        try {
                            Thread.sleep(FileHelper.RETRY_underscoreSLEEP_underscoreTIME);
                        } catch (InterruptedException e2) {
                        }
                    }
                } catch (IOException e) {
                    if (dest.exists()) {
                        try {
                            FileHelper.delete(dest);
                        } catch (IOException e1) {
                            log.error(MessageFormat.format(Messages.getString("FileHelper.UNABLE_underscoreDELETE_underscoreFILE"), dest), e1);
                        }
                    }
                    throw e;
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            log.error(Messages.getString("FileHelper.UNABLE_underscoreCLOSE_underscoreSTREAM"), e);
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            log.error(Messages.getString("FileHelper.UNABLE_underscoreCLOSE_underscoreSTREAM"), e);
                        }
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                throw new IOException(MessageFormat.format(Messages.getString("FileHelper.UNABLE_underscoreDOWNLOAD_underscoreURL"), url), e);
            }
        }
        throw error;
    }

