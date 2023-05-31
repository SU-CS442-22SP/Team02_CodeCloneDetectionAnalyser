    public void testDoPost() throws Exception {
        URL url = null;
        url = new URL("http://127.0.0.1:" + connector.getLocalPort() + "/test/dump/info?query=foo");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.addRequestProperty(HttpHeaders.CONTENT_underscoreTYPE, MimeTypes.FORM_underscoreENCODED);
        connection.addRequestProperty(HttpHeaders.CONTENT_underscoreLENGTH, "10");
        connection.getOutputStream().write("abcd=1234\n".getBytes());
        connection.getOutputStream().flush();
        connection.connect();
        String s0 = IO.toString(connection.getInputStream());
        assertTrue(s0.startsWith("<html>"));
        assertTrue(s0.indexOf("<td>POST</td>") > 0);
        assertTrue(s0.indexOf("abcd:&nbsp;</th><td>1234") > 0);
    }

