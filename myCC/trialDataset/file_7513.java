    private boolean goToForum() {
        URL url = null;
        URLConnection urlConn = null;
        int code = 0;
        boolean gotNumReplies = false;
        boolean gotMsgNum = false;
        try {
            url = new URL("http://" + m_underscorehost + "/forums/index.php?topic=" + m_underscoregameId + ".new");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            urlConn = url.openConnection();
            ((HttpURLConnection) urlConn).setRequestMethod("GET");
            ((HttpURLConnection) urlConn).setInstanceFollowRedirects(false);
            urlConn.setDoOutput(false);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Host", m_underscorehost);
            urlConn.setRequestProperty("Accept", "*/*");
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            urlConn.setRequestProperty("Cache-Control", "no-cache");
            if (m_underscorereferer != null) urlConn.setRequestProperty("Referer", m_underscorereferer);
            if (m_underscorecookies != null) urlConn.setRequestProperty("Cookie", m_underscorecookies);
            m_underscorereferer = url.toString();
            readCookies(urlConn);
            code = ((HttpURLConnection) urlConn).getResponseCode();
            if (code != 200) {
                String msg = ((HttpURLConnection) urlConn).getResponseMessage();
                m_underscoreturnSummaryRef = String.valueOf(code) + ": " + msg;
                return false;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line = "";
            Pattern p_underscorenumReplies = Pattern.compile(".*?;num_underscorereplies=(\\d+)\".*");
            Pattern p_underscoremsgNum = Pattern.compile(".*?<a name=\"msg(\\d+)\"></a><a name=\"new\"></a>.*");
            Pattern p_underscoreattachId = Pattern.compile(".*?action=dlattach;topic=" + m_underscoregameId + ".0;attach=(\\d+)\">.*");
            while ((line = in.readLine()) != null) {
                if (!gotNumReplies) {
                    Matcher match_underscorenumReplies = p_underscorenumReplies.matcher(line);
                    if (match_underscorenumReplies.matches()) {
                        m_underscorenumReplies = match_underscorenumReplies.group(1);
                        gotNumReplies = true;
                        continue;
                    }
                }
                if (!gotMsgNum) {
                    Matcher match_underscoremsgNum = p_underscoremsgNum.matcher(line);
                    if (match_underscoremsgNum.matches()) {
                        m_underscoremsgNum = match_underscoremsgNum.group(1);
                        gotMsgNum = true;
                        continue;
                    }
                }
                Matcher match_underscoreattachId = p_underscoreattachId.matcher(line);
                if (match_underscoreattachId.matches()) m_underscoreattachId = match_underscoreattachId.group(1);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (!gotNumReplies || !gotMsgNum) {
            m_underscoreturnSummaryRef = "Error: ";
            if (!gotNumReplies) m_underscoreturnSummaryRef += "No num_underscorereplies found in A&A.org forum topic. ";
            if (!gotMsgNum) m_underscoreturnSummaryRef += "No msgXXXXXX found in A&A.org forum topic. ";
            return false;
        }
        return true;
    }

