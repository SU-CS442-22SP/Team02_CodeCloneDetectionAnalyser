    public static String get_underscorecontent(String _underscoreurl) throws Exception {
        URL url = new URL(_underscoreurl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String content = new String();
        while ((inputLine = in.readLine()) != null) {
            content += inputLine;
        }
        in.close();
        return content;
    }

