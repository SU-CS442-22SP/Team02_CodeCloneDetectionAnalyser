    @Override
    protected Metadata doGet(final String url) throws WebServiceException, MbXMLException {
        final HttpGet method = new HttpGet(url);
        this.log.debug(url);
        Metadata metadata = null;
        try {
            final HttpResponse response = this.httpClient.execute(method);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_underscoreOK == statusCode) {
                final InputStream responseStream = response.getEntity().getContent();
                metadata = this.getParser().parse(responseStream);
            } else {
                final String responseString = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "";
                switch(statusCode) {
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
            }
        } catch (IOException e) {
            this.log.error("Fatal transport error: " + e.getMessage());
            throw new WebServiceException(e.getMessage(), e);
        }
        return metadata;
    }

