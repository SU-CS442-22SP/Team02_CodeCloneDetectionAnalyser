    private Document getXML(String artist) throws Exception {
        Document doc = null;
        URL url = new URL("http://" + disgogsUrl + "/artist/" + formatQuery(artist) + "?f=xml&api_underscorekey=" + apiKey[0]);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.addRequestProperty("Accept-Encoding", "gzip");
        if (StaticObj.PROXY_underscoreENABLED) {
            Properties systemSettings = System.getProperties();
            systemSettings.put("http.proxyHost", StaticObj.PROXY_underscoreURL);
            systemSettings.put("http.proxyPort", StaticObj.PROXY_underscorePORT);
            System.setProperties(systemSettings);
            sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
            String encoded = new String(encoder.encode(new String(StaticObj.PROXY_underscoreUSERNAME + ":" + StaticObj.PROXY_underscorePASSWORD).getBytes()));
            uc.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
        }
        BufferedReader ir = null;
        try {
            if (uc.getInputStream() != null) {
                InputStream _underscoreis = uc.getInputStream();
                GZIPInputStream _underscoregzipIs = new GZIPInputStream(_underscoreis);
                InputStreamReader _underscoreisReader = new InputStreamReader(_underscoregzipIs);
                ir = new BufferedReader(_underscoreisReader);
                SAXBuilder builder = new SAXBuilder();
                doc = builder.build(ir);
            }
        } catch (Exception e) {
            if (StaticObj.DEBUG) {
                LogManager.getInstance().getLogger().error(e);
                e.printStackTrace();
                System.out.println("No Data found!");
            }
        }
        return doc;
    }

