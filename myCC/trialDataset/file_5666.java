    public static Vector getKeywordsFromURL(String p_underscoreurl) throws Exception {
        URL x_underscoreurl = new URL(p_underscoreurl);
        URLConnection x_underscoreconn = x_underscoreurl.openConnection();
        InputStreamReader x_underscoreis_underscorereader = new InputStreamReader(x_underscoreconn.getInputStream());
        BufferedReader x_underscorereader = new BufferedReader(x_underscoreis_underscorereader);
        String x_underscoreline = null;
        String x_underscorelc_underscoreline = null;
        Vector x_underscorewords = new Vector(1000);
        int x_underscorebody = -1;
        while ((x_underscoreline = x_underscorereader.readLine()) != null) {
            x_underscorelc_underscoreline = x_underscoreline.toLowerCase();
            x_underscorebody = x_underscorelc_underscoreline.indexOf("<body");
            if (x_underscorebody != -1) {
                x_underscoreline = x_underscoreline.substring(x_underscorebody + 5);
                break;
            }
        }
        boolean x_underscorestatus = false;
        int x_underscoreend = -1;
        if (x_underscorelc_underscoreline == null) {
            System.out.println("No <body start");
            return null;
        }
        do {
            x_underscorelc_underscoreline = x_underscoreline.toLowerCase();
            x_underscoreend = x_underscorelc_underscoreline.indexOf("</body>");
            if (x_underscoreend != -1) {
                extractOutsideText(x_underscoreline.substring(0, x_underscoreend), "<", ">", x_underscorewords, x_underscorestatus);
                break;
            }
            x_underscorestatus = extractOutsideText(x_underscoreline, "<", ">", x_underscorewords, x_underscorestatus);
        } while ((x_underscoreline = x_underscorereader.readLine()) != null);
        return x_underscorewords;
    }

