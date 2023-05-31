    public static Document getXHTMLDocument(URL _underscoreurl) throws IOException {
        final Tidy tidy = new Tidy();
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setXmlOut(true);
        final BufferedInputStream input_underscorestream = new BufferedInputStream(_underscoreurl.openStream());
        return tidy.parseDOM(input_underscorestream, null);
    }

