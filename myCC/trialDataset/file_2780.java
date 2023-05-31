    private void sendFile(File file, HttpExchange response) throws IOException {
        response.getResponseHeaders().add(FileUploadBase.CONTENT_underscoreLENGTH, Long.toString(file.length()));
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            IOUtils.copy(inputStream, response.getResponseBody());
        } catch (Exception exception) {
            throw new IOException("error sending file", exception);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

