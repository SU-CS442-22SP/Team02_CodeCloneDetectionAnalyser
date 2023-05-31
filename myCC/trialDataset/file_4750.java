    private void _underscorescanForMetaData(URL _underscoreurl) throws java.io.IOException {
        if (DEBUG.Enabled) System.out.println(this + " _underscorescanForMetaData: xml props " + mXMLpropertyList);
        if (DEBUG.Enabled) System.out.println("*** Opening connection to " + _underscoreurl);
        markAccessAttempt();
        Properties metaData = scrapeHTMLmetaData(_underscoreurl.openConnection(), 2048);
        if (DEBUG.Enabled) System.out.println("*** Got meta-data " + metaData);
        markAccessSuccess();
        String title = metaData.getProperty("title");
        if (title != null && title.length() > 0) {
            setProperty("title", title);
            title = title.replace('\n', ' ').trim();
            setTitle(title);
        }
        try {
            setByteSize(Integer.parseInt((String) getProperty("contentLength")));
        } catch (Exception e) {
        }
    }

