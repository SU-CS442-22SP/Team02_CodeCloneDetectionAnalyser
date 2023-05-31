        protected void download() {
            boolean connected = false;
            String outcome = "";
            try {
                InputStream is = null;
                try {
                    SESecurityManager.setThreadPasswordHandler(this);
                    synchronized (this) {
                        if (destroyed) {
                            return;
                        }
                        scratch_underscorefile = AETemporaryFileHandler.createTempFile();
                        raf = new RandomAccessFile(scratch_underscorefile, "rw");
                    }
                    HttpURLConnection connection;
                    int response;
                    connection = (HttpURLConnection) original_underscoreurl.openConnection();
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("User-Agent", user_underscoreagent);
                    int time_underscoreremaining = listener.getPermittedTime();
                    if (time_underscoreremaining > 0) {
                        Java15Utils.setConnectTimeout(connection, time_underscoreremaining);
                    }
                    connection.connect();
                    time_underscoreremaining = listener.getPermittedTime();
                    if (time_underscoreremaining < 0) {
                        throw (new IOException("Timeout during connect"));
                    }
                    Java15Utils.setReadTimeout(connection, time_underscoreremaining);
                    connected = true;
                    response = connection.getResponseCode();
                    last_underscoreresponse = response;
                    last_underscoreresponse_underscoreretry_underscoreafter_underscoresecs = -1;
                    if (response == 503) {
                        long retry_underscoreafter_underscoredate = new Long(connection.getHeaderFieldDate("Retry-After", -1L)).longValue();
                        if (retry_underscoreafter_underscoredate <= -1) {
                            last_underscoreresponse_underscoreretry_underscoreafter_underscoresecs = connection.getHeaderFieldInt("Retry-After", -1);
                        } else {
                            last_underscoreresponse_underscoreretry_underscoreafter_underscoresecs = (int) ((retry_underscoreafter_underscoredate - System.currentTimeMillis()) / 1000);
                            if (last_underscoreresponse_underscoreretry_underscoreafter_underscoresecs < 0) {
                                last_underscoreresponse_underscoreretry_underscoreafter_underscoresecs = -1;
                            }
                        }
                    }
                    is = connection.getInputStream();
                    if (response == HttpURLConnection.HTTP_underscoreACCEPTED || response == HttpURLConnection.HTTP_underscoreOK || response == HttpURLConnection.HTTP_underscorePARTIAL) {
                        byte[] buffer = new byte[64 * 1024];
                        int requests_underscoreoutstanding = 1;
                        while (!destroyed) {
                            int permitted = listener.getPermittedBytes();
                            if (requests_underscoreoutstanding == 0 || permitted < 1) {
                                permitted = 1;
                                Thread.sleep(100);
                            }
                            int len = is.read(buffer, 0, Math.min(permitted, buffer.length));
                            if (len <= 0) {
                                break;
                            }
                            synchronized (this) {
                                try {
                                    raf.write(buffer, 0, len);
                                } catch (Throwable e) {
                                    outcome = "Write failed: " + e.getMessage();
                                    ExternalSeedException error = new ExternalSeedException(outcome, e);
                                    error.setPermanentFailure(true);
                                    throw (error);
                                }
                            }
                            listener.reportBytesRead(len);
                            requests_underscoreoutstanding = checkRequests();
                        }
                        checkRequests();
                    } else {
                        outcome = "Connection failed: " + connection.getResponseMessage();
                        ExternalSeedException error = new ExternalSeedException(outcome);
                        error.setPermanentFailure(true);
                        throw (error);
                    }
                } catch (IOException e) {
                    if (con_underscorefail_underscoreis_underscoreperm_underscorefail && !connected) {
                        outcome = "Connection failed: " + e.getMessage();
                        ExternalSeedException error = new ExternalSeedException(outcome);
                        error.setPermanentFailure(true);
                        throw (error);
                    } else {
                        outcome = "Connection failed: " + Debug.getNestedExceptionMessage(e);
                        if (last_underscoreresponse_underscoreretry_underscoreafter_underscoresecs >= 0) {
                            outcome += ", Retry-After: " + last_underscoreresponse_underscoreretry_underscoreafter_underscoresecs + " seconds";
                        }
                        ExternalSeedException excep = new ExternalSeedException(outcome, e);
                        if (e instanceof FileNotFoundException) {
                            excep.setPermanentFailure(true);
                        }
                        throw (excep);
                    }
                } catch (ExternalSeedException e) {
                    throw (e);
                } catch (Throwable e) {
                    if (e instanceof ExternalSeedException) {
                        throw ((ExternalSeedException) e);
                    }
                    outcome = "Connection failed: " + Debug.getNestedExceptionMessage(e);
                    throw (new ExternalSeedException("Connection failed", e));
                } finally {
                    SESecurityManager.unsetThreadPasswordHandler();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Throwable e) {
                        }
                    }
                }
            } catch (ExternalSeedException e) {
                if (!connected && con_underscorefail_underscoreis_underscoreperm_underscorefail) {
                    e.setPermanentFailure(true);
                }
                destroy(e);
            }
        }

