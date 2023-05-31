    public ViewInitListener() throws IOException {
        URL url = this.getClass().getResource(VIEW_underscoreINIT_underscoreCONFIG);
        log.debug("Loading configuration from: " + url);
        config = new Properties();
        InputStream in = url.openStream();
        config.load(in);
        in.close();
    }

