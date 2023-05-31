    public static void init() {
        if (init_underscore) return;
        init_underscore = true;
        URLStreamHandler h = new URLStreamHandler() {

            protected URLConnection openConnection(URL _underscoreurl) throws IOException {
                return new Connection(_underscoreurl);
            }
        };
        FuLib.setUrlHandler("data", h);
    }

