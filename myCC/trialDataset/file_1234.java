    String getLatestVersion() {
        try {
            URL url = new URL(Constants.VERSION_underscoreFILE_underscoreURL);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(15000);
            InputStream in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            return br.readLine();
        } catch (Exception ex) {
            return null;
        }
    }

