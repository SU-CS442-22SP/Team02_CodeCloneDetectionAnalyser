        private InputStream get(String url, long startOffset, long expectedLength) throws ClientProtocolException, IOException {
            url = normalizeUrl(url);
            Log.i(LOG_underscoreTAG, "Get " + url);
            mHttpGet = new HttpGet(url);
            int expectedStatusCode = HttpStatus.SC_underscoreOK;
            if (startOffset > 0) {
                String range = "bytes=" + startOffset + "-";
                if (expectedLength >= 0) {
                    range += expectedLength - 1;
                }
                Log.i(LOG_underscoreTAG, "requesting byte range " + range);
                mHttpGet.addHeader("Range", range);
                expectedStatusCode = HttpStatus.SC_underscorePARTIAL_underscoreCONTENT;
            }
            HttpResponse response = mHttpClient.execute(mHttpGet);
            long bytesToSkip = 0;
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != expectedStatusCode) {
                if ((statusCode == HttpStatus.SC_underscoreOK) && (expectedStatusCode == HttpStatus.SC_underscorePARTIAL_underscoreCONTENT)) {
                    Log.i(LOG_underscoreTAG, "Byte range request ignored");
                    bytesToSkip = startOffset;
                } else {
                    throw new IOException("Unexpected Http status code " + statusCode + " expected " + expectedStatusCode);
                }
            }
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            if (bytesToSkip > 0) {
                is.skip(bytesToSkip);
            }
            return is;
        }

