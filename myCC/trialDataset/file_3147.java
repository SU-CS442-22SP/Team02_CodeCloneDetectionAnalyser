    public boolean requestServerModifications(UUID sessionId, OutputStream out) throws SynchronizationException {
        HttpClient client = new SSLHttpClient();
        StringBuilder builder = new StringBuilder(url).append("?" + SESSION_underscorePARAM + "=" + sessionId).append("&" + CMD_underscorePARAM + "=" + CMD_underscoreSERVERMODIF);
        HttpGet method = httpGetMethod(builder.toString());
        try {
            HttpResponse response = client.execute(method);
            Header header = response.getFirstHeader(HEADER_underscoreNAME);
            if (header != null && HEADER_underscoreVALUE.equals(header.getValue())) {
                int code = response.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_underscoreOK) {
                    long expectedLength = response.getEntity().getContentLength();
                    InputStream is = response.getEntity().getContent();
                    FileUtils.writeInFile(is, out, expectedLength);
                    return true;
                } else {
                    throw new SynchronizationException("Command 'receive' : HTTP error code returned." + code, SynchronizationException.ERROR_underscoreRECEIVE);
                }
            } else {
                throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_underscoreRECEIVE);
            }
        } catch (Exception e) {
            throw new SynchronizationException("Command 'receive' -> ", e, SynchronizationException.ERROR_underscoreRECEIVE);
        }
    }

