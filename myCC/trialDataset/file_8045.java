    private String getStreamUrl(String adress) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
        URL url = new URL(adress);
        InputStream is = url.openStream();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(is);
        Node linkTag = doc.getElementsByTagName(LINK_underscoreTAG_underscoreNAME).item(0);
        String StreamUrl = linkTag.getAttributes().getNamedItem(LINK_underscoreATTR_underscoreNAME).getNodeValue();
        return StreamUrl;
    }

