    public TestReport runImpl() throws Exception {
        DocumentFactory df = new SAXDocumentFactory(GenericDOMImplementation.getDOMImplementation(), parserClassName);
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
        e.setAttribute(targetAttribute, targetValue);
        if (targetValue.equals(e.getAttribute(targetAttribute))) {
            return reportSuccess();
        }
        DefaultTestReport report = new DefaultTestReport(this);
        report.setErrorCode(TestReport.ERROR_underscoreTEST_underscoreFAILED);
        report.setPassed(false);
        return report;
    }

