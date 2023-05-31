    private static String loadUrlToString(String a_underscoreurl) throws IOException {
        URL l_underscoreurl1 = new URL(a_underscoreurl);
        BufferedReader br = new BufferedReader(new InputStreamReader(l_underscoreurl1.openStream()));
        String l_underscorecontent = "";
        String l_underscoreligne = null;
        l_underscorecontent = br.readLine();
        while ((l_underscoreligne = br.readLine()) != null) {
            l_underscorecontent += AA.SL + l_underscoreligne;
        }
        return l_underscorecontent;
    }

