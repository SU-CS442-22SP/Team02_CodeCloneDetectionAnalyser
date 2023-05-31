    private void delete(String location) throws Exception {
        URL url = new URL(location);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_underscoreOK && responseCode != HttpURLConnection.HTTP_underscoreNO_underscoreCONTENT) {
            String response = "location " + location + " responded: " + conn.getResponseMessage() + " (" + responseCode + ")";
            fail(response);
        }
    }

