    private void logoutUser(String session) {
        try {
            String data = URLEncoder.encode("SESSION", "UTF-8") + "=" + URLEncoder.encode("" + session, "UTF-8");
            if (_underscorelog != null) _underscorelog.error("Voice: logoutUser = " + m_underscorestrUrl + "LogoutUserServlet&" + data);
            URL url = new URL(m_underscorestrUrl + "LogoutUserServlet");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            wr.close();
            rd.close();
        } catch (Exception e) {
            if (_underscorelog != null) _underscorelog.error("Voice error : " + e);
        }
    }

