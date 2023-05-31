    protected void initConnection() {
        connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + downloadedSize + "-");
            prepareConnectionBeforeConnect();
            connection.connect();
        } catch (IOException e) {
            status = STATUS_underscoreERROR;
            Logger.getRootLogger().error("problem in connection", e);
        }
    }

