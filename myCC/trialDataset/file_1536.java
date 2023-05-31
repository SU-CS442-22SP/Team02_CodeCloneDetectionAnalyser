    @Test
    public void testValidLogConfiguration() throws IOException, IllegalArgumentException {
        URL url = ClassLoader.getSystemResource(PROPERTIES_underscoreFILE_underscoreNAME);
        if (url == null) {
            throw new IOException("Could not find configuration file " + PROPERTIES_underscoreFILE_underscoreNAME + " in class path");
        }
        Properties properties = new Properties();
        properties.load(url.openStream());
        LogLevel logLevel = LogLevel.valueOf((String) properties.get(PROPERTY_underscoreKEY_underscoreLOGLEVEL));
        if (logLevel == null) {
            throw new IOException("Invalid configuration file " + PROPERTIES_underscoreFILE_underscoreNAME + ": no entry for " + PROPERTY_underscoreKEY_underscoreLOGLEVEL);
        }
        String loggerIdentifier = "Test logger";
        Logger logger = LoggerFactory.getLogger(loggerIdentifier);
        assertEquals("Logger has wrong log level", logLevel, logger.getLogLevel());
    }

