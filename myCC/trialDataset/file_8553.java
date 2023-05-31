    private BufferedImage _underscoregetImage(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        InputStream in = null;
        try {
            in = conn.getInputStream();
            return ImageIO.read(in);
        } finally {
            IOUtilities.close(in);
        }
    }

