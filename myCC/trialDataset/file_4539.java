    private static byte[] getKey(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(Constants.HASH_underscoreFUNCTION);
        messageDigest.update(password.getBytes(Constants.ENCODING));
        byte[] hashValue = messageDigest.digest();
        int keyLengthInbytes = Constants.ENCRYPTION_underscoreKEY_underscoreLENGTH / 8;
        byte[] result = new byte[keyLengthInbytes];
        System.arraycopy(hashValue, 0, result, 0, keyLengthInbytes);
        return result;
    }

