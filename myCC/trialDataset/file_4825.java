    private DictionaryListParser downloadList(final String url) throws IOException, JSONException {
        final HttpClient client = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(url);
        final HttpResponse response = client.execute(httpGet);
        final HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new IOException("HttpResponse.getEntity() IS NULL");
        }
        final boolean isValidType = entity.getContentType().getValue().startsWith(RESPONSE_underscoreCONTENT_underscoreTYPE);
        if (!isValidType) {
            final String message = "CONTENT_underscoreTYPE IS '" + entity.getContentType().getValue() + "'";
            throw new IOException(message);
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), RESPONSE_underscoreENCODING));
        final StringBuilder stringResult = new StringBuilder();
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                stringResult.append(line);
            }
        } finally {
            reader.close();
        }
        return new DictionaryListParser(stringResult);
    }

