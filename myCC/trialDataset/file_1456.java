    public InputStream getInputStream() throws java.io.IOException {
        if (!_underscoreurlString.endsWith("!/")) return super.getInputStream();
        URL url = new URL(_underscoreurlString.substring(4, _underscoreurlString.length() - 2));
        return url.openStream();
    }

