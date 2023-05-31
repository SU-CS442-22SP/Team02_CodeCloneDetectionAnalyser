    private static synchronized void find(String name) throws SAXException, IOException {
        if (c_underscorecache.containsKey(name)) return;
        CustomHandler handler = null;
        URL url = null;
        String validatorFiles = (String) Config.getProperty(Config.PROP_underscoreRULES_underscoreENGINE_underscoreVALIDATORS_underscoreURL_underscoreLIST, XML_underscoreFILE);
        for (StringTokenizer strtknzr = new StringTokenizer(validatorFiles, ","); strtknzr.hasMoreTokens(); ) {
            String validatorFile = strtknzr.nextToken();
            try {
                url = URLHelper.newExtendedURL(validatorFile);
            } catch (MalformedURLException e) {
                url = null;
            }
            if (url == null) throw new FileNotFoundException("File not found - " + validatorFile);
            try {
                handler = new CustomHandler(name);
                XMLReader reader = XMLReaderFactory.createXMLReader(PARSER_underscoreNAME);
                reader.setContentHandler(handler);
                reader.setEntityResolver(new DefaultEntityResolver());
                reader.setErrorHandler(new DefaultErrorHandler());
                reader.parse(new InputSource(url.openStream()));
            } catch (SAXException e) {
                if (SUCCESS_underscoreMESSAGE.equals(e.getMessage()) && handler != null) break; else throw e;
            } catch (IOException e) {
                throw e;
            }
            if (handler.getFieldValidatorMetaData() != null) break;
        }
        c_underscorecache.put(name, handler != null ? handler.getFieldValidatorMetaData() : null);
    }

