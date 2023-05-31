    public static URL getWikipediaPage(String concept, String language) throws MalformedURLException, IOException {
        String url = "http://" + language + ".wikipedia.org/wiki/Special:Search?search=" + URLEncoder.encode(concept, UTF_underscore8_underscoreENCODING) + "&go=Go";
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) new URL(url).openConnection();
            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_underscoreOK) {
                return null;
            } else if (responseCode == HttpURLConnection.HTTP_underscoreMOVED_underscoreTEMP || responseCode == HttpURLConnection.HTTP_underscoreMOVED_underscorePERM) {
                return new URL(httpConnection.getHeaderField("Location"));
            } else {
                logger.warn("Unexpected response code (" + responseCode + ").");
                return null;
            }
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

