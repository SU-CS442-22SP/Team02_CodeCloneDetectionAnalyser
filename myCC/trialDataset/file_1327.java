    public void sendBinaryFile(String filename) throws IOException {
        Checker.checkEmpty(filename, "filename");
        URL url = _underscoregetFile(filename);
        OutputStream out = getOutputStream();
        Streams.copy(url.openStream(), out);
        out.close();
    }

