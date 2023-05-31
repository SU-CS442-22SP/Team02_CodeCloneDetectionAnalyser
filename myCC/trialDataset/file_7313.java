    private void announce(String trackerURL, byte[] hash, byte[] peerId, int port) {
        try {
            String strUrl = trackerURL + "?info_underscorehash=" + URLEncoder.encode(new String(hash, Constants.BYTE_underscoreENCODING), Constants.BYTE_underscoreENCODING).replaceAll("\\+", "%20") + "&peer_underscoreid=" + URLEncoder.encode(new String(peerId, Constants.BYTE_underscoreENCODING), Constants.BYTE_underscoreENCODING).replaceAll("\\+", "%20") + "&port=" + port + "&uploaded=0&downloaded=0&left=0&numwant=50&no_underscorepeer_underscoreid=1&compact=1";
            URL url = new URL(strUrl);
            URLConnection con = url.openConnection();
            con.connect();
            con.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

