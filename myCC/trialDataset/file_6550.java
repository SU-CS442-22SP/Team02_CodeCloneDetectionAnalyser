    private void loadFile(File file) throws Exception {
        Edl edl = new Edl("file:///" + file.getAbsolutePath());
        URL url = ExtractaHelper.retrieveUrl(edl.getUrlRetrievalDescriptor());
        String sUrlString = url.toExternalForm();
        if (sUrlString.startsWith("file:/") && (sUrlString.charAt(6) != '/')) {
            sUrlString = sUrlString.substring(0, 6) + "//" + sUrlString.substring(6);
        }
        Document document = DomHelper.parseHtml(url.openStream());
        m_underscoreinputPanel.setDocument(document);
        m_underscoreresultPanel.setContext(new ResultContext(edl, document, url));
        initNameCounters(edl.getItemDescriptors());
        m_underscoreoutputFile = file;
        m_underscoresUrlString = sUrlString;
        m_underscoreurlTF.setText(m_underscoresUrlString);
        updateHistroy(m_underscoreoutputFile);
        setModified(false);
    }

