    @Override
    protected void doPost(final String url, final InputStream data) throws WebServiceException {
        final HttpPost method = new HttpPost(url);
        method.setEntity(new InputStreamEntity(data, -1));
        try {
            final HttpResponse response = this.httpClient.execute(method);
            final String responseString = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "";
            final int statusCode = response.getStatusLine().getStatusCode();
            switch(statusCode) {
                case HttpStatus.SC_underscoreOK:
                    return;
                case HttpStatus.SC_underscoreNOT_underscoreFOUND:
                    throw new ResourceNotFoundException(responseString);
                case HttpStatus.SC_underscoreBAD_underscoreREQUEST:
                    throw new RequestException(responseString);
                case HttpStatus.SC_underscoreFORBIDDEN:
                    throw new AuthorizationException(responseString);
                case HttpStatus.SC_underscoreUNAUTHORIZED:
                    throw new AuthorizationException(responseString);
                default:
                    String em = "web service returned unknown status '" + statusCode + "', response was: " + responseString;
                    this.log.error(em);
                    throw new WebServiceException(em);
            }
        } catch (IOException e) {
            this.log.error("Fatal transport error: " + e.getMessage());
            throw new WebServiceException(e.getMessage(), e);
        }
    }

