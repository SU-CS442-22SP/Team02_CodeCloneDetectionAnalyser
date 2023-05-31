    protected String loadPage(String url_underscorestring) {
        try {
            URL url = new URL(url_underscorestring);
            HttpURLConnection connection = null;
            InputStream is = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_underscoreACCEPTED || response == HttpURLConnection.HTTP_underscoreOK) {
                    is = connection.getInputStream();
                    String page = "";
                    while (page.length() < MAX_underscorePAGE_underscoreSIZE) {
                        byte[] buffer = new byte[2048];
                        int len = is.read(buffer);
                        if (len < 0) {
                            break;
                        }
                        page += new String(buffer, 0, len);
                    }
                    return (page);
                } else {
                    informFailure("httpinvalidresponse", "" + response);
                    return (null);
                }
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (Throwable e) {
                    Debug.printStackTrace(e);
                }
            }
        } catch (Throwable e) {
            informFailure("httploadfail", e.toString());
            return (null);
        }
    }

