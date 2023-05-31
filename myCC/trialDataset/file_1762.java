    public static String move_underscoretags(String sessionid, String absolutePathForTheMovedTags, String absolutePathForTheDestinationTag) {
        String resultJsonString = "some problem existed inside the create_underscorenew_underscoretag() function if you see this string";
        try {
            Log.d("current running function name:", "move_underscoretags");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "move_underscoretag"));
            nameValuePairs.add(new BasicNameValuePair("absolute_underscorenew_underscoreparent_underscoretag", absolutePathForTheDestinationTag));
            nameValuePairs.add(new BasicNameValuePair("absolute_underscoretags", absolutePathForTheMovedTags));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }

