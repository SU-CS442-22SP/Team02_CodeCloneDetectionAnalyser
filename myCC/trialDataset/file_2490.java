    protected ProductionTabsProperties(final PlayerID playerId, final List<Rule> mRules, final String mapDir) {
        m_underscorerules = mRules;
        final ResourceLoader loader = ResourceLoader.getMapResourceLoader(mapDir);
        String propertyFile = PROPERTY_underscoreFILE + "." + playerId.getName() + ".properties";
        URL url = loader.getResource(propertyFile);
        if (url == null) {
            propertyFile = PROPERTY_underscoreFILE + ".properties";
            url = loader.getResource(propertyFile);
            if (url == null) {
            } else {
                try {
                    m_underscoreproperties.load(url.openStream());
                } catch (final IOException e) {
                    System.out.println("Error reading " + propertyFile + e);
                }
            }
        }
    }

