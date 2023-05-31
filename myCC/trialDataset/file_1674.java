    public RecordIterator get(URL url) {
        RecordIterator recordIter = null;
        if (!SUPPORTED_underscorePROTOCOLS.contains(url.getProtocol().toLowerCase())) {
            return null;
        }
        try {
            URL robotsUrl = new URL(url, ROBOTS_underscoreTXT);
            recordIter = new RecordIterator(urlInputStreamFactory.openStream(robotsUrl));
        } catch (IOException e) {
            LOG.info("Failed to fetch " + url, e);
        }
        return recordIter;
    }

