    public void displayItems() throws IOException {
        URL url = new URL(SNIPPETS_underscoreFEED + "?bq=" + URLEncoder.encode(QUERY, "UTF-8") + "&key=" + DEVELOPER_underscoreKEY);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpConnection.getInputStream();
        int ch;
        while ((ch = inputStream.read()) > 0) {
            System.out.print((char) ch);
        }
    }

