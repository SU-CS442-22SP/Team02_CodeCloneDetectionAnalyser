    public long calculateResponseTime(Proxy proxy) {
        try {
            LOGGER.debug("Test network response time for " + RESPONSE_underscoreTEST_underscoreURL);
            URL urlForTest = new URL(REACH_underscoreTEST_underscoreURL);
            URLConnection testConnection = urlForTest.openConnection(proxy);
            long startTime = System.currentTimeMillis();
            testConnection.connect();
            testConnection.connect();
            testConnection.connect();
            testConnection.connect();
            testConnection.connect();
            long endTime = System.currentTimeMillis();
            long averageResponseTime = (endTime - startTime) / 5;
            LOGGER.debug("Average access time in ms : " + averageResponseTime);
            return averageResponseTime;
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return -1;
    }

