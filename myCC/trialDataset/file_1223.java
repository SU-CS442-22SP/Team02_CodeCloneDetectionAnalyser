    public HttpURLConnection getURLConnection() throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) _underscoreurl.openConnection();
            conn.setUseCaches(false);
            conn.setRequestProperty("User-Agent", "WD-2.0");
            if (_underscoredoConditionalGET) {
                ResourceInfo ri = (ResourceInfo) conditionalGetTable().get(_underscoreurl.toString());
                if (ri != null) {
                    if (ri.lastModified != null) {
                        conn.setRequestProperty("If-Modified-Since", ri.lastModified);
                    }
                    if (ri.etag != null) {
                        conn.setRequestProperty("If-None-Match", ri.etag);
                    }
                }
            }
            if (_underscoreusername != null && _underscorepassword != null) {
                String authenticationStr = _underscoreusername + ":" + _underscorepassword;
                String encodedAuthStr = Base64.encodeBytes(authenticationStr.getBytes());
                conn.setRequestProperty("Authorization", "Basic " + encodedAuthStr);
            }
            _underscorehttpResponseCode = conn.getResponseCode();
            if (_underscorehttpResponseCode == HttpURLConnection.HTTP_underscoreOK) {
                if (_underscoredoConditionalGET) {
                    ResourceInfo ri = new ResourceInfo();
                    ri.lastModified = conn.getHeaderField("Last-Modified");
                    ri.etag = conn.getHeaderField("ETag");
                    Hashtable table = conditionalGetTable();
                    table.put(_underscoreurl.toString(), ri);
                    storeConditionalGetTable(table);
                }
            } else if (_underscorehttpResponseCode == HttpURLConnection.HTTP_underscoreNOT_underscoreMODIFIED) {
                _underscoreshouldGET = false;
            } else {
                Log.getInstance().write("Error getting url: " + _underscoreurl + "\n" + "Error code: " + _underscorehttpResponseCode);
                _underscoreerror = HTTP_underscoreNOT_underscoreOK;
                conn.disconnect();
                conn = null;
            }
        } catch (SocketException ex) {
            conn.disconnect();
            conn = null;
            _underscoreerror = NETWORK_underscoreDOWN;
        }
        return conn;
    }

