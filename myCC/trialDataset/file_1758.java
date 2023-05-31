    public static String fetch(String str_underscoreurl) throws IOException {
        URL url;
        URLConnection connection;
        String jsonText = "";
        url = new URL(str_underscoreurl);
        connection = url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            jsonText += line;
        }
        return jsonText;
    }

