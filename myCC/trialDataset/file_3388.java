    public boolean add(String url) {
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
            request.setRequestMethod("POST");
            request.setRequestProperty(GameRecord.GAME_underscoreIP_underscoreHEADER, String.valueOf(ip));
            request.setRequestProperty(GameRecord.GAME_underscorePORT_underscoreHEADER, String.valueOf(port));
            request.setRequestProperty(GameRecord.GAME_underscoreMESSAGE_underscoreHEADER, message);
            request.setRequestProperty(GameRecord.GAME_underscoreLATITUDE_underscoreHEADER, df.format(lat));
            request.setRequestProperty(GameRecord.GAME_underscoreLONGITUDE_underscoreHEADER, df.format(lon));
            request.setRequestProperty("Content-Length", "0");
            request.connect();
            if (request.getResponseCode() != HttpURLConnection.HTTP_underscoreOK) {
                throw new IOException("Unexpected response: " + request.getResponseCode() + " " + request.getResponseMessage());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

