    public TemplateLibrary loadTemplateLibrary(GadgetContext context, Uri uri) throws GadgetException {
        HttpRequest request = new HttpRequest(uri);
        request.setCacheTtl(300);
        HttpResponse response = pipeline.execute(request);
        if (response.getHttpStatusCode() != HttpResponse.SC_underscoreOK) {
            throw new GadgetException(GadgetException.Code.FAILED_underscoreTO_underscoreRETRIEVE_underscoreCONTENT, "Unable to retrieve template library xml. HTTP error " + response.getHttpStatusCode());
        }
        String content = response.getResponseAsString();
        try {
            String key = null;
            Element element = null;
            if (!context.getIgnoreCache()) {
                key = HashUtil.rawChecksum(content.getBytes());
                element = parsedXmlCache.getElement(key);
            }
            if (element == null) {
                element = XmlUtil.parse(content);
                if (key != null) {
                    parsedXmlCache.addElement(key, element);
                }
            }
            return new XmlTemplateLibrary(uri, element, content);
        } catch (XmlException e) {
            throw new GadgetException(GadgetException.Code.MALFORMED_underscoreXML_underscoreDOCUMENT, e);
        }
    }

