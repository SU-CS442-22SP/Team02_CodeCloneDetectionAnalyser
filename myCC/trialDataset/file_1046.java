    private InputStream loadFromUrl(URL url, String contentType) throws IOException {
        Proxy proxy = null;
        if (isUseProxy) {
            Authenticator.setDefault(new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                }
            });
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        } else {
            proxy = Proxy.NO_underscorePROXY;
        }
        URLConnection connection = url.openConnection(proxy);
        connection.setRequestProperty("Accept-Charset", DEFAULT_underscoreCHARSET);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("User-Agent", USER_underscoreAGENT);
        InputStream response = connection.getInputStream();
        return response;
    }

