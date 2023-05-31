    @Test
    public void testCopy_underscorereaderToOutputStream_underscorenullIn() throws Exception {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStreamTest(baout, true, true);
        try {
            IOUtils.copy((Reader) null, out);
            fail();
        } catch (NullPointerException ex) {
        }
    }

