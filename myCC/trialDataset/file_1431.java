    public TestReport runImpl() throws Exception {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parser);
        File f = (new File(testFileName));
        URL url = f.toURL();
        Document doc = df.createDocument(null, rootTag, url.toString(), url.openStream());
        Element e = doc.getElementById(targetId);
        if (e == null) {
            DefaultTestReport report = new DefaultTestReport(this);
            report.setErrorCode(ERROR_underscoreGET_underscoreELEMENT_underscoreBY_underscoreID_underscoreFAILED);
            report.addDescriptionEntry(ENTRY_underscoreKEY_underscoreID, targetId);
            report.setPassed(false);
            return report;
        }
        Document otherDocument = df.createDocument(null, rootTag, url.toString(), url.openStream());
        DocumentFragment docFrag = otherDocument.createDocumentFragment();
        try {
            docFrag.appendChild(doc.getDocumentElement());
        } catch (DOMException ex) {
            return reportSuccess();
        }
        DefaultTestReport report = new DefaultTestReport(this);
        report.setErrorCode(ERROR_underscoreEXCEPTION_underscoreNOT_underscoreTHROWN);
        report.setPassed(false);
        return report;
    }

