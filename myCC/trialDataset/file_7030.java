    public static Image getPluginImage(Object plugin, String name) {
        try {
            try {
                URL url = getPluginImageURL(plugin, name);
                if (m_underscoreURLImageMap.containsKey(url)) return m_underscoreURLImageMap.get(url);
                InputStream is = url.openStream();
                Image image;
                try {
                    image = getImage(is);
                    m_underscoreURLImageMap.put(url, image);
                } finally {
                    is.close();
                }
                return image;
            } catch (Throwable e) {
            }
        } catch (Throwable e) {
        }
        return null;
    }

