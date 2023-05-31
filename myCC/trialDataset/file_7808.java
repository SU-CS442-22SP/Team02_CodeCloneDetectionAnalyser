    private static String doGetForSessionKey(String authCode) throws Exception {
        String sessionKey = "";
        HttpClient hc = new DefaultHttpClient();
        HttpGet hg = new HttpGet(Common.TEST_underscoreSESSION_underscoreHOST + Common.TEST_underscoreSESSION_underscorePARAM + authCode);
        HttpResponse hr = hc.execute(hg);
        BufferedReader br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String result = sb.toString();
        Log.i("sessionKeyMessages", result);
        Map<String, String> map = Util.handleURLParameters(result);
        sessionKey = map.get(Common.TOP_underscoreSESSION);
        String topParameters = map.get(Common.TOP_underscorePARAMETERS);
        String decTopParameters = Util.decodeBase64(topParameters);
        Log.i("base64", decTopParameters);
        map = Util.handleURLParameters(decTopParameters);
        Log.i("nick", map.get(Common.VISITOR_underscoreNICK));
        CachePool.put(Common.VISITOR_underscoreNICK, map.get(Common.VISITOR_underscoreNICK));
        return sessionKey;
    }

