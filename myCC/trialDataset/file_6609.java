    public static String cryptoSHA(String _underscorestrSrc) {
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(_underscorestrSrc.getBytes());
            byte[] buffer = sha.digest();
            return encoder.encode(buffer);
        } catch (Exception err) {
            System.out.println(err);
        }
        return "";
    }

