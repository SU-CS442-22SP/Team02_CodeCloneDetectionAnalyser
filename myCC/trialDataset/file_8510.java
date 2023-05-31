    private static String webService(String strUrl) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(strUrl);
            InputStream input = url.openStream();
            String sCurrentLine = "";
            InputStreamReader read = new InputStreamReader(input, "utf-8");
            BufferedReader l_underscorereader = new java.io.BufferedReader(read);
            while ((sCurrentLine = l_underscorereader.readLine()) != null) {
                buffer.append(sCurrentLine);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

