    public static Vector getMetaKeywordsFromURL(String p_underscoreurl) throws Exception {
        URL x_underscoreurl = new URL(p_underscoreurl);
        URLConnection x_underscoreconn = x_underscoreurl.openConnection();
        InputStreamReader x_underscoreis_underscorereader = new InputStreamReader(x_underscoreconn.getInputStream());
        BufferedReader x_underscorereader = new BufferedReader(x_underscoreis_underscorereader);
        String x_underscoreline = null;
        String x_underscorelc_underscoreline = null;
        int x_underscorebody = -1;
        String x_underscorekeyword_underscorelist = null;
        int x_underscorekeywords = -1;
        String[] x_underscoremeta_underscorekeywords = null;
        while ((x_underscoreline = x_underscorereader.readLine()) != null) {
            x_underscorelc_underscoreline = x_underscoreline.toLowerCase();
            x_underscorekeywords = x_underscorelc_underscoreline.indexOf("<meta name=\"keywords\" content=\"");
            if (x_underscorekeywords != -1) {
                x_underscorekeywords = "<meta name=\"keywords\" content=\"".length();
                x_underscorekeyword_underscorelist = x_underscoreline.substring(x_underscorekeywords, x_underscoreline.indexOf("\">", x_underscorekeywords));
                x_underscorekeyword_underscorelist = x_underscorekeyword_underscorelist.replace(',', ' ');
                x_underscoremeta_underscorekeywords = Parser.extractWordsFromSpacedList(x_underscorekeyword_underscorelist);
            }
            x_underscorebody = x_underscorelc_underscoreline.indexOf("<body");
            if (x_underscorebody != -1) break;
        }
        Vector x_underscorevector = new Vector(x_underscoremeta_underscorekeywords.length);
        for (int i = 0; i < x_underscoremeta_underscorekeywords.length; i++) x_underscorevector.add(x_underscoremeta_underscorekeywords[i]);
        return x_underscorevector;
    }

