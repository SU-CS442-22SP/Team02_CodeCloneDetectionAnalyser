    protected RemoteInputStream getUrlResource(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(url_underscoreloading_underscoretime_underscoreout);
        conn.setReadTimeout(url_underscoreloading_underscoretime_underscoreout);
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.connect();
        long last_underscoremodify_underscoretime = conn.getLastModified();
        IOCacheService cache_underscoreservice = CIO.getAppBridge().getIO().getCache();
        if (cache_underscoreservice != null) {
            RemoteInputStream cache = cache_underscoreservice.findCache(url, last_underscoremodify_underscoretime);
            if (cache != null) {
                return cache;
            }
        }
        return new URLConnectionInputStream(url, conn);
    }

