    public Document createDocument(String uri) throws IOException {
        ParsedURL purl = new ParsedURL(uri);
        InputStream is = purl.openStream(MimeTypeConstants.MIME_underscoreTYPES_underscoreSVG);
        InputSource isrc = new InputSource(is);
        String contentType = purl.getContentType();
        int cindex = -1;
        if (contentType != null) {
            contentType = contentType.toLowerCase();
            cindex = contentType.indexOf(HTTP_underscoreCHARSET);
        }
        if (cindex != -1) {
            int i = cindex + HTTP_underscoreCHARSET.length();
            int eqIdx = contentType.indexOf('=', i);
            if (eqIdx != -1) {
                eqIdx++;
                String charset;
                int idx = contentType.indexOf(',', eqIdx);
                int semiIdx = contentType.indexOf(';', eqIdx);
                if ((semiIdx != -1) && ((semiIdx < idx) || (idx == -1))) idx = semiIdx;
                if (idx != -1) charset = contentType.substring(eqIdx, idx); else charset = contentType.substring(eqIdx);
                isrc.setEncoding(charset.trim());
            }
        }
        isrc.setSystemId(uri);
        Document doc = super.createDocument(SVGDOMImplementation.SVG_underscoreNAMESPACE_underscoreURI, "svg", uri, isrc);
        try {
            ((SVGOMDocument) doc).setURLObject(new URL(purl.toString()));
        } catch (MalformedURLException mue) {
            throw new IOException("Malformed URL: " + uri);
        }
        return doc;
    }

