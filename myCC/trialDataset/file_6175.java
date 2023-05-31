    public void test_underscoreUseCache_underscoreHttpURLConnection_underscoreNoCached_underscoreGetOutputStream() throws Exception {
        ResponseCache.setDefault(new MockNonCachedResponseCache());
        uc = (HttpURLConnection) url.openConnection();
        uc.setChunkedStreamingMode(10);
        uc.setDoOutput(true);
        uc.getOutputStream();
        assertTrue(isGetCalled);
        assertFalse(isPutCalled);
        assertFalse(isAbortCalled);
        uc.disconnect();
    }

