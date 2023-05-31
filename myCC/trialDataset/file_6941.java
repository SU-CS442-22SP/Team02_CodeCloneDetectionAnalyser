    protected URLConnection openConnection(URL url) throws IOException {
        if (url == null) return null;
        if (!url.getProtocol().equals("nntp")) return null;
        if (m_underscoreconnection != null) {
            if (m_underscoreconnection.getURL().getHost().equals(url.getHost()) && (m_underscoreconnection.getURL().getPort() == url.getPort()) && (m_underscoreconnection.getURL().getUserInfo().equals(url.getUserInfo()))) {
                return m_underscoreconnection;
            }
        }
        m_underscoreconnection = new NNTPConnection(url);
        return m_underscoreconnection;
    }

