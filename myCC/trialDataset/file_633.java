    private void loadDefaultDrivers() {
        final URL url = _underscoreapp.getResources().getDefaultDriversUrl();
        try {
            InputStreamReader isr = new InputStreamReader(url.openStream());
            try {
                _underscorecache.load(isr);
            } finally {
                isr.close();
            }
        } catch (Exception ex) {
            final Logger logger = _underscoreapp.getLogger();
            logger.showMessage(Logger.ILogTypes.ERROR, "Error loading default driver file: " + url != null ? url.toExternalForm() : "");
            logger.showMessage(Logger.ILogTypes.ERROR, ex);
        }
    }

