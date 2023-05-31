    public boolean compile(URL url) {
        try {
            final InputStream stream = url.openStream();
            final InputSource input = new InputSource(stream);
            input.setSystemId(url.toString());
            return compile(input, _underscoreclassName);
        } catch (IOException e) {
            _underscoreparser.reportError(Constants.FATAL, new ErrorMsg(e));
            return false;
        }
    }

