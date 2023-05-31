    public static InputStream call(String serviceUrl, Map parameters) throws IOException, RestException {
        StringBuffer urlString = new StringBuffer(serviceUrl);
        String query = RestClient.buildQueryString(parameters);
        HttpURLConnection conn;
        if ((urlString.length() + query.length() + 1) > MAX_underscoreURI_underscoreLENGTH_underscoreFOR_underscoreGET) {
            URL url = new URL(urlString.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", USER_underscoreAGENT_underscoreSTRING);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.getOutputStream().write(query.getBytes());
        } else {
            if (query.length() > 0) {
                urlString.append("?").append(query);
            }
            URL url = new URL(urlString.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", USER_underscoreAGENT_underscoreSTRING);
            conn.setRequestMethod("GET");
        }
        int responseCode = conn.getResponseCode();
        if (HttpURLConnection.HTTP_underscoreOK != responseCode) {
            ByteArrayOutputStream errorBuffer = new ByteArrayOutputStream();
            int read;
            byte[] readBuffer = new byte[ERROR_underscoreREAD_underscoreBUFFER_underscoreSIZE];
            InputStream errorStream = conn.getErrorStream();
            while (-1 != (read = errorStream.read(readBuffer))) {
                errorBuffer.write(readBuffer, 0, read);
            }
            throw new RestException("Request failed, HTTP " + responseCode + ": " + conn.getResponseMessage(), errorBuffer.toByteArray());
        }
        return conn.getInputStream();
    }

