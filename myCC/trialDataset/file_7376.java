    public String jsFunction_underscoresend(String postData) {
        URL url = null;
        try {
            if (_underscoreuri.startsWith("http")) {
                url = new URL(_underscoreuri);
            } else {
                url = new URL("file://./" + _underscoreuri);
            }
        } catch (MalformedURLException e) {
            IdeLog.logError(ScriptingPlugin.getDefault(), Messages.WebRequest_underscoreError, e);
            return StringUtils.EMPTY;
        }
        try {
            URLConnection conn = url.openConnection();
            OutputStreamWriter wr = null;
            if (this._underscoremethod.equals("post")) {
                conn.setDoOutput(true);
                wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(postData);
                wr.flush();
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line + "\r\n");
            }
            if (wr != null) {
                wr.close();
            }
            rd.close();
            String result = sb.toString();
            return result;
        } catch (Exception e) {
            IdeLog.logError(ScriptingPlugin.getDefault(), Messages.WebRequest_underscoreError, e);
            return StringUtils.EMPTY;
        }
    }

