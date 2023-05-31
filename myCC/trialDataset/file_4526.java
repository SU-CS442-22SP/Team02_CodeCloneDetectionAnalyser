    public static Book GetReviewsForBook(String bookId, int page, int rating) throws Exception {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("www.goodreads.com");
        builder.path("book/show");
        builder.appendQueryParameter("key", _underscoreConsumerKey);
        builder.appendQueryParameter("page", Integer.toString(page));
        builder.appendQueryParameter("rating", Integer.toString(rating));
        builder.appendQueryParameter("id", bookId);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(builder.build().toString());
        if (get_underscoreIsAuthenticated()) {
            _underscoreConsumer.sign(getRequest);
        }
        HttpResponse response = httpClient.execute(getRequest);
        Response responseData = ResponseParser.parse(response.getEntity().getContent());
        return responseData.get_underscoreBook();
    }

