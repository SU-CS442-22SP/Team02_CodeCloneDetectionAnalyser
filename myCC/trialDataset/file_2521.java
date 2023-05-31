    private URLConnection getServletConnection(String strServlet_underscorename) throws MalformedURLException, IOException {
        URL urlServlet = null;
        if (strServlet_underscorename == null) {
            urlServlet = m_underscoreUrl;
        } else {
            urlServlet = new URL(m_underscoreUrl, strServlet_underscorename);
        }
        URLConnection connection = urlServlet.openConnection();
        connection.setConnectTimeout(180000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
        if (m_underscorestrJsessionid != null) {
            connection.setRequestProperty("Cookie", m_underscorestrJsessionid);
        }
        return connection;
    }

