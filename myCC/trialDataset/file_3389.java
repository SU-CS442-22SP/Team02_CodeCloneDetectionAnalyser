    public static boolean delete(String url, int ip, int port) {
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
            request.setRequestMethod("DELETE");
            request.setRequestProperty(GameRecord.GAME_underscoreIP_underscoreHEADER, String.valueOf(ip));
            request.setRequestProperty(GameRecord.GAME_underscorePORT_underscoreHEADER, String.valueOf(port));
            request.connect();
            return request.getResponseCode() == HttpURLConnection.HTTP_underscoreOK;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

