    protected void initializeFromURL(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        String message = this.validateURLConnection(connection, DBASE_underscoreCONTENT_underscoreTYPES);
        if (message != null) {
            throw new IOException(message);
        }
        this.channel = Channels.newChannel(WWIO.getBufferedInputStream(connection.getInputStream()));
        this.initialize();
    }

