    public void jsFunction_underscoreaddFile(ScriptableFile infile) throws IOException {
        if (!infile.jsFunction_underscoreexists()) throw new IllegalArgumentException("Cannot add a file that doesn't exists to an archive");
        ZipArchiveEntry entry = new ZipArchiveEntry(infile.getName());
        entry.setSize(infile.jsFunction_underscoregetSize());
        out.putArchiveEntry(entry);
        try {
            InputStream inStream = infile.jsFunction_underscorecreateInputStream();
            IOUtils.copy(inStream, out);
            inStream.close();
        } finally {
            out.closeArchiveEntry();
        }
    }

