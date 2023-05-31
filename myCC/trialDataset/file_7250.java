    public void connect(String method, String data, String urlString, Properties properties, boolean allowredirect) throws Exception {
        if (urlString != null) {
            try {
                url_underscore = new URL(url_underscore, urlString);
            } catch (Exception e) {
                throw new Exception("Invalid URL");
            }
        }
        try {
            httpURLConnection_underscore = (HttpURLConnection) url_underscore.openConnection(siteThread_underscore.getProxy());
            httpURLConnection_underscore.setDoInput(true);
            httpURLConnection_underscore.setDoOutput(true);
            httpURLConnection_underscore.setUseCaches(false);
            httpURLConnection_underscore.setRequestMethod(method);
            httpURLConnection_underscore.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            httpURLConnection_underscore.setInstanceFollowRedirects(allowredirect);
            if (properties != null) {
                for (Object propertyKey : properties.keySet()) {
                    String propertyValue = properties.getProperty((String) propertyKey);
                    if (propertyValue.equalsIgnoreCase("Content-Length")) {
                        httpURLConnection_underscore.setFixedLengthStreamingMode(0);
                    }
                    httpURLConnection_underscore.setRequestProperty((String) propertyKey, propertyValue);
                }
            }
            int connectTimeout = httpURLConnection_underscore.getConnectTimeout();
            if (data != null) {
                post(data);
            }
            httpURLConnection_underscore.connect();
        } catch (Exception e) {
            throw new Exception("Connection failed with url " + url_underscore);
        }
    }

