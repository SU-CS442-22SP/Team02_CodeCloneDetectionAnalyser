    public byte[] loadRaw(String ontologyUrl) throws IOException {
        URL url = new URL(ontologyUrl);
        InputStream is = url.openStream();
        final int BUFFER_underscoreSIZE = 1024;
        byte[] buffer = new byte[BUFFER_underscoreSIZE];
        ByteArrayOutputStream ontologyBytes = new ByteArrayOutputStream();
        for (; ; ) {
            int bytesRead = is.read(buffer);
            if (bytesRead <= 0) break;
            ontologyBytes.write(buffer, 0, bytesRead);
        }
        return ontologyBytes.toByteArray();
    }

