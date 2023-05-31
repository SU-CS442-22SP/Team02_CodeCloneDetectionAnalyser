    public static String getMD5(String _underscorepwd) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(_underscorepwd.getBytes());
            return toHexadecimal(new String(md.digest()).getBytes());
        } catch (NoSuchAlgorithmException x) {
            x.printStackTrace();
            return "";
        }
    }

