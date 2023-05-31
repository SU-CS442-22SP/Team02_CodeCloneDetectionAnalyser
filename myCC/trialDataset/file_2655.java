    public static String rename_underscorefile(String sessionid, String key, String newFileName) {
        String jsonstring = "";
        try {
            Log.d("current running function name:", "rename_underscorefile");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "rename_underscorefile"));
            nameValuePairs.add(new BasicNameValuePair("new_underscorename", newFileName));
            nameValuePairs.add(new BasicNameValuePair("key", key));
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

