    public HttpEntity execute(final HttpRequestBase request) throws IOException, ClientProtocolException {
        final HttpResponse response = mClient.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_underscoreOK | statusCode == HttpStatus.SC_underscoreCREATED) {
            return response.getEntity();
        }
        return null;
    }

