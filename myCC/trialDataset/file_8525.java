    public boolean connect() {
        if (connectStatus > -1) return (connectStatus == 1);
        connectStatus = 0;
        try {
            URL url = new URL(getURL());
            m_underscoreconnection = (HttpURLConnection) url.openConnection();
            m_underscoreconnection.connect();
            processHeaders();
            m_underscoreinputStream = m_underscoreconnection.getInputStream();
        } catch (MalformedURLException e) {
            newError("connect failed", e, true);
        } catch (IOException e) {
            newError("connect failed", e, true);
        }
        return (connectStatus == 1);
    }

