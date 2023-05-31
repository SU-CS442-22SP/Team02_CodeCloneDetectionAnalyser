    public void write(HttpServletResponse res) throws MalformedURLException, IOException {
        if (m_underscoreurl.equals("")) {
            return;
        }
        URL url = new URL(m_underscoreurl);
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        BufferedInputStream in = new BufferedInputStream(con.getInputStream(), BUF_underscoreSIZE);
        BufferedOutputStream out = new BufferedOutputStream(res.getOutputStream());
        byte[] buf = new byte[BUF_underscoreSIZE];
        int size = 0;
        String contentType = con.getContentType();
        if (contentType != null) {
            res.setContentType(con.getContentType());
        }
        while ((size = in.read(buf)) > 0) {
            out.write(buf, 0, size);
        }
        out.flush();
        out.close();
        in.close();
    }

