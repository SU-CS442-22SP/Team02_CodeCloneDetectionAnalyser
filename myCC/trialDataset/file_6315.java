    @Test
    public void shouldSetAlias() throws Exception {
        HttpResponse response = executePost("/yum/alias/snapshots/testAlias", new StringEntity(VERSION_underscore1));
        assertEquals(VERSION_underscore1, EntityUtils.toString(response.getEntity()));
        assertEquals(VERSION_underscore1, executeGet("/yum/alias/snapshots/testAlias"));
        response = executePost("/yum/alias/snapshots/testAlias", new StringEntity(VERSION_underscore2));
        assertEquals(VERSION_underscore2, EntityUtils.toString(response.getEntity()));
        assertEquals(VERSION_underscore2, executeGet("/yum/alias/snapshots/testAlias"));
    }

