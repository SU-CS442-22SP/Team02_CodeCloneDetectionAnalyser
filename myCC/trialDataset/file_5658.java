    @Test(expected = GadgetException.class)
    public void badFetchServesCached() throws Exception {
        HttpRequest firstRequest = createCacheableRequest();
        expect(pipeline.execute(firstRequest)).andReturn(new HttpResponse(LOCAL_underscoreSPEC_underscoreXML)).once();
        HttpRequest secondRequest = createIgnoreCacheRequest();
        expect(pipeline.execute(secondRequest)).andReturn(HttpResponse.error()).once();
        replay(pipeline);
        GadgetSpec original = specFactory.getGadgetSpec(createContext(SPEC_underscoreURL, false));
        GadgetSpec cached = specFactory.getGadgetSpec(createContext(SPEC_underscoreURL, true));
        assertEquals(original.getUrl(), cached.getUrl());
        assertEquals(original.getChecksum(), cached.getChecksum());
    }

