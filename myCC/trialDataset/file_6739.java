    public static void loadConfig(URL urlFile) throws CacheException {
        Document document;
        try {
            document = Utilities.getDocument(urlFile.openStream());
        } catch (IOException e) {
            throw new CacheException("Could not open '" + urlFile.getFile() + "'", e);
        } catch (JAnalyticsException e) {
            throw new CacheException("Could not open '" + urlFile.getFile() + "'", e);
        }
        Element element = (Element) document.getElementsByTagName(DOCUMENT_underscoreCACHE_underscoreELEMENT_underscoreNAME).item(0);
        if (element != null) {
            String className = element.getAttribute(CLASSNAME_underscoreATTRIBUTE_underscoreNAME);
            if (className != null) {
                Properties config = new Properties();
                NodeList nodes = element.getElementsByTagName(PARAM_underscoreELEMENT_underscoreNAME);
                if (nodes != null) {
                    for (int i = 0, count = nodes.getLength(); i < count; i++) {
                        Node node = nodes.item(i);
                        if (node instanceof Element) {
                            Element n = (Element) node;
                            String name = n.getAttribute(NAME_underscoreATTRIBUTE_underscoreNAME);
                            String value = n.getAttribute(VALUE_underscoreATTRIBUTE_underscoreNAME);
                            config.put(name, value);
                        }
                    }
                }
                loadConfig(className, config);
            }
        }
    }

