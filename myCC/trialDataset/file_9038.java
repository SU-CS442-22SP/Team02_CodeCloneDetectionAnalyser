    private void _underscorereadValuesFromNetwork() {
        if (_underscoreintrinsicValuesByAttribute == null) {
            NSMutableDictionary<String, Object> values = new NSMutableDictionary<String, Object>(3);
            values.setObjectForKey(Boolean.FALSE, "NetworkFailure");
            try {
                URLConnection connection = url().openConnection();
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpconnect = (HttpURLConnection) connection;
                    httpconnect.setRequestMethod("HEAD");
                    switch(httpconnect.getResponseCode()) {
                        case HttpURLConnection.HTTP_underscoreOK:
                        case HttpURLConnection.HTTP_underscoreMOVED_underscorePERM:
                        case HttpURLConnection.HTTP_underscoreMOVED_underscoreTEMP:
                        case HttpURLConnection.HTTP_underscoreNOT_underscoreMODIFIED:
                            values.setObjectForKey(Boolean.TRUE, MD.FSExists);
                            break;
                        default:
                            values.setObjectForKey(Boolean.FALSE, MD.FSExists);
                    }
                    LOG.info("_underscorereadValuesFromNetwork: " + httpconnect.toString());
                    values.setObjectForKey(new NSTimestamp(httpconnect.getLastModified()), MD.FSContentChangeDate);
                    values.setObjectForKey(new Integer(httpconnect.getContentLength()), MD.FSSize);
                } else {
                    values.setObjectForKey(Boolean.FALSE, MD.FSExists);
                }
            } catch (Exception x) {
                values.setObjectForKey(Boolean.FALSE, MD.FSExists);
                values.setObjectForKey(Boolean.TRUE, "NetworkFailure");
            }
            _underscoreintrinsicValuesByAttribute = values;
        }
    }

