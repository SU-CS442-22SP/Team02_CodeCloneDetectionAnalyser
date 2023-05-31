    protected void init() throws MXQueryException {
        String add = getStringValueOrEmpty(subIters[0]);
        if (add == null) {
            currentToken = BooleanToken.FALSE_underscoreTOKEN;
            return;
        }
        URI uri;
        if (!TypeLexicalConstraints.isValidURI(add)) throw new DynamicException(ErrorCodes.F0017_underscoreINVALID_underscoreARGUMENT_underscoreTO_underscoreFN_underscoreDOC, "Invalid URI given to fn:doc-available", loc);
        try {
            if (TypeLexicalConstraints.isAbsoluteURI(add)) {
                uri = new URI(add);
            } else {
                uri = new URI(IOLib.convertToAndroid(add));
            }
        } catch (URISyntaxException se) {
            throw new DynamicException(ErrorCodes.F0017_underscoreINVALID_underscoreARGUMENT_underscoreTO_underscoreFN_underscoreDOC, "Invalid URI given to fn:doc-available", loc);
        }
        if (add.startsWith("http://")) {
            URL url;
            try {
                url = uri.toURL();
            } catch (MalformedURLException e) {
                throw new DynamicException(ErrorCodes.F0017_underscoreINVALID_underscoreARGUMENT_underscoreTO_underscoreFN_underscoreDOC, "Invalid URI given to fn:doc-available", loc);
            }
            try {
                InputStream in = url.openStream();
                in.close();
            } catch (IOException e) {
                currentToken = BooleanToken.FALSE_underscoreTOKEN;
                return;
            }
            currentToken = BooleanToken.TRUE_underscoreTOKEN;
        } else {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(MXQuery.getContext().openFileInput(uri.toString())));
                currentToken = BooleanToken.TRUE_underscoreTOKEN;
            } catch (FileNotFoundException e) {
                currentToken = BooleanToken.FALSE_underscoreTOKEN;
            } catch (IOException e) {
                currentToken = BooleanToken.FALSE_underscoreTOKEN;
            }
        }
    }

