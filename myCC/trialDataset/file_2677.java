    public InputStream openAsStream() throws IOException {
        ClassLoader cl = _underscorepreferredClassLoader;
        if (cl == null) {
            cl = Thread.currentThread().getContextClassLoader();
        }
        final URL url = (cl == null) ? null : cl.getResource(_underscorepath);
        return (url == null) ? null : url.openStream();
    }

