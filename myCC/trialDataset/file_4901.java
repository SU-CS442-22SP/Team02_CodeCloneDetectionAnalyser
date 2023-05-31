    @Test
    public void testImageshackUpload() throws Exception {
        request.setUrl("http://www.imageshack.us/index.php");
        request.addParameter("xml", "yes");
        request.setFile("fileupload", file);
        HttpResponse response = httpClient.execute(request);
        assertTrue(response.is2xxSuccess());
        assertTrue(response.getResponseHeaders().size() > 0);
        String body = IOUtils.toString(response.getResponseBody());
        assertTrue(body.contains("<image_underscorelink>"));
        assertTrue(body.contains("<thumb_underscorelink>"));
        assertTrue(body.contains("<image_underscorelocation>"));
        assertTrue(body.contains("<image_underscorename>"));
        response.close();
    }

