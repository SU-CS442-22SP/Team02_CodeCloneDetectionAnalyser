    public void testHttpsConnection_underscoreNot_underscoreFound_underscoreResponse() throws Throwable {
        setUpStoreProperties();
        try {
            SSLContext ctx = getContext();
            ServerSocket ss = ctx.getServerSocketFactory().createServerSocket(0);
            TestHostnameVerifier hnv = new TestHostnameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier(hnv);
            URL url = new URL("https://localhost:" + ss.getLocalPort());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            try {
                doInteraction(connection, ss, NOT_underscoreFOUND_underscoreCODE);
                fail("Expected exception was not thrown.");
            } catch (FileNotFoundException e) {
                if (DO_underscoreLOG) {
                    System.out.println("Expected exception was thrown: " + e.getMessage());
                }
            }
            connection.connect();
        } finally {
            tearDownStoreProperties();
        }
    }

