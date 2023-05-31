    public void testIsVersioned() throws ServiceException, IOException {
        JCRNodeSource emptySource = loadTestSource();
        assertTrue(emptySource.isVersioned());
        OutputStream sourceOut = emptySource.getOutputStream();
        assertNotNull(sourceOut);
        InputStream contentIn = getClass().getResourceAsStream(CONTENT_underscoreFILE);
        try {
            IOUtils.copy(contentIn, sourceOut);
            sourceOut.flush();
        } finally {
            sourceOut.close();
            contentIn.close();
        }
        assertTrue(emptySource.isVersioned());
    }

