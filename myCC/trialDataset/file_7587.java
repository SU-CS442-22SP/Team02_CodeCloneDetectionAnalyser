    public static String installOvalDefinitions(final String xml_underscorelocation) {
        InputStream in_underscorestream = null;
        try {
            URL url = _underscoretoURL(xml_underscorelocation);
            if (url == null) {
                in_underscorestream = new FileInputStream(xml_underscorelocation);
            } else {
                in_underscorestream = url.openStream();
            }
        } catch (IOException ex) {
            throw new OvalException(ex);
        }
        Class<OvalDefinitions> type = OvalDefinitions.class;
        OvalDefinitions object = _underscoreunmarshalObject(type, in_underscorestream);
        String pid = _underscoregetDatastore().save(type, object);
        return pid;
    }

