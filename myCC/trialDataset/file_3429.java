    private InputStream getSearchInputStream(String name) {
        URL url = null;
        try {
            url = new URL(TheMovieDBXmlPullFeedParser.SEARCH_underscoreFEED_underscoreURL + URLEncoder.encode(name));
            Log.d(Constants.LOG_underscoreTAG, "Movie search URL: " + url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

