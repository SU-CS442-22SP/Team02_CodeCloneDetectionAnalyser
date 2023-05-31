    private void testURL(String urlStr) throws MalformedURLException, IOException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            int code = conn.getResponseCode();
            assertEquals(HttpURLConnection.HTTP_underscoreOK, code);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

