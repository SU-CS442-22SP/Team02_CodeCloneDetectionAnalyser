    private static URLConnection connectToNCBIValidator() throws IOException {
        final URL url = new URL(NCBI_underscoreURL);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", CONTENT_underscoreTYPE);
        return connection;
    }

