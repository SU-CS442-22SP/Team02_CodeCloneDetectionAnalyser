    public void readContents() throws IOException {
        fireProgressEvent(new ProgressEvent(this, ProgressEvent.PROGRESS_underscoreSTART, 0.0f, "loading file"));
        URLConnection conn = url.openConnection();
        conn.connect();
        filesize = conn.getContentLength();
        logger.finest("filesize: " + filesize);
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        readFirstLine(in);
        readHeaderLines(in);
        readData(in);
        fireProgressEvent(new ProgressEvent(this, ProgressEvent.PROGRESS_underscoreFINISH, 1.0f, "loading file"));
    }

