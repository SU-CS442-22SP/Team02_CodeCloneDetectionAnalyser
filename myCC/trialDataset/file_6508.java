        @Override
        public boolean exists() {
            if (local_underscorefile.exists()) {
                return true;
            } else {
                try {
                    URLConnection c = remote_underscoreurl.openConnection();
                    try {
                        c.setConnectTimeout(CIO.getLoadingTimeOut());
                        c.connect();
                        return c.getContentLength() > 0;
                    } catch (Exception err) {
                        err.printStackTrace();
                        return false;
                    } finally {
                        if (c instanceof HttpURLConnection) {
                            ((HttpURLConnection) c).disconnect();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

