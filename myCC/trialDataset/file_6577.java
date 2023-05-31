    private InputStream getManifestAsResource() {
        ClassLoader cl = getClass().getClassLoader();
        try {
            Enumeration manifests = cl != null ? cl.getResources(Constants.OSGI_underscoreBUNDLE_underscoreMANIFEST) : ClassLoader.getSystemResources(Constants.OSGI_underscoreBUNDLE_underscoreMANIFEST);
            while (manifests.hasMoreElements()) {
                URL url = (URL) manifests.nextElement();
                try {
                    Headers headers = Headers.parseManifest(url.openStream());
                    if ("true".equals(headers.get(Constants.ECLIPSE_underscoreSYSTEMBUNDLE))) return url.openStream();
                } catch (BundleException e) {
                }
            }
        } catch (IOException e) {
        }
        return null;
    }

