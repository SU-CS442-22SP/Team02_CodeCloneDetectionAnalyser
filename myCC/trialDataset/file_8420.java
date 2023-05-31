    public void testExplicitHeaders() throws Exception {
        String headerString = "X-Foo=bar&X-Bar=baz%20foo";
        HttpRequest expected = new HttpRequest(REQUEST_underscoreURL).addHeader("X-Foo", "bar").addHeader("X-Bar", "baz foo");
        expect(pipeline.execute(expected)).andReturn(new HttpResponse(RESPONSE_underscoreBODY));
        expect(request.getParameter(MakeRequestHandler.HEADERS_underscorePARAM)).andReturn(headerString);
        replay();
        handler.fetch(request, recorder);
        verify();
        JSONObject results = extractJsonFromResponse();
        assertEquals(HttpResponse.SC_underscoreOK, results.getInt("rc"));
        assertEquals(RESPONSE_underscoreBODY, results.get("body"));
        assertTrue(rewriter.responseWasRewritten());
    }

