    @JiveScriptCommand(help = "Load and executes a JiveScript file")
    public void load(String path) throws JiveScriptException, IOException {
        loading = true;
        JivesScene.setActiveScene(null);
        boolean allowScripting = JiveScriptEngine.allowScripting;
        JiveScriptEngine.allowScripting = true;
        JiveScriptEngine.FILENAME = null;
        URL url = new URL(path);
        InputStream fis = url.openStream();
        if (fis == null) {
            throw new IOException("Unable to open file at path " + path);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        script = "";
        String line;
        while ((line = br.readLine()) != null) {
            script = script.concat(line) + Log.LINE_underscoreSEPARATOR;
        }
        eval(script);
        if (JiveScriptEngine.FILENAME == null) {
            String filename = path.substring(path.lastIndexOf(File.separatorChar) + 1);
            JiveScriptEngine.FILENAME = filename;
        }
        JiveScriptEngine.MD5 = DigestUtils.md5Hex(script.toString().getBytes());
        NetworkImplementorIntf networkImplementor = Jives.getNetwork();
        Object[] config = (Object[]) networkImplementor.getConnectionState(NetworkImplementorIntf.CONNECTIONSTATE_underscoreALL);
        boolean internet = (Boolean) config[NetworkImplementorIntf.CONNECTIONSTATE_underscoreINTERNET];
        if (internet) {
            echo("Starting network on the internet");
        } else {
            boolean ipv6 = (Boolean) config[NetworkImplementorIntf.CONNECTIONSTATE_underscoreIPV6];
            String connection = (String) config[NetworkImplementorIntf.CONNECTIONSTATE_underscoreRENDEZVOUS_underscoreIPV4] + ":" + (Integer) config[NetworkImplementorIntf.CONNECTIONSTATE_underscoreRENDEZVOUS_underscoreIPV4_underscorePORT];
            if (ipv6) {
                connection = (String) networkImplementor.getConnectionState(NetworkImplementorIntf.CONNECTIONSTATE_underscoreRENDEZVOUS_underscoreIPV6) + ":" + (Integer) networkImplementor.getConnectionState(NetworkImplementorIntf.CONNECTIONSTATE_underscoreRENDEZVOUS_underscoreIPV6_underscorePORT);
            }
            echo("Starting network on " + connection);
        }
        networkImplementor.startNetwork(JiveScriptEngine.FILENAME, JiveScriptEngine.MD5);
        fis.close();
        if (JiveScriptEngine.allowScripting && !allowScripting) {
            JiveScriptEngine.allowScripting = allowScripting;
        }
        loading = false;
    }

