    public static String doGet(String http_underscoreurl, String get_underscoredata) {
        URL url;
        try {
            if ((get_underscoredata != "") && (get_underscoredata != null)) {
                url = new URL(http_underscoreurl + "?" + get_underscoredata);
            } else {
                url = new URL(http_underscoreurl);
            }
            URLConnection conn = url.openConnection();
            InputStream stream = new BufferedInputStream(conn.getInputStream());
            try {
                StringBuffer b = new StringBuffer();
                int ch;
                while ((ch = stream.read()) != -1) b.append((char) ch);
                return b.toString();
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            ;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

