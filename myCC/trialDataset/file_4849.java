    public static String calculateHA1(String username, byte[] password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(getBytes(username, ISO_underscore8859_underscore1));
            md.update((byte) ':');
            md.update(getBytes(DAAP_underscoreREALM, ISO_underscore8859_underscore1));
            md.update((byte) ':');
            md.update(password);
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException err) {
            throw new RuntimeException(err);
        }
    }

