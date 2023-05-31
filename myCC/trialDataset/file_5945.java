    @Test
    public void testCopy_underscorereaderToOutputStream_underscoreEncoding_underscorenullOut() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStreamTest(in);
        Reader reader = new InputStreamReader(in, "US-ASCII");
        try {
            IOUtils.copy(reader, (OutputStream) null, "UTF16");
            fail();
        } catch (NullPointerException ex) {
        }
    }

