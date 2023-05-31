    protected void downloadCacheFile(File file) throws Exception {
        ApplicationProperties app = ApplicationProperties.getInstance();
        String address = app.getProperty(JabberConstants.PROPERTY_underscoreJABBER_underscoreSERVERLIST, DEFAULT_underscoreSERVER_underscoreURL);
        URL url = new URL(address);
        file.createNewFile();
        OutputStream cache = new FileOutputStream(file);
        InputStream input = url.openStream();
        byte buffer[] = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) >= 0) cache.write(buffer, 0, bytesRead);
        input.close();
        cache.close();
    }

