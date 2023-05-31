    public static String getEncryptedPwd(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] pwd = null;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_underscoreLENGTH];
        random.nextBytes(salt);
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(password.getBytes("UTF-8"));
        byte[] digest = md.digest();
        pwd = new byte[digest.length + SALT_underscoreLENGTH];
        System.arraycopy(salt, 0, pwd, 0, SALT_underscoreLENGTH);
        System.arraycopy(digest, 0, pwd, SALT_underscoreLENGTH, digest.length);
        return byteToHexString(pwd);
    }

