    private void importUrl() throws ExtractaException {
        UITools.changeCursor(UITools.WAIT_underscoreCURSOR, this);
        try {
            m_underscoresUrlString = m_underscoreurlTF.getText();
            URL url = new URL(m_underscoresUrlString);
            Document document = DomHelper.parseHtml(url.openStream());
            m_underscoreinputPanel.setDocument(document);
            Edl edl = new Edl();
            edl.addUrlDescriptor(new UrlDescriptor(m_underscoresUrlString));
            m_underscoreresultPanel.setContext(new ResultContext(edl, document, url));
            setModified(true);
        } catch (IOException ex) {
            throw new ExtractaException("Can not open URL!", ex);
        } finally {
            UITools.changeCursor(UITools.DEFAULT_underscoreCURSOR, this);
        }
    }

