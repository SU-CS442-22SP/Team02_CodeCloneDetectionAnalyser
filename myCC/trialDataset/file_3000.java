    public boolean compile(URL url, String name) {
        try {
            final InputStream stream = url.openStream();
            final InputSource input = new InputSource(stream);
            input.setSystemId(url.toString());
            return compile(input, name);
        } catch (IOException e) {
            _underscoreparser.reportError(Constants.FATAL, new ErrorMsg(e));
            return false;
        }
    }

