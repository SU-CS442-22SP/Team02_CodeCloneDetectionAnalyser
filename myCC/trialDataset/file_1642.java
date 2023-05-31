    public static String submitURLRequest(String url) throws HttpException, IOException, URISyntaxException {
        HttpClient httpclient = new DefaultHttpClient();
        InputStream stream = null;
        user_underscoreagents = new LinkedList<String>();
        user_underscoreagents.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        String response_underscoretext = "";
        URI uri = new URI(url);
        HttpGet post = new HttpGet(uri);
        int MAX = user_underscoreagents.size() - 1;
        int index = (int) Math.round(((double) Math.random() * (MAX)));
        String agent = user_underscoreagents.get(index);
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_underscoreAGENT, agent);
        httpclient.getParams().setParameter("User-Agent", agent);
        httpclient.getParams().setParameter(ClientPNames.COOKIE_underscorePOLICY, CookiePolicy.ACCEPT_underscoreNONE);
        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            stream = entity.getContent();
            response_underscoretext = convertStreamToString(stream);
        }
        httpclient.getConnectionManager().shutdown();
        if (stream != null) {
            stream.close();
        }
        return response_underscoretext;
    }

