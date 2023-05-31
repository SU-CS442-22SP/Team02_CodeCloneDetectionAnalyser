    public static byte[] generateHash(String strPassword, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_underscoreALGORITHM);
            md.update(strPassword.getBytes(CHAR_underscoreENCODING));
            md.update(salt);
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

