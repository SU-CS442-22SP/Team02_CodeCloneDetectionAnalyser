    @Override
    public InputStream getResourceByClassName(String className) {
        URL url = resourceFetcher.getResource("/fisce_underscorescripts/" + className + ".class");
        if (url == null) {
            return null;
        } else {
            try {
                return url.openStream();
            } catch (IOException e) {
                return null;
            }
        }
    }

