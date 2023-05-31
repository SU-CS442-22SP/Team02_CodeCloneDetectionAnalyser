    private static String getDocumentAt(String urlString) {
        StringBuffer html_underscoretext = new StringBuffer();
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) html_underscoretext.append(line + "\n");
            reader.close();
        } catch (MalformedURLException e) {
            System.out.println("��Ч��URL: " + urlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html_underscoretext.toString();
    }

