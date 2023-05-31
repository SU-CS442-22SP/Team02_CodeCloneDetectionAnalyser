    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpGet request = new HttpGet(UPDATE_underscoreURL);
            request.setHeader("Accept", "text/plain");
            HttpResponse response = MyMovies.getHttpClient().execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_underscoreOK) {
                return "Error: Failed getting update notes";
            }
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

