    private void handleURL() throws JspException, IOException {
        Map in_underscoremap = prepareInputMap();
        String in_underscorestr = JSONTransformer.serialize(in_underscoremap);
        byte[] input = in_underscorestr.getBytes("UTF-8");
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.connect();
        OutputStream os = conn.getOutputStream();
        os.write(input);
        os.close();
        InputStream is = conn.getInputStream();
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        StringBuffer s_underscorebuf = new StringBuffer();
        char[] tmp_underscorebuf = new char[1024];
        int count;
        while ((count = reader.read(tmp_underscorebuf)) != -1) {
            if (count == 0) continue;
            s_underscorebuf.append(tmp_underscorebuf, 0, count);
        }
        reader.close();
        Map out_underscoremap = null;
        try {
            out_underscoremap = JSONTransformer.parseObject(s_underscorebuf.toString());
        } catch (ParseException e) {
            returnErrorResult(e.getMessage());
        }
        handleResultMap(out_underscoremap);
    }

