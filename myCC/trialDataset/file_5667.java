    public static Vector[] getLinksFromURLFast(String p_underscoreurl) throws Exception {
        timeCheck("getLinksFromURLFast ");
        URL x_underscoreurl = new URL(p_underscoreurl);
        URLConnection x_underscoreconn = x_underscoreurl.openConnection();
        InputStreamReader x_underscoreis_underscorereader = new InputStreamReader(x_underscoreconn.getInputStream());
        BufferedReader x_underscorereader = new BufferedReader(x_underscoreis_underscorereader);
        String x_underscoreline = null;
        RE e = new RE("(.*/)", RE.REG_underscoreICASE);
        System.out.println("RE: " + e.toString());
        REMatch x_underscorematch = e.getMatch(p_underscoreurl);
        String x_underscoredir = p_underscoreurl.substring(x_underscorematch.getSubStartIndex(1), x_underscorematch.getSubEndIndex(1));
        e = new RE("(http://.*?)/?", RE.REG_underscoreICASE);
        x_underscorematch = e.getMatch(p_underscoreurl);
        String x_underscoreroot = p_underscoreurl.substring(x_underscorematch.getSubStartIndex(1), x_underscorematch.getSubEndIndex(1));
        e = new RE("<a href=\"?(.*?)\"?>(.*?)</a>", RE.REG_underscoreICASE);
        System.out.println("RE: " + e.toString());
        Vector x_underscorelinks = new Vector(100);
        Vector x_underscoretexts = new Vector(100);
        StringBuffer x_underscorebuf = new StringBuffer(10000);
        REMatch[] x_underscorematches = null;
        timeCheck("starting parsing ");
        while ((x_underscoreline = x_underscorereader.readLine()) != null) {
            x_underscorebuf.append(x_underscoreline);
        }
        String x_underscorepage = x_underscorebuf.toString();
        String x_underscorelink = null;
        x_underscorematches = e.getAllMatches(x_underscorepage);
        for (int i = 0; i < x_underscorematches.length; i++) {
            x_underscorelink = x_underscorepage.substring(x_underscorematches[i].getSubStartIndex(1), x_underscorematches[i].getSubEndIndex(1));
            if (x_underscorelink.indexOf("mailto:") != -1) continue;
            x_underscorelink = toAbsolute(x_underscoreroot, x_underscoredir, x_underscorelink);
            x_underscorelinks.addElement(x_underscorelink);
            x_underscoretexts.addElement(x_underscorepage.substring(x_underscorematches[i].getSubStartIndex(2), x_underscorematches[i].getSubEndIndex(2)));
        }
        Vector[] x_underscoreresult = new Vector[2];
        x_underscoreresult[0] = x_underscorelinks;
        x_underscoreresult[1] = x_underscoretexts;
        timeCheck("end parsing ");
        return x_underscoreresult;
    }

