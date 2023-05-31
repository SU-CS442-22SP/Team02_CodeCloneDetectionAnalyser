    @Override
    public byte[] readData(byte[] options, boolean transferMetaData) throws Throwable {
        long startTime = System.currentTimeMillis();
        long transferredBytesNum = 0;
        long elapsedTime = 0;
        Properties opts = PropertiesUtils.deserializeProperties(options);
        String server = opts.getProperty(TRANSFER_underscoreOPTION_underscoreSERVER);
        String username = opts.getProperty(TRANSFER_underscoreOPTION_underscoreUSERNAME);
        String password = opts.getProperty(TRANSFER_underscoreOPTION_underscorePASSWORD);
        String filePath = opts.getProperty(TRANSFER_underscoreOPTION_underscoreFILEPATH);
        if (transferMetaData) {
            int idx = filePath.lastIndexOf(PATH_underscoreSEPARATOR);
            if (idx != -1) {
                String fileName = filePath.substring(idx + 1) + META_underscoreDATA_underscoreFILE_underscoreSUFIX;
                filePath = filePath.substring(0, idx);
                filePath = filePath + PATH_underscoreSEPARATOR + fileName;
            } else {
                filePath += META_underscoreDATA_underscoreFILE_underscoreSUFIX;
            }
        }
        if (!server.contains(PORT_underscoreSEPARATOR)) {
            server += PORT_underscoreSEPARATOR + DEFAULT_underscorePORT;
        }
        URL url = new URL(PROTOCOL_underscorePREFIX + username + ":" + password + "@" + server + filePath + ";type=i");
        URLConnection urlc = url.openConnection(BackEnd.getProxy(Proxy.Type.SOCKS));
        urlc.setConnectTimeout(Preferences.getInstance().preferredTimeOut * 1000);
        urlc.setReadTimeout(Preferences.getInstance().preferredTimeOut * 1000);
        ByteArrayOutputStream baos = null;
        try {
            InputStream is = urlc.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int br;
            while ((br = is.read(buffer)) > 0) {
                baos.write(buffer, 0, br);
                if (!transferMetaData) {
                    transferredBytesNum += br;
                    elapsedTime = System.currentTimeMillis() - startTime;
                    fireOnProgressEvent(transferredBytesNum, elapsedTime);
                }
            }
            baos.close();
            is.close();
        } catch (FileNotFoundException fnfe) {
        }
        return baos != null ? baos.toByteArray() : null;
    }

