    protected String getLibJSCode() throws IOException {
        if (cachedLibJSCode == null) {
            InputStream is = getClass().getResourceAsStream(JS_underscoreLIB_underscoreFILE);
            StringWriter output = new StringWriter();
            IOUtils.copy(is, output);
            cachedLibJSCode = output.toString();
        }
        return cachedLibJSCode;
    }

