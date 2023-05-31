    private URLConnection openGetConnection(StringBuffer sb) throws IOException, IOException, MalformedURLException {
        URL url = new URL(m_underscoregatewayAddress + "?" + sb.toString());
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection;
    }

