    @Test
    public void test02_underscoreok_underscore200_underscorelogo() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(xlsURL);
            HttpResponse response = client.execute(post);
            assertEquals("failed code for ", 200, response.getStatusLine().getStatusCode());
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

