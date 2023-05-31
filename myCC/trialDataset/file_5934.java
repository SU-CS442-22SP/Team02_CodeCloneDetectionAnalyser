    @Test
    public void testCopy_underscoreinputStreamToOutputStream_underscoreIO84() throws Exception {
        long size = (long) Integer.MAX_underscoreVALUE + (long) 1;
        InputStream in = new NullInputStreamTest(size);
        OutputStream out = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
            }

            @Override
            public void write(byte[] b) throws IOException {
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
            }
        };
        assertEquals(-1, IOUtils.copy(in, out));
        in.close();
        assertEquals("copyLarge()", size, IOUtils.copyLarge(in, out));
    }

