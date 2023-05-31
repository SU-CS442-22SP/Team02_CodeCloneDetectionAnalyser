    public boolean searchEntity(String login, String password, String searcheId, OutputStream os) throws SynchronizationException {
        HttpClient client = new SSLHttpClient();
        try {
            StringBuilder builder = new StringBuilder(url).append("?" + CMD_underscorePARAM + "=" + CMD_underscoreSEARCH).append("&" + LOGIN_underscorePARAM + "=" + URLEncoder.encode(login, "UTF-8")).append("&" + PASSWD_underscorePARAM + "=" + URLEncoder.encode(password, "UTF-8")).append("&" + SEARCH_underscorePARAM + "=" + searcheId);
            HttpGet method = httpGetMethod(builder.toString());
            HttpResponse response = client.execute(method);
            Header header = response.getFirstHeader(HEADER_underscoreNAME);
            if (header != null && HEADER_underscoreVALUE.equals(HEADER_underscoreVALUE)) {
                int code = response.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_underscoreOK) {
                    long expectedLength = response.getEntity().getContentLength();
                    InputStream is = response.getEntity().getContent();
                    FileUtils.writeInFile(is, os, expectedLength);
                    return true;
                } else {
                    throw new SynchronizationException("Command 'search' : HTTP error code returned." + code, SynchronizationException.ERROR_underscoreSEARCH);
                }
            } else {
                throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_underscoreSEARCH);
            }
        } catch (Exception e) {
            throw new SynchronizationException("Command 'search' -> ", e, SynchronizationException.ERROR_underscoreSEARCH);
        }
    }

