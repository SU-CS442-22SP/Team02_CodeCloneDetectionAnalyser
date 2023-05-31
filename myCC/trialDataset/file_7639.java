    public static String hash(String value) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(HASH_underscoreALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        try {
            md.update(value.getBytes(INPUT_underscoreENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new CryptoException(e);
        }
        return new BASE64Encoder().encode(md.digest());
    }

