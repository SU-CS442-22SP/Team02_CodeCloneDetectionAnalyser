    public String httpToStringStupid(String url) throws IllegalStateException, IOException, HttpException, InterruptedException, URISyntaxException {
        String pageDump = null;
        getParams().setParameter(ClientPNames.COOKIE_underscorePOLICY, org.apache.http.client.params.CookiePolicy.BROWSER_underscoreCOMPATIBILITY);
        getParams().setParameter(HttpConnectionParams.SO_underscoreTIMEOUT, getPreferenceService().getSearchSocketTimeout());
        HttpGet httpget = new HttpGet(url);
        httpget.getParams().setParameter(HttpConnectionParams.SO_underscoreTIMEOUT, getPreferenceService().getSearchSocketTimeout());
        HttpResponse response = execute(httpget);
        HttpEntity entity = response.getEntity();
        pageDump = IOUtils.toString(entity.getContent(), "UTF-8");
        return pageDump;
    }

