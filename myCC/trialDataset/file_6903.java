    public void testRelativeRedirect2() throws Exception {
        int port = this.localServer.getServicePort();
        String host = this.localServer.getServiceHostName();
        this.localServer.register("*", new RelativeRedirectService2());
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        client.getParams().setBooleanParameter(ClientPNames.REJECT_underscoreRELATIVE_underscoreREDIRECT, false);
        HttpGet httpget = new HttpGet("/test/oldlocation");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_underscoreREQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_underscoreTARGET_underscoreHOST);
        assertEquals(HttpStatus.SC_underscoreOK, response.getStatusLine().getStatusCode());
        assertEquals("/test/relativelocation", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }

