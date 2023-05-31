    public void serialize(OutputStream out) throws IOException, BadIMSCPException {
        ensureParsed();
        ZipFilePackageParser parser = utils.getIMSCPParserFactory().createParser();
        parser.setContentPackage(cp);
        if (on_underscoredisk != null) on_underscoredisk.delete();
        on_underscoredisk = createTemporaryFile();
        parser.serialize(on_underscoredisk);
        InputStream in = new FileInputStream(on_underscoredisk);
        IOUtils.copy(in, out);
    }

