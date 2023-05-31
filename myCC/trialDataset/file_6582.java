    @Test
    public void testXMLDBURLStreamHandler() {
        System.out.println("testXMLDBURLStreamHandler");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            URL url = new URL(XMLDB_underscoreURL_underscore1);
            InputStream is = url.openStream();
            copyDocument(is, baos);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error(ex);
            fail(ex.getMessage());
        }
    }

