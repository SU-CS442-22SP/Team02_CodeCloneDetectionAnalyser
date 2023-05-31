    public String sendMessage(String message, boolean log) {
        StringBuilder ret;
        try {
            URL url = new URL(this.stringURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("User-Agent", serverName);
            urlConnection.setRequestProperty("Host", ip);
            urlConnection.setRequestProperty("Content-type", "text/xml");
            urlConnection.setRequestProperty("Content-length", Integer.toString(message.length()));
            PrintWriter _underscoreout = new PrintWriter(urlConnection.getOutputStream());
            if (log) {
                CampaignData.mwlog.infoLog("Sending Message: " + MWCyclopsUtils.formatMessage(message));
            } else CampaignData.mwlog.infoLog("Sending Message: " + message);
            _underscoreout.println(message);
            _underscoreout.flush();
            _underscoreout.close();
            ret = new StringBuilder();
            if (log) {
                BufferedReader _underscorein = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String input;
                while ((input = _underscorein.readLine()) != null) ret.append(input + "\n");
                CampaignData.mwlog.infoLog(ret.toString());
                _underscorein.close();
            } else {
                BufferedReader _underscorein = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while (_underscorein.readLine() != null) {
                }
                _underscorein.close();
            }
            _underscoreout.close();
            urlConnection.disconnect();
            return ret.toString();
        } catch (Exception ex) {
            CampaignData.mwlog.errLog(ex);
        }
        return "";
    }

