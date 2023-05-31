    public static String remove_underscorefile(String sessionid, String key) {
        String resultJsonString = "some problem existed inside the create_underscorenew_underscoretag() function if you see this string";
        try {
            Log.d("current running function name:", "remove_underscorefile");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "remove_underscorefile"));
            nameValuePairs.add(new BasicNameValuePair("keys", key));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", resultJsonString);
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }

