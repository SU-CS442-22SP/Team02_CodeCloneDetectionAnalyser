    public void loadProfilefromConfig(String filename, P xslProfileClass, String profileTag) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        if (Val.chkStr(profileTag).equals("")) {
            profileTag = "Profile";
        }
        String configuration_underscorefolder_underscorepath = this.getConfigurationFolderPath();
        if (configuration_underscorefolder_underscorepath == null || configuration_underscorefolder_underscorepath.length() == 0) {
            Properties properties = new Properties();
            final URL url = CswProfiles.class.getResource("CswCommon.properties");
            properties.load(url.openStream());
            configuration_underscorefolder_underscorepath = properties.getProperty("DEFAULT_underscoreCONFIGURATION_underscoreFOLDER_underscorePATH");
        }
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        ResourcePath rscPath = new ResourcePath();
        InputSource configFile = rscPath.makeInputSource(configuration_underscorefolder_underscorepath + filename);
        if (configFile == null) {
            configFile = rscPath.makeInputSource("/" + configuration_underscorefolder_underscorepath + filename);
        }
        Document doc = builder.parse(configFile);
        NodeList profileNodes = doc.getElementsByTagName(profileTag);
        for (int i = 0; i < profileNodes.getLength(); i++) {
            Node currProfile = profileNodes.item(i);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String id = Val.chkStr(xpath.evaluate("ID", currProfile));
            String name = Val.chkStr(xpath.evaluate("Name", currProfile));
            String description = Val.chkStr(xpath.evaluate("Description", currProfile));
            String requestXslt = Val.chkStr(xpath.evaluate("GetRecords/XSLTransformations/Request", currProfile));
            String expectedGptXmlOutput = Val.chkStr(xpath.evaluate("GetRecords/XSLTransformations/Request/@expectedGptXmlOutput", currProfile));
            if (expectedGptXmlOutput.equals("")) {
                expectedGptXmlOutput = FORMAT_underscoreSEARCH_underscoreTO_underscoreXSL.MINIMAL_underscoreLEGACY_underscoreCSWCLIENT.toString();
            }
            String responseXslt = Val.chkStr(xpath.evaluate("GetRecords/XSLTransformations/Response", currProfile));
            String requestKVPs = Val.chkStr(xpath.evaluate("GetRecordByID/RequestKVPs", currProfile));
            String metadataXslt = Val.chkStr(xpath.evaluate("GetRecordByID/XSLTransformations/Response", currProfile));
            boolean extentSearch = Boolean.parseBoolean(Val.chkStr(xpath.evaluate("SupportSpatialQuery", currProfile)));
            boolean liveDataMaps = Boolean.parseBoolean(Val.chkStr(xpath.evaluate("SupportContentTypeQuery", currProfile)));
            boolean extentDisplay = Boolean.parseBoolean(Val.chkStr(xpath.evaluate("SupportSpatialBoundary", currProfile)));
            boolean harvestable = Boolean.parseBoolean(Val.chkStr(xpath.evaluate("Harvestable", currProfile)));
            requestXslt = configuration_underscorefolder_underscorepath + requestXslt;
            responseXslt = configuration_underscorefolder_underscorepath + responseXslt;
            metadataXslt = configuration_underscorefolder_underscorepath + metadataXslt;
            SearchXslProfile profile = null;
            try {
                profile = xslProfileClass.getClass().newInstance();
                profile.setId(id);
                profile.setName(name);
                profile.setDescription(description);
                profile.setRequestxslt(requestXslt);
                profile.setResponsexslt(responseXslt);
                profile.setMetadataxslt(metadataXslt);
                profile.setSupportsContentTypeQuery(liveDataMaps);
                profile.setSupportsSpatialBoundary(extentDisplay);
                profile.setSupportsSpatialQuery(extentSearch);
                profile.setKvp(requestKVPs);
                profile.setHarvestable(harvestable);
                profile.setFormatRequestToXsl(SearchXslProfile.FORMAT_underscoreSEARCH_underscoreTO_underscoreXSL.valueOf(expectedGptXmlOutput));
                profile.setFilter_underscoreextentsearch(extentSearch);
                profile.setFilter_underscorelivedatamap(liveDataMaps);
                addProfile((P) profile);
            } catch (InstantiationException e) {
                throw new IOException("Could not instantiate profile class" + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new IOException("Could not instantiate profile class" + e.getMessage());
            }
        }
    }

