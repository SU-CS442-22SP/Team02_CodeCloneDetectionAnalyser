    public static void main(String[] args) throws IOException {
        httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_underscoreVERSION, HttpVersion.HTTP_underscore1_underscore1);
        loginLocalhostr();
        initialize();
        HttpOptions httpoptions = new HttpOptions(localhostrurl);
        HttpResponse myresponse = httpclient.execute(httpoptions);
        HttpEntity myresEntity = myresponse.getEntity();
        System.out.println(EntityUtils.toString(myresEntity));
        fileUpload();
    }

