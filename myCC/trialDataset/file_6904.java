    public void testRedirectWithCookie() throws Exception {
        String host = "localhost";
        int port = this.localServer.getServicePort();
        this.localServer.register("*", new BasicRedirectService(host, port));
        DefaultHttpClient client = new DefaultHttpClient();
        CookieStore cookieStore = new BasicCookieStore();
        client.setCookieStore(cookieStore);
        BasicClientCookie cookie = new BasicClientCookie("name", "value");
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_underscoreREQUEST);
        assertEquals(HttpStatus.SC_underscoreOK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        Header[] headers = reqWrapper.getHeaders(SM.COOKIE);
        assertEquals("There can only be one (cookie)", 1, headers.length);
    }

