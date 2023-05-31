    public static void home4www(String location) throws NetworkException {
        HttpClient client = HttpUtil.newInstance();
        HttpGet get = new HttpGet(HttpUtil.KAIXIN_underscoreWWW_underscoreURL + location);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            HTTPUtil.consume(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }

