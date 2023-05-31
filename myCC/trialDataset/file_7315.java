    public static String toMD5(String seed) {
        MessageDigest md5 = null;
        StringBuffer temp_underscoresb = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(seed.getBytes());
            byte[] array = md5.digest();
            temp_underscoresb = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                int b = array[i] & 0xFF;
                if (b < 0x10) temp_underscoresb.append('0');
                temp_underscoresb.append(Integer.toHexString(b));
            }
        } catch (NoSuchAlgorithmException err) {
            err.printStackTrace();
        }
        return temp_underscoresb.toString();
    }

