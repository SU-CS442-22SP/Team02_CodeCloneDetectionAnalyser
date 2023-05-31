    private static void _underscorereadAllRegionMDFiles(ClassLoader loader, RegionMetadata bean, String regionMDFile) {
        if (_underscoreLOG.isFinest()) {
            _underscoreLOG.finest("searching for region-metadata with resource:{0}", regionMDFile);
        }
        try {
            Enumeration<URL> files = loader.getResources(regionMDFile);
            while (files.hasMoreElements()) {
                URL url = files.nextElement();
                String publicId = url.toString();
                try {
                    InputStream in = url.openStream();
                    _underscorereadRegionMetadata(bean, in, publicId);
                    in.close();
                } catch (IOException e) {
                    _underscoreerror(publicId, e);
                }
            }
        } catch (IOException e) {
            _underscoreLOG.warning("ERR_underscoreGET_underscoreREGION_underscoreMETADATA_underscoreFILE", _underscore_underscoreCONFIG_underscoreFILE_underscoreOTHER);
            _underscoreLOG.warning(e);
        }
    }

