    private boolean postLogin() {
        URL url = null;
        URLConnection urlConn = null;
        OutputStream out = null;
        int code = 0;
        boolean gotPhpsessid = false;
        try {
            url = new URL("http://" + m_underscorehost + "/forums/index.php?action=login2");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            urlConn = url.openConnection();
            ((HttpURLConnection) urlConn).setRequestMethod("POST");
            ((HttpURLConnection) urlConn).setInstanceFollowRedirects(false);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Host", m_underscorehost);
            urlConn.setRequestProperty("Accept", "*/*");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            urlConn.setRequestProperty("Cache-Control", "no-cache");
            out = urlConn.getOutputStream();
            out.write(new String("user=" + m_underscoreusername + "&passwrd=" + m_underscorepassword + "&cookielength=60").getBytes());
            out.flush();
            out.close();
            do {
                readCookies(urlConn);
                m_underscorereferer = url.toString();
                code = ((HttpURLConnection) urlConn).getResponseCode();
                if (code == 301 || code == 302) {
                    url = new URL(urlConn.getHeaderField("Location"));
                    urlConn = url.openConnection();
                    ((HttpURLConnection) urlConn).setRequestMethod("GET");
                    ((HttpURLConnection) urlConn).setInstanceFollowRedirects(false);
                    urlConn.setDoOutput(true);
                    urlConn.setDoInput(true);
                    urlConn.setRequestProperty("Host", m_underscorehost);
                    urlConn.setRequestProperty("Accept", "*/*");
                    urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConn.setRequestProperty("Connection", "Keep-Alive");
                    urlConn.setRequestProperty("Cache-Control", "no-cache");
                    if (m_underscorereferer != null) urlConn.setRequestProperty("Referer", m_underscorereferer);
                    if (m_underscorecookies != null) urlConn.setRequestProperty("Cookie", m_underscorecookies);
                    continue;
                }
                if (code == 200) {
                    String refreshHdr = urlConn.getHeaderField("Refresh");
                    Pattern p_underscorephpsessid = Pattern.compile(".*?\\?PHPSESSID=(\\w+).*");
                    Matcher match_underscorephpsessid = p_underscorephpsessid.matcher(refreshHdr);
                    if (match_underscorephpsessid.matches()) {
                        gotPhpsessid = true;
                    }
                    urlConn = null;
                    continue;
                }
                String msg = ((HttpURLConnection) urlConn).getResponseMessage();
                m_underscoreturnSummaryRef = String.valueOf(code) + ": " + msg;
                return false;
            } while (urlConn != null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (!gotPhpsessid) {
            m_underscoreturnSummaryRef = "Error: PHPSESSID not found after login. ";
            return false;
        }
        if (m_underscorecookies == null) {
            m_underscoreturnSummaryRef = "Error: cookies not found after login. ";
            return false;
        }
        try {
            Thread.sleep(m_underscoreloginDelayInSeconds * 1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return true;
    }

