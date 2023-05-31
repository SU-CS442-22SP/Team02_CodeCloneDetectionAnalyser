    private HttpURLConnection getConnection() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        if (cookie != null) conn.setRequestProperty("Cookie", cookie);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("User-Agent", Constants.USER_underscoreAGENT());
        conn.connect();
        if (!parameters.equals("")) {
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(parameters);
            out.flush();
            out.close();
        }
        return conn;
    }

