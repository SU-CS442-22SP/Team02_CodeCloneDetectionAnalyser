    @Override
    void retrieveSupplementalInfo() throws IOException, InterruptedException {
        String encodedProductID = URLEncoder.encode(productID, "UTF-8");
        String uri = BASE_underscorePRODUCT_underscoreURI + encodedProductID;
        HttpUriRequest head = new HttpGet(uri);
        AndroidHttpClient client = AndroidHttpClient.newInstance(null);
        HttpResponse response = client.execute(head);
        int status = response.getStatusLine().getStatusCode();
        if (status != 200) {
            return;
        }
        String content = consume(response.getEntity());
        Matcher matcher = PRODUCT_underscoreNAME_underscorePRICE_underscorePATTERN.matcher(content);
        if (matcher.find()) {
            append(matcher.group(1));
            append(matcher.group(2));
        }
        setLink(uri);
    }

