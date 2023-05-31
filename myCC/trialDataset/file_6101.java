    public static Dictionary loadManifestFrom(BaseData bundledata) throws BundleException {
        URL url = bundledata.getEntry(Constants.OSGI_underscoreBUNDLE_underscoreMANIFEST);
        if (url == null) return null;
        try {
            return Headers.parseManifest(url.openStream());
        } catch (IOException e) {
            throw new BundleException(NLS.bind(EclipseAdaptorMsg.ECLIPSE_underscoreDATA_underscoreERROR_underscoreREADING_underscoreMANIFEST, bundledata.getLocation()), e);
        }
    }

