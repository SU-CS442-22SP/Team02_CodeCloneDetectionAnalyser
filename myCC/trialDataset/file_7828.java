    public void getWebByUrl(String strUrl, String charset, String fileIndex) {
        try {
            System.out.println("Getting web by url: " + strUrl);
            addReport("Getting web by url: " + strUrl + "\n");
            URL url = new URL(strUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            InputStream is = null;
            is = url.openStream();
            String filePath = fPath + "/web" + fileIndex + ".htm";
            PrintWriter pw = null;
            FileOutputStream fos = new FileOutputStream(filePath);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            pw = new PrintWriter(writer);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String rLine = null;
            String tmp_underscorerLine = null;
            while ((rLine = bReader.readLine()) != null) {
                tmp_underscorerLine = rLine;
                int str_underscorelen = tmp_underscorerLine.length();
                if (str_underscorelen > 0) {
                    sb.append("\n" + tmp_underscorerLine);
                    pw.println(tmp_underscorerLine);
                    pw.flush();
                    if (deepUrls.get(strUrl) < webDepth) getUrlByString(tmp_underscorerLine, strUrl);
                }
                tmp_underscorerLine = null;
            }
            is.close();
            pw.close();
            System.out.println("Get web successfully! " + strUrl);
            addReport("Get web successfully! " + strUrl + "\n");
            addWebSuccessed();
        } catch (Exception e) {
            System.out.println("Get web failed!       " + strUrl);
            addReport("Get web failed!       " + strUrl + "\n");
            addWebFailed();
        }
    }

