    protected InputStream openStreamInternal(String userAgent, Iterator mimeTypes, Iterator encodingTypes) throws IOException {
        if (stream != null) return stream;
        hasBeenOpened = true;
        URL url = null;
        try {
            url = buildURL();
        } catch (MalformedURLException mue) {
            throw new IOException("Unable to make sense of URL for connection");
        }
        if (url == null) return null;
        URLConnection urlC = url.openConnection();
        if (urlC instanceof HttpURLConnection) {
            if (userAgent != null) urlC.setRequestProperty(HTTP_underscoreUSER_underscoreAGENT_underscoreHEADER, userAgent);
            if (mimeTypes != null) {
                String acceptHeader = "";
                while (mimeTypes.hasNext()) {
                    acceptHeader += mimeTypes.next();
                    if (mimeTypes.hasNext()) acceptHeader += ",";
                }
                urlC.setRequestProperty(HTTP_underscoreACCEPT_underscoreHEADER, acceptHeader);
            }
            if (encodingTypes != null) {
                String encodingHeader = "";
                while (encodingTypes.hasNext()) {
                    encodingHeader += encodingTypes.next();
                    if (encodingTypes.hasNext()) encodingHeader += ",";
                }
                urlC.setRequestProperty(HTTP_underscoreACCEPT_underscoreENCODING_underscoreHEADER, encodingHeader);
            }
            contentType = urlC.getContentType();
            contentEncoding = urlC.getContentEncoding();
        }
        return (stream = urlC.getInputStream());
    }

