    public static String getTitleFromURLFast(String p_underscoreurl) throws Exception {
        URL x_underscoreurl = new URL(p_underscoreurl);
        URLConnection x_underscoreconn = x_underscoreurl.openConnection();
        InputStreamReader x_underscoreis_underscorereader = new InputStreamReader(x_underscoreconn.getInputStream());
        BufferedReader x_underscorereader = new BufferedReader(x_underscoreis_underscorereader);
        String x_underscoreline = null;
        String x_underscoretitle_underscoreline = null;
        String x_underscorelc_underscoreline = null;
        int x_underscoretitle = -1;
        int x_underscoreend = -1;
        while ((x_underscoreline = x_underscorereader.readLine()) != null) {
            x_underscorelc_underscoreline = x_underscoreline.toLowerCase();
            x_underscoretitle = x_underscorelc_underscoreline.indexOf("<title");
            if (x_underscoretitle != -1) {
                x_underscoreend = x_underscorelc_underscoreline.indexOf("</title>");
                x_underscoretitle_underscoreline = x_underscoreline.substring((x_underscoretitle + 7), (x_underscoreend == -1 ? x_underscoreline.length() : x_underscoreend));
                break;
            }
        }
        return x_underscoretitle_underscoreline;
    }

