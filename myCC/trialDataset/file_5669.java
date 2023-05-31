    public static Vector getKeywordsFromURLFast(String p_underscoreurl) throws Exception {
        URL x_underscoreurl = new URL(p_underscoreurl);
        URLConnection x_underscoreconn = x_underscoreurl.openConnection();
        InputStreamReader x_underscoreis_underscorereader = new InputStreamReader(x_underscoreconn.getInputStream());
        BufferedReader x_underscorereader = new BufferedReader(x_underscoreis_underscorereader);
        String x_underscoreline = null;
        String x_underscoretitle_underscoreline = null;
        String x_underscorelc_underscoreline = null;
        Vector x_underscorewords = new Vector(1000);
        int x_underscorebody = -1;
        int x_underscoretitle = -1;
        boolean x_underscorebod = false;
        int x_underscoreend = -1;
        while ((x_underscoreline = x_underscorereader.readLine()) != null) {
            x_underscorelc_underscoreline = x_underscoreline.toLowerCase();
            x_underscoretitle = x_underscorelc_underscoreline.indexOf("<title");
            if (x_underscoretitle != -1) {
                x_underscoreend = x_underscorelc_underscoreline.indexOf("</title>");
                x_underscoretitle_underscoreline = x_underscoreline.substring((x_underscoretitle + 7), (x_underscoreend == -1 ? x_underscoreline.length() : x_underscoreend));
            }
            x_underscorebody = x_underscorelc_underscoreline.indexOf("<body");
            if (x_underscorebody != -1) {
                x_underscorebod = true;
                x_underscoreline = x_underscoreline.substring(x_underscorebody + 5);
                break;
            }
        }
        boolean x_underscorestatus = false;
        x_underscoreend = -1;
        String[] x_underscoretemp_underscorewords;
        if (x_underscorebod == false) {
            if (x_underscoretitle_underscoreline != null) {
                x_underscorewords = new Vector();
                x_underscoretemp_underscorewords = extractWordsFromSpacedList(x_underscoretitle_underscoreline);
                for (int i = 0; i < x_underscoretemp_underscorewords.length; i++) x_underscorewords.addElement(x_underscoretemp_underscorewords[i]);
                x_underscorewords.addElement(x_underscoretitle_underscoreline);
                return x_underscorewords;
            } else {
                return null;
            }
        }
        StringBuffer x_underscorebuf = new StringBuffer(35);
        do {
            x_underscorelc_underscoreline = x_underscoreline.toLowerCase();
            x_underscoreend = x_underscorelc_underscoreline.indexOf("</body>");
            if (x_underscoreend != -1) {
                extractOutsideTextFast(x_underscoreline.substring(0, x_underscoreend), '<', '>', x_underscorewords, x_underscorestatus, x_underscorebuf);
                break;
            }
            x_underscorestatus = extractOutsideTextFast(x_underscoreline, '<', '>', x_underscorewords, x_underscorestatus, x_underscorebuf);
        } while ((x_underscoreline = x_underscorereader.readLine()) != null);
        if (x_underscoretitle_underscoreline != null) x_underscorewords.addElement(x_underscoretitle_underscoreline);
        return x_underscorewords;
    }

