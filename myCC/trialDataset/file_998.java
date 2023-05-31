    public void jsFunction_underscoreextract(ScriptableFile outputFile) throws IOException, FileSystemException, ArchiveException {
        InputStream is = file.jsFunction_underscorecreateInputStream();
        OutputStream output = outputFile.jsFunction_underscorecreateOutputStream();
        BufferedInputStream buf = new BufferedInputStream(is);
        ArchiveInputStream input = ScriptableZipArchive.getFactory().createArchiveInputStream(buf);
        try {
            long count = 0;
            while (input.getNextEntry() != null) {
                if (count == offset) {
                    IOUtils.copy(input, output);
                    break;
                }
                count++;
            }
        } finally {
            input.close();
            output.close();
            is.close();
        }
    }

