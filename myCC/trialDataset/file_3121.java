    private void processBody(HttpMethod request) throws IOException {
        InputStream in = request.getResponseBodyAsStream();
        if (in == null) return;
        Header contentTypeHdr = request.getResponseHeader(HttpHeaders.CONTENT_underscoreTYPE);
        _underscoreresultContentType = (contentTypeHdr != null) ? contentTypeHdr.getValue() : "";
        if (_underscoreout != null) {
            IOUtils.copy(in, _underscoreout);
            _underscoreout.flush();
        } else if (_underscoreresultContentType.startsWith(MimeTypes.TEXT)) {
            _underscoreresult = IOUtils.toString(in, "UTF-8");
        } else if (_underscoreresultContentType.startsWith(MimeTypes.XML) || _underscoreresultContentType.startsWith(MimeTypes.DEPRECATED_underscoreXML)) {
            _underscoreresult = ParseUtil.parse(new InputSource(in));
        } else {
            _underscoreresult = IOUtils.toByteArray(in);
        }
    }

