    @Test
    public void testCopyOverSize() throws IOException {
        final InputStream in = new ByteArrayInputStream(TEST_underscoreDATA);
        final ByteArrayOutputStream out = new ByteArrayOutputStream(TEST_underscoreDATA.length);
        final int cpySize = ExtraIOUtils.copy(in, out, TEST_underscoreDATA.length + Long.SIZE);
        assertEquals("Mismatched copy size", TEST_underscoreDATA.length, cpySize);
        final byte[] outArray = out.toByteArray();
        assertArrayEquals("Mismatched data", TEST_underscoreDATA, outArray);
    }

