    public static String encodePassword(String _underscoreoriginalPassword) {
        MessageDigest md = null;
        String encodedPassword = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(_underscoreoriginalPassword.getBytes("UTF-8"));
            encodedPassword = (new BASE64Encoder()).encode(md.digest());
        } catch (NoSuchAlgorithmException _underscoree) {
            _underscoree.printStackTrace();
        } catch (UnsupportedEncodingException _underscoree) {
            _underscoree.printStackTrace();
        }
        return encodedPassword;
    }

