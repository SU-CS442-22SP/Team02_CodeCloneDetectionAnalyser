    public void create_underscorelist() {
        try {
            String data = URLEncoder.encode("PHPSESSID", "UTF-8") + "=" + URLEncoder.encode(this.get_underscoresession(), "UTF-8");
            URL url = new URL(URL_underscoreLOLA + FILE_underscoreCREATE_underscoreLIST);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            line = rd.readLine();
            wr.close();
            rd.close();
            System.out.println("Gene list saved in LOLA");
        } catch (Exception e) {
            System.out.println("error in createList()");
            e.printStackTrace();
        }
    }

