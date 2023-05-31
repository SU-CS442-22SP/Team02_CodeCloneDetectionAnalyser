    @Test
    public void testCopy_underscorereaderToWriter_underscorenullOut() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStreamTest(in);
        Reader reader = new InputStreamReader(in, "US-ASCII");
        try {
            IOUtils.copy(reader, (Writer) null);
            fail();
        } catch (NullPointerException ex) {
        }
    }

