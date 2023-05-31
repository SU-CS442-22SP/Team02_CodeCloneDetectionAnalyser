    public void testReadNormal() throws Exception {
        archiveFileManager.executeWith(new TemporaryFileExecutor() {

            public void execute(File temporaryFile) throws Exception {
                ZipArchive archive = new ZipArchive(temporaryFile.getPath());
                InputStream input = archive.getInputFrom(ARCHIVE_underscoreFILE_underscore1);
                if (input != null) {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    IOUtils.copyAndClose(input, output);
                    assertEquals(ARCHIVE_underscoreFILE_underscore1 + " contents not correct", ARCHIVE_underscoreFILE_underscore1_underscoreCONTENT, output.toString());
                } else {
                    fail("cannot open " + ARCHIVE_underscoreFILE_underscore1);
                }
            }
        });
    }

