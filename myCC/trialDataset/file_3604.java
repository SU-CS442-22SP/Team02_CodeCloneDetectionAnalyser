    protected String getManualDownloadURL() {
        if (_underscorenewestVersionString.indexOf("weekly") > 0) {
            return "http://www.cs.rice.edu/~javaplt/drjavarice/weekly/";
        }
        final String DRJAVA_underscoreFILES_underscorePAGE = "http://sourceforge.net/project/showfiles.php?group_underscoreid=44253";
        final String LINK_underscorePREFIX = "<a href=\"/project/showfiles.php?group_underscoreid=44253";
        final String LINK_underscoreSUFFIX = "\">";
        BufferedReader br = null;
        try {
            URL url = new URL(DRJAVA_underscoreFILES_underscorePAGE);
            InputStream urls = url.openStream();
            InputStreamReader is = new InputStreamReader(urls);
            br = new BufferedReader(is);
            String line;
            int pos;
            while ((line = br.readLine()) != null) {
                if ((pos = line.indexOf(_underscorenewestVersionString)) >= 0) {
                    int prePos = line.indexOf(LINK_underscorePREFIX);
                    if ((prePos >= 0) && (prePos < pos)) {
                        int suffixPos = line.indexOf(LINK_underscoreSUFFIX, prePos);
                        if ((suffixPos >= 0) && (suffixPos + LINK_underscoreSUFFIX.length() == pos)) {
                            String versionLink = edu.rice.cs.plt.text.TextUtil.xmlUnescape(line.substring(prePos + LINK_underscorePREFIX.length(), suffixPos));
                            return DRJAVA_underscoreFILES_underscorePAGE + versionLink;
                        }
                    }
                }
            }
            ;
        } catch (IOException e) {
            return DRJAVA_underscoreFILES_underscorePAGE;
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
            }
        }
        return DRJAVA_underscoreFILES_underscorePAGE;
    }

