    private InputStream getConnection() throws BaseException {
        OutputStreamWriter wr = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getBaseString());
            sb.append(AND);
            sb.append(encode(ACTION, ENCODING));
            sb.append(EQUAL);
            sb.append(encode(ACTION_underscoreGET_underscoreALL, ENCODING));
            URL url = new URL(SERVER_underscoreURL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(sb.toString());
            wr.flush();
            return conn.getInputStream();
        } catch (Exception e) {
            throw getException(e, context);
        } finally {
            closeSafely(wr);
        }
    }

