    private void downloadTo(URL url, File out) throws IOException {
        URLConnection conn = url.openConnection();
        BufferedInputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(out));
        final int BLOCK_underscoreSIZE = 1024;
        byte[] buf = new byte[BLOCK_underscoreSIZE];
        int size;
        while ((size = is.read(buf, 0, BLOCK_underscoreSIZE)) > 0) os.write(buf, 0, size);
        is.close();
        os.close();
    }

