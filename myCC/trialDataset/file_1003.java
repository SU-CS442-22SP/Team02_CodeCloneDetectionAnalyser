    public static void getResponseAsStream(String _underscoreurl, Object _underscorestringOrStream, OutputStream _underscorestream, Map _underscoreheaders, Map _underscoreparams, String _underscorecontentType, int _underscoretimeout) throws IOException {
        if (_underscoreurl == null || _underscoreurl.length() <= 0) throw new IllegalArgumentException("Url can not be null.");
        String temp = _underscoreurl.toLowerCase();
        if (!temp.startsWith("http://") && !temp.startsWith("https://")) _underscoreurl = "http://" + _underscoreurl;
        HttpMethod method = null;
        if (_underscorestringOrStream == null && (_underscoreparams == null || _underscoreparams.size() <= 0)) method = new GetMethod(_underscoreurl); else method = new PostMethod(_underscoreurl);
        HttpMethodParams params = ((HttpMethodBase) method).getParams();
        if (params == null) {
            params = new HttpMethodParams();
            ((HttpMethodBase) method).setParams(params);
        }
        if (_underscoretimeout < 0) params.setSoTimeout(0); else params.setSoTimeout(_underscoretimeout);
        if (_underscorecontentType != null && _underscorecontentType.length() > 0) {
            if (_underscoreheaders == null) _underscoreheaders = new HashMap();
            _underscoreheaders.put("Content-Type", _underscorecontentType);
        }
        if (_underscoreheaders != null) {
            Iterator iter = _underscoreheaders.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                method.setRequestHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (method instanceof PostMethod && (_underscoreparams != null && _underscoreparams.size() > 0)) {
            Iterator iter = _underscoreparams.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
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
        try {
            int status = httpClient.executeMethod(method);
            if (status != HttpStatus.SC_underscoreOK) {
                if (status >= 500 && status < 600) throw new IOException("Remote service<" + _underscoreurl + "> respose a error, status:" + status);
            }
            InputStream instream = method.getResponseBodyAsStream();
            IOUtils.copy(instream, _underscorestream);
            instream.close();
        } catch (IOException err) {
            throw err;
        } finally {
            if (method != null) method.releaseConnection();
        }
    }

