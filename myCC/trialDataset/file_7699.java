    public String login() {
        System.out.println("Logging in to LOLA");
        try {
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(get_underscoreemail(), "UTF-8");
            data += "&" + URLEncoder.encode("pw", "UTF-8") + "=" + URLEncoder.encode(get_underscorepw(), "UTF-8");
            URL url = new URL(URL_underscoreLOLA + FILE_underscoreLOGIN);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line, sessid;
            line = rd.readLine();
            sessid = get_underscoresessid(line);
            this.set_underscoresession(sessid);
            wr.close();
            rd.close();
            return sessid;
        } catch (Exception e) {
            System.out.println("Login Error");
            return "";
        }
    }

