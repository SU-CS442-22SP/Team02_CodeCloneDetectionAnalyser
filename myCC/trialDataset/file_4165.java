    @Test
    public void test01_underscoreok_underscorefailed_underscore500_underscoreno_underscorelogo() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(xlsURL);
            HttpResponse response = client.execute(post);
            assertEquals("failed code for ", 500, response.getStatusLine().getStatusCode());
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

