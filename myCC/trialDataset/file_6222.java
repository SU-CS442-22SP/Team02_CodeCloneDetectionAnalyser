    public HttpURLConnection getConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = null;
        if (_underscoreproxy == null) {
            connection = (HttpURLConnection) url.openConnection();
        } else {
            URLConnection con = url.openConnection(_underscoreproxy);
            String encodedUserPwd = new String(Base64.encodeBase64((_underscoreusername + ":" + _underscorepassword).getBytes()));
            con.setRequestProperty("Proxy-Authorization", "Basic " + encodedUserPwd);
            connection = (HttpURLConnection) con;
        }
        return connection;
    }

