    public static String urlPost(Map<String, String> paraMap, String urlStr) throws IOException {
        String strParam = "";
        for (Map.Entry<String, String> entry : paraMap.entrySet()) {
            strParam = strParam + (entry.getKey() + "=" + entry.getValue()) + "&";
        }
        URL url = new URL(urlStr);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
        out.write(strParam);
        out.flush();
        out.close();
        String sCurrentLine;
        String sTotalString;
        sCurrentLine = "";
        sTotalString = "";
        InputStream l_underscoreurlStream;
        l_underscoreurlStream = connection.getInputStream();
        BufferedReader l_underscorereader = new BufferedReader(new InputStreamReader(l_underscoreurlStream));
        while ((sCurrentLine = l_underscorereader.readLine()) != null) {
            sTotalString += sCurrentLine + "\r\n";
        }
        System.out.println(sTotalString);
        return sTotalString;
    }

