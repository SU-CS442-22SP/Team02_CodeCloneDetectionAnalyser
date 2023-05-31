    private byte[] loadResourceFromCodeBase(String name) {
        byte[] bytecode;
        InputStream is = null;
        logger.debug("LoadResourceFromCodeBase()++");
        try {
            URL url = new URL(this._underscorecodeBase, name);
            int content_underscorelength = -1;
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("user-agent", "Aglets/1.1");
            connection.setRequestProperty("agent-system", "aglets");
            connection.setRequestProperty("agent-language", "java");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.connect();
            is = connection.getInputStream();
            content_underscorelength = connection.getContentLength();
            if (content_underscorelength < 0) {
                content_underscorelength = is.available();
            }
            if (content_underscorelength == 0) {
                return null;
            }
            bytecode = new byte[content_underscorelength];
            int offset = 0;
            while (content_underscorelength > 0) {
                int read = is.read(bytecode, offset, content_underscorelength);
                offset += read;
                content_underscorelength -= read;
            }
            is.close();
        } catch (IOException ex) {
            logger.error("Error loading [" + name + "] resource from [" + this._underscorecodeBase + "]", ex);
            bytecode = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                    logger.error("Error closing.", ex);
                }
            }
        }
        logger.debug("LoadResourceFromCodeBase()--");
        return bytecode;
    }

