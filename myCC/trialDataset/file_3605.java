    public void init(final javax.swing.text.Document doc) {
        this.doc = doc;
        String dtdLocation = null;
        String schemaLocation = null;
        SyntaxDocument mDoc = (SyntaxDocument) doc;
        Object mDtd = mDoc.getProperty(XPontusConstantsIF.PARSER_underscoreDATA_underscoreDTD_underscoreCOMPLETION_underscoreINFO);
        Object mXsd = mDoc.getProperty(XPontusConstantsIF.PARSER_underscoreDATA_underscoreSCHEMA_underscoreCOMPLETION_underscoreINFO);
        if (mDtd != null) {
            dtdLocation = mDtd.toString();
        }
        if (mXsd != null) {
            schemaLocation = mXsd.toString();
        }
        Object o = doc.getProperty("BUILTIN_underscoreCOMPLETION");
        if (o != null) {
            if (o.equals("HTML")) {
                dtdLocation = getClass().getResource("xhtml.dtd").toExternalForm();
            }
        }
        try {
            if (dtdLocation != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Using dtd to build completion database");
                }
                setCompletionParser(new DTDCompletionParser());
                URL url = new java.net.URL(dtdLocation);
                Reader dtdReader = new InputStreamReader(url.openStream());
                updateAssistInfo(null, dtdLocation, dtdReader);
            } else if (schemaLocation != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Using schema  to build completion database");
                }
                setCompletionParser(new XSDCompletionParser());
                URL url = new java.net.URL(schemaLocation);
                Reader dtdReader = new InputStreamReader(url.openStream());
                updateAssistInfo(null, schemaLocation, dtdReader);
            }
        } catch (Exception err) {
            if (logger.isDebugEnabled()) {
                logger.debug(err.getMessage(), err);
            }
        }
    }

