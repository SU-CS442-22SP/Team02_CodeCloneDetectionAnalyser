    private void populateSessionId() throws IOException, java.net.MalformedURLException {
        String general_underscoresearch_underscoreurl = "http://agricola.nal.usda.gov/cgi-bin/Pwebrecon.cgi?" + "DB=local&CNT=1&Search_underscoreArg=RNAi&Search_underscoreCode=GKEY&STARTDB=AGRIDB";
        String sidString = "", inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader((new URL(general_underscoresearch_underscoreurl)).openStream()));
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith("<INPUT TYPE=HIDDEN NAME=PID VALUE=")) {
                sidString = (inputLine.substring(inputLine.indexOf("PID VALUE=") + 11, inputLine.indexOf(">") - 1));
            }
        }
        sessionId = Integer.parseInt(sidString.trim());
    }

