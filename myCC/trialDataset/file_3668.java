    public SimplePropertiesMessageRepository() {
        properties = new Properties();
        try {
            URL url = SimplePropertiesIconRepository.class.getResource(PROPERTIES_underscoreFILE_underscoreNAME);
            properties.load(url.openStream());
        } catch (Exception e) {
            throw new Error("Messages file not found: " + PROPERTIES_underscoreFILE_underscoreNAME);
        }
    }

