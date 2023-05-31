    public void generateHtmlPage(String real_underscorefilename, String url_underscorefilename) {
        String str_underscorecontent = "";
        URL m_underscoreurl = null;
        URLConnection m_underscoreurlcon = null;
        try {
            m_underscoreurl = new URL(url_underscorefilename);
            m_underscoreurlcon = m_underscoreurl.openConnection();
            InputStream in_underscorestream = m_underscoreurlcon.getInputStream();
            byte[] bytes = new byte[1];
            Vector v_underscorebytes = new Vector();
            while (in_underscorestream.read(bytes) != -1) {
                v_underscorebytes.add(bytes);
                bytes = new byte[1];
            }
            byte[] all_underscorebytes = new byte[v_underscorebytes.size()];
            for (int i = 0; i < v_underscorebytes.size(); i++) all_underscorebytes[i] = ((byte[]) v_underscorebytes.get(i))[0];
            str_underscorecontent = new String(all_underscorebytes, "GBK");
        } catch (Exception urle) {
        }
        try {
            oaFileOperation file_underscorecontrol = new oaFileOperation();
            file_underscorecontrol.writeFile(str_underscorecontent, real_underscorefilename, true);
            String strPath = url_underscorefilename.substring(0, url_underscorefilename.lastIndexOf("/") + 1);
            String strUrlFileName = url_underscorefilename.substring(url_underscorefilename.lastIndexOf("/") + 1);
            if (strUrlFileName.indexOf(".jsp") > 0) {
                strUrlFileName = strUrlFileName.substring(0, strUrlFileName.indexOf(".jsp")) + "_underscore1.jsp";
                m_underscoreurl = new URL(strPath + strUrlFileName);
                m_underscoreurl.openConnection();
            }
            intWriteFileCount++;
            intWriteFileCount = (intWriteFileCount > 100000) ? 0 : intWriteFileCount;
        } catch (Exception e) {
        }
        m_underscoreurlcon = null;
    }

