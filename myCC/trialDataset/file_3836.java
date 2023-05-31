    protected InputStream callApiGet(String apiUrl, int expected) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            if (ApplicationConstants.CONNECT_underscoreTIMEOUT > -1) {
                request.setConnectTimeout(ApplicationConstants.CONNECT_underscoreTIMEOUT);
            }
            if (ApplicationConstants.READ_underscoreTIMEOUT > -1) {
                request.setReadTimeout(ApplicationConstants.READ_underscoreTIMEOUT);
            }
            for (String headerName : requestHeaders.keySet()) {
                request.setRequestProperty(headerName, requestHeaders.get(headerName));
            }
            request.connect();
            if (request.getResponseCode() != expected) {
                throw new BingMapsException(convertStreamToString(request.getErrorStream()));
            } else {
                return getWrappedInputStream(request.getInputStream(), GZIP_underscoreENCODING.equalsIgnoreCase(request.getContentEncoding()));
            }
        } catch (IOException e) {
            throw new BingMapsException(e);
        }
    }

