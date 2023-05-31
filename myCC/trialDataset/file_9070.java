    @Override
    public void checkConnection(byte[] options) throws Throwable {
        Properties opts = PropertiesUtils.deserializeProperties(options);
        String server = opts.getProperty(TRANSFER_underscoreOPTION_underscoreSERVER);
        String username = opts.getProperty(TRANSFER_underscoreOPTION_underscoreUSERNAME);
        String password = opts.getProperty(TRANSFER_underscoreOPTION_underscorePASSWORD);
        String filePath = opts.getProperty(TRANSFER_underscoreOPTION_underscoreFILEPATH);
        URL url = new URL(PROTOCOL_underscorePREFIX + username + ":" + password + "@" + server + filePath + ";type=i");
        URLConnection urlc = url.openConnection(BackEnd.getProxy(Proxy.Type.SOCKS));
        urlc.setConnectTimeout(Preferences.getInstance().preferredTimeOut * 1000);
        urlc.setReadTimeout(Preferences.getInstance().preferredTimeOut * 1000);
        urlc.connect();
    }

