    public void testServletTesterClient() throws Exception {
        String base_underscoreurl = tester.createSocketConnector(true);
        URL url = new URL(base_underscoreurl + "/context/hello/info");
        String result = IO.toString(url.openStream());
        assertEquals("<h1>Hello Servlet</h1>", result);
    }

