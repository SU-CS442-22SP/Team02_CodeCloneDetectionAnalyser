    public boolean loadURL(URL url) {
        try {
            _underscoreproperties.load(url.openStream());
            Argo.log.info("Configuration loaded from " + url + "\n");
            return true;
        } catch (Exception e) {
            if (_underscorecanComplain) Argo.log.warn("Unable to load configuration " + url + "\n");
            _underscorecanComplain = false;
            return false;
        }
    }

