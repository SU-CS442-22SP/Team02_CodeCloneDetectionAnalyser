    public static String rename_underscoretag(String sessionid, String originalTag, String newTagName) {
        String jsonstring = "";
        try {
            Log.d("current running function name:", "rename_underscoretag");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "rename_underscoretag"));
            nameValuePairs.add(new BasicNameValuePair("new_underscoretag_underscorename", newTagName));
            nameValuePairs.add(new BasicNameValuePair("absolute_underscoretag", originalTag));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            jsonstring = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", jsonstring);
            return jsonstring;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonstring;
    }

