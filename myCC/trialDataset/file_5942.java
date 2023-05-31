    @Test
    public void testCopy_underscorereaderToOutputStream_underscorenullOut() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStreamTest(in);
        Reader reader = new InputStreamReader(in, "US-ASCII");
        try {
            IOUtils.copy(reader, (OutputStream) null);
            fail();
        } catch (NullPointerException ex) {
        }
    }

