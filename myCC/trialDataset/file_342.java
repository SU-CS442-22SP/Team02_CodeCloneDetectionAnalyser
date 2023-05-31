    public static String readFromUrl(String url) {
        URL url_underscore = null;
        URLConnection uc = null;
        BufferedReader in = null;
        StringBuilder str = new StringBuilder();
        try {
            url_underscore = new URL(url);
            uc = url_underscore.openConnection();
            in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) str.append(inputLine);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

