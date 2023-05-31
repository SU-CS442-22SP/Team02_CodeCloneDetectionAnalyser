    private CExtractHelper getData(String p_underscoreurl) {
        CExtractHelper l_underscoreextractHelper = new CExtractHelper();
        URL l_underscoreurl;
        HttpURLConnection l_underscoreconnection;
        try {
            System.out.println("Getting [" + p_underscoreurl + "]");
            l_underscoreurl = new URL(p_underscoreurl);
            try {
                URLConnection l_underscoreuconn = l_underscoreurl.openConnection();
                l_underscoreconnection = (HttpURLConnection) l_underscoreuconn;
                l_underscoreconnection.setConnectTimeout(2000);
                l_underscoreconnection.setReadTimeout(2000);
                l_underscoreconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
                l_underscoreconnection.connect();
                int l_underscoreresponseCode = l_underscoreconnection.getResponseCode();
                String response = l_underscoreconnection.getResponseMessage();
                System.out.println("HTTP/1.x " + l_underscoreresponseCode + " " + response);
                for (int j = 1; ; j++) {
                    String l_underscoreheader = l_underscoreconnection.getHeaderField(j);
                    String l_underscorekey = l_underscoreconnection.getHeaderFieldKey(j);
                    if (l_underscoreheader == null || l_underscorekey == null) {
                        break;
                    }
                }
                InputStream l_underscoreinputStream = new BufferedInputStream(l_underscoreconnection.getInputStream());
                CRemoteXML l_underscoreparser = new CRemoteXML();
                try {
                    Document l_underscoredocument = l_underscoreparser.parse(l_underscoreinputStream);
                    PrintWriter l_underscorewriterOut = new PrintWriter(new OutputStreamWriter(System.out, charsetName), true);
                    OutputFormat l_underscoreformat = OutputFormat.createPrettyPrint();
                    XMLWriter l_underscorexmlWriter = new XMLWriter(l_underscorewriterOut, l_underscoreformat);
                    l_underscorexmlWriter.write(l_underscoredocument);
                    l_underscorexmlWriter.flush();
                    l_underscoreconnection.disconnect();
                    l_underscoreextractHelper.m_underscoredocument = l_underscoredocument;
                    return l_underscoreextractHelper;
                } catch (DocumentException e) {
                    e.printStackTrace();
                    l_underscoreconnection.disconnect();
                    System.out.println("XML parsing issue");
                    l_underscoreextractHelper.m_underscoregeneralFailure = true;
                }
            } catch (SocketTimeoutException e) {
                l_underscoreextractHelper.m_underscoretimeoutFailure = true;
                System.out.println("Timed out");
            } catch (IOException e) {
                e.printStackTrace();
                l_underscoreextractHelper.m_underscoregeneralFailure = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            l_underscoreextractHelper.m_underscoregeneralFailure = true;
        }
        return l_underscoreextractHelper;
    }

