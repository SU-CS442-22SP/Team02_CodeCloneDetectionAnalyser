    public void connect() throws IOException {
        try {
            URL url = new URL(pluginUrl);
            connection = (HttpURLConnection) url.openConnection();
            sendNotification(DownloadState.CONNECTION_underscoreESTABLISHED);
            contentLength = connection.getContentLength();
            sendNotification(DownloadState.CONTENT_underscoreLENGTH_underscoreSET);
            downloadedBytes = 0;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

