    public static void getResponseAsStream(String _underscoreurl, Object _underscorestringOrStream, OutputStream _underscorestream, Map<String, String> _underscoreheaders, Map<String, String> _underscoreparams, String _underscorecontentType, int _underscoretimeout) throws IOException {
        if (_underscoreurl == null || _underscoreurl.length() <= 0) throw new IllegalArgumentException("Url can not be null.");
        String temp = _underscoreurl.toLowerCase();
        if (!temp.startsWith("http://") && !temp.startsWith("https://")) _underscoreurl = "http://" + _underscoreurl;
        _underscoreurl = encodeURL(_underscoreurl);
        HttpMethod method = null;
        if (_underscorestringOrStream == null && (_underscoreparams == null || _underscoreparams.size() <= 0)) method = new GetMethod(_underscoreurl); else method = new PostMethod(_underscoreurl);
        HttpMethodParams methodParams = ((HttpMethodBase) method).getParams();
        if (methodParams == null) {
            methodParams = new HttpMethodParams();
            ((HttpMethodBase) method).setParams(methodParams);
        }
        if (_underscoretimeout < 0) methodParams.setSoTimeout(0); else methodParams.setSoTimeout(_underscoretimeout);
        if (_underscorecontentType != null && _underscorecontentType.length() > 0) {
            if (_underscoreheaders == null) _underscoreheaders = new HashMap<String, String>();
            _underscoreheaders.put("Content-Type", _underscorecontentType);
        }
        if (_underscoreheaders == null || !_underscoreheaders.containsKey("User-Agent")) {
            if (_underscoreheaders == null) _underscoreheaders = new HashMap<String, String>();
            _underscoreheaders.put("User-Agent", DEFAULT_underscoreUSERAGENT);
        }
        if (_underscoreheaders != null) {
            Iterator<Map.Entry<String, String>> iter = _underscoreheaders.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                method.setRequestHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (method instanceof PostMethod && (_underscoreparams != null && _underscoreparams.size() > 0)) {
            Iterator<Map.Entry<String, String>> iter = _underscoreparams.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                ((PostMethod) method).addParameter((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (method instanceof EntityEnclosingMethod && _underscorestringOrStream != null) {
            if (_underscorestringOrStream instanceof InputStream) {
                RequestEntity entity = new InputStreamRequestEntity((InputStream) _underscorestringOrStream);
                ((EntityEnclosingMethod) method).setRequestEntity(entity);
            } else {
                RequestEntity entity = new StringRequestEntity(_underscorestringOrStream.toString(), _underscorecontentType, null);
                ((EntityEnclosingMethod) method).setRequestEntity(entity);
            }
        }
        HttpClient httpClient = new HttpClient(new org.apache.commons.httpclient.SimpleHttpConnectionManager());
        httpClient.getParams().setBooleanParameter(HttpClientParams.ALLOW_underscoreCIRCULAR_underscoreREDIRECTS, true);
        InputStream instream = null;
        try {
            int status = httpClient.executeMethod(method);
            if (status != HttpStatus.SC_underscoreOK) {
                LOG.warn("Http Satus:" + status + ",Url:" + _underscoreurl);
                if (status >= 500 && status < 600) throw new IOException("Remote service<" + _underscoreurl + "> respose a error, status:" + status);
            }
            instream = method.getResponseBodyAsStream();
            IOUtils.copy(instream, _underscorestream);
        } catch (IOException err) {
            LOG.error("Failed to access " + _underscoreurl, err);
            throw err;
        } finally {
            IOUtils.closeQuietly(instream);
            if (method != null) method.releaseConnection();
        }
    }

