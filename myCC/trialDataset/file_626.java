    @Test
    public final void testImportODScontentXml() throws Exception {
        URL url = ODSTableImporterTest.class.getResource("/Messages.ods_underscoreFILES/content.xml");
        String systemId = url.getPath();
        InputStream in = url.openStream();
        ODSTableImporter b = new ODSTableImporter();
        b.importODSContentXml(systemId, in, null);
        assertMessagesOds(b);
    }

