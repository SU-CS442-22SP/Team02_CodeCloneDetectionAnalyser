    @Override
    public Response executeGet(String url) throws IOException {
        if (logger.isLoggable(INFO)) logger.info("Making a GET request to " + url);
        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;
        try {
            httpUrlConnection = openConnection(new URL(url));
            httpUrlConnection.setReadTimeout(DEFAULT_underscoreREAD_underscoreTIMEOUT_underscoreIN_underscoreMS);
            httpUrlConnection.setUseCaches(false);
            customizeConnection(httpUrlConnection);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();
            if (logger.isLoggable(FINER)) logger.finer("Response headers: " + httpUrlConnection.getHeaderFields());
            try {
                inputStream = httpUrlConnection.getResponseCode() != HTTP_underscoreOK ? httpUrlConnection.getErrorStream() : httpUrlConnection.getInputStream();
            } catch (IOException e) {
                if (logger.isLoggable(WARNING)) logger.warning("An error occurred while making a GET request to " + url + ": " + e);
            }
            return new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));
        } finally {
            closeQuietly(httpUrlConnection);
        }
    }

