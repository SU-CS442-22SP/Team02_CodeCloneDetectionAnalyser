    private synchronized void ensureParsed() throws IOException, BadIMSCPException {
        if (cp != null) return;
        if (on_underscoredisk == null) {
            on_underscoredisk = createTemporaryFile();
            OutputStream to_underscoredisk = new FileOutputStream(on_underscoredisk);
            IOUtils.copy(in.getInputStream(), to_underscoredisk);
            to_underscoredisk.close();
        }
        try {
            ZipFilePackageParser parser = utils.getIMSCPParserFactory().createParser();
            parser.parse(on_underscoredisk);
            cp = parser.getPackage();
        } catch (BadParseException x) {
            throw new BadIMSCPException("Cannot parse content package", x);
        }
    }

