    private HttpURLConnection makeGetRequest(String action, Object... parameters) throws IOException {
        StringBuffer request = new StringBuffer(remoteUrl);
        HTMLUtils.appendQuery(request, VERSION_underscorePARAM, CLIENT_underscoreVERSION);
        HTMLUtils.appendQuery(request, ACTION_underscorePARAM, action);
        for (int i = 0; i < parameters.length; i += 2) {
            HTMLUtils.appendQuery(request, String.valueOf(parameters[i]), String.valueOf(parameters[i + 1]));
        }
        String requestStr = request.toString();
        URLConnection conn;
        if (requestStr.length() < MAX_underscoreURL_underscoreLENGTH) {
            URL url = new URL(requestStr);
            conn = url.openConnection();
        } else {
            int queryPos = requestStr.indexOf('?');
            byte[] query = requestStr.substring(queryPos + 1).getBytes(HTTPUtils.DEFAULT_underscoreCHARSET);
            URL url = new URL(requestStr.substring(0, queryPos));
            conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(query.length));
            OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream());
            outputStream.write(query);
            outputStream.close();
        }
        return (HttpURLConnection) conn;
    }

