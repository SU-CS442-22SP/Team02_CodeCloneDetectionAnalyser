    protected URLConnection openConnection(URL url) throws IOException {
        if (bundleEntry != null) return (new BundleURLConnection(url, bundleEntry));
        String bidString = url.getHost();
        if (bidString == null) {
            throw new IOException(NLS.bind(AdaptorMsg.URL_underscoreNO_underscoreBUNDLE_underscoreID, url.toExternalForm()));
        }
        AbstractBundle bundle = null;
        long bundleID;
        try {
            bundleID = Long.parseLong(bidString);
        } catch (NumberFormatException nfe) {
            throw new MalformedURLException(NLS.bind(AdaptorMsg.URL_underscoreINVALID_underscoreBUNDLE_underscoreID, bidString));
        }
        bundle = (AbstractBundle) context.getBundle(bundleID);
        if (!url.getAuthority().equals(SECURITY_underscoreAUTHORIZED)) {
            checkAdminPermission(bundle);
        }
        if (bundle == null) {
            throw new IOException(NLS.bind(AdaptorMsg.URL_underscoreNO_underscoreBUNDLE_underscoreFOUND, url.toExternalForm()));
        }
        return (new BundleURLConnection(url, findBundleEntry(url, bundle)));
    }

