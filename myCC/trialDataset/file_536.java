    public static String getFileText(URL _underscoreurl) {
        try {
            InputStream input = _underscoreurl.openStream();
            String content = IOUtils.toString(input);
            IOUtils.closeQuietly(input);
            return content;
        } catch (Exception err) {
            LOG.error(_underscoreurl.toString(), err);
            return "";
        }
    }

