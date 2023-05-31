    public void load(URL url) throws IOException {
        try {
            oggBitStream_underscore = new BufferedInputStream(url.openStream());
        } catch (Exception ex) {
            System.err.println("ogg file " + url + " could not be loaded");
        }
        load();
    }

