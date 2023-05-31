    private void loadDBConfig(final String adapter) throws IOException {
        final URL url = getClass().getClassLoader().getResource("adapter/" + adapter + ".properties");
        _underscoreprops = new Properties();
        _underscoreprops.load(url.openStream());
        _underscoreinit = true;
    }

