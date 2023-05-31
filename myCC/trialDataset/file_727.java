    private synchronized Document executeHttpMethod(final HttpUriRequest httpRequest) throws UnauthorizedException, ThrottledException, ApiException {
        if (!isNextRequestAllowed()) {
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Wait " + WAITING_underscoreTIME + "ms for request.");
                }
                wait(WAITING_underscoreTIME);
            } catch (InterruptedException ie) {
                throw new ApiException("Waiting for request interrupted.", ie);
            }
        }
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Perform request.");
            }
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            switch(httpResponse.getStatusLine().getStatusCode()) {
                case HTTP_underscoreOK:
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null) {
                        InputStream responseStream = httpEntity.getContent();
                        if (responseStream == null) {
                            throw new ApiException("TODO");
                        } else {
                            String response = null;
                            try {
                                response = IOUtils.toString(responseStream, RESPONSE_underscoreENCODING);
                            } catch (IOException ioe) {
                                throw new ApiException("Problem reading response", ioe);
                            } catch (RuntimeException re) {
                                httpRequest.abort();
                                throw new ApiException("Problem reading response", re);
                            } finally {
                                responseStream.close();
                            }
                            StringReader responseReader = new StringReader(response);
                            Document document = documentBuilder.parse(new InputSource(responseReader));
                            return document;
                        }
                    }
                case HTTP_underscoreUNAVAILABLE:
                    throw new ThrottledException("TODO");
                case HTTP_underscoreUNAUTHORIZED:
                    throw new UnauthorizedException("TODO");
                default:
                    throw new ApiException("Unexpected HTTP status code: " + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (SAXException se) {
            throw new ApiException("TODO", se);
        } catch (IOException ioe) {
            throw new ApiException("TODO", ioe);
        } finally {
            updateLastRequestTimestamp();
        }
    }

