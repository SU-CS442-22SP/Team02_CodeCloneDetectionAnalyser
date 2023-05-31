    public static int sendMessage(String auth_underscoretoken, String registrationId, String message) throws IOException {
        StringBuilder postDataBuilder = new StringBuilder();
        postDataBuilder.append(PARAM_underscoreREGISTRATION_underscoreID).append("=").append(registrationId);
        postDataBuilder.append("&").append(PARAM_underscoreCOLLAPSE_underscoreKEY).append("=").append("0");
        postDataBuilder.append("&").append("data.payload").append("=").append(URLEncoder.encode(message, UTF8));
        byte[] postData = postDataBuilder.toString().getBytes(UTF8);
        URL url = new URL(C2DM_underscoreSEND_underscoreENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + auth_underscoretoken);
        OutputStream out = conn.getOutputStream();
        out.write(postData);
        out.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpServletResponse.SC_underscoreUNAUTHORIZED || responseCode == HttpServletResponse.SC_underscoreFORBIDDEN) {
            AuthenticationUtil.getTokenFromServer(Util.USER, Util.PASSWORD);
            sendMessage(auth_underscoretoken, registrationId, message);
        }
        String updatedAuthToken = conn.getHeaderField(UPDATE_underscoreCLIENT_underscoreAUTH);
        if (updatedAuthToken != null && !auth_underscoretoken.equals(updatedAuthToken)) {
            Util.updateToken(updatedAuthToken);
        }
        return responseCode;
    }

