        private long getSize(String url) throws ClientProtocolException, IOException {
            url = normalizeUrl(url);
            Log.i(LOG_underscoreTAG, "Head " + url);
            HttpHead httpGet = new HttpHead(url);
            HttpResponse response = mHttpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_underscoreOK) {
                throw new IOException("Unexpected Http status code " + response.getStatusLine().getStatusCode());
            }
            Header[] clHeaders = response.getHeaders("Content-Length");
            if (clHeaders.length > 0) {
                Header header = clHeaders[0];
                return Long.parseLong(header.getValue());
            }
            return -1;
        }

