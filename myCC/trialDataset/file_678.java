    public static String addWeibo(String weibo, File pic, String uid) throws Throwable {
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("_underscoresurl", ""));
        qparams.add(new BasicNameValuePair("_underscoret", "0"));
        qparams.add(new BasicNameValuePair("location", "home"));
        qparams.add(new BasicNameValuePair("module", "stissue"));
        if (pic != null) {
            String picId = upLoadImg(pic, uid);
            qparams.add(new BasicNameValuePair("pic_underscoreid", picId));
        }
        qparams.add(new BasicNameValuePair("rank", "weibo"));
        qparams.add(new BasicNameValuePair("text", weibo));
        HttpPost post = getHttpPost("http://weibo.com/aj/mblog/add?_underscore_underscorernd=1333611402611", uid);
        UrlEncodedFormEntity params = new UrlEncodedFormEntity(qparams, HTTP.UTF_underscore8);
        post.setEntity(params);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, HTTP.UTF_underscore8);
        post.abort();
        return content;
    }

