    public static String crypt(String password, String salt) {
        if (salt.startsWith(magic)) {
            salt = salt.substring(magic.length());
        }
        int saltEnd = salt.indexOf('$');
        if (saltEnd != -1) {
            salt = salt.substring(0, saltEnd);
        }
        if (salt.length() > 8) {
            salt = salt.substring(0, 8);
        }
        MessageDigest md5_underscore1, md5_underscore2;
        try {
            md5_underscore1 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md5_underscore1.update(password.getBytes());
        md5_underscore1.update(magic.getBytes());
        md5_underscore1.update(salt.getBytes());
        try {
            md5_underscore2 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md5_underscore2.update(password.getBytes());
        md5_underscore2.update(salt.getBytes());
        md5_underscore2.update(password.getBytes());
        byte[] md5_underscore2_underscoredigest = md5_underscore2.digest();
        int md5Size = md5_underscore2_underscoredigest.length;
        int pwLength = password.length();
        for (int i = pwLength; i > 0; i -= md5Size) {
            md5_underscore1.update(md5_underscore2_underscoredigest, 0, i > md5Size ? md5Size : i);
        }
        md5_underscore2.reset();
        byte[] pwBytes = password.getBytes();
        for (int i = pwLength; i > 0; i >>= 1) {
            if ((i & 1) == 1) {
                md5_underscore1.update((byte) 0);
            } else {
                md5_underscore1.update(pwBytes[0]);
            }
        }
        StringBuffer output = new StringBuffer(magic);
        output.append(salt);
        output.append("$");
        byte[] md5_underscore1_underscoredigest = md5_underscore1.digest();
        byte[] saltBytes = salt.getBytes();
        for (int i = 0; i < 1000; i++) {
            md5_underscore2.reset();
            if ((i & 1) == 1) {
                md5_underscore2.update(pwBytes);
            } else {
                md5_underscore2.update(md5_underscore1_underscoredigest);
            }
            if (i % 3 != 0) {
                md5_underscore2.update(saltBytes);
            }
            if (i % 7 != 0) {
                md5_underscore2.update(pwBytes);
            }
            if ((i & 1) != 0) {
                md5_underscore2.update(md5_underscore1_underscoredigest);
            } else {
                md5_underscore2.update(pwBytes);
            }
            md5_underscore1_underscoredigest = md5_underscore2.digest();
        }
        int value;
        value = ((md5_underscore1_underscoredigest[0] & 0xff) << 16) | ((md5_underscore1_underscoredigest[6] & 0xff) << 8) | (md5_underscore1_underscoredigest[12] & 0xff);
        output.append(cryptTo64(value, 4));
        value = ((md5_underscore1_underscoredigest[1] & 0xff) << 16) | ((md5_underscore1_underscoredigest[7] & 0xff) << 8) | (md5_underscore1_underscoredigest[13] & 0xff);
        output.append(cryptTo64(value, 4));
        value = ((md5_underscore1_underscoredigest[2] & 0xff) << 16) | ((md5_underscore1_underscoredigest[8] & 0xff) << 8) | (md5_underscore1_underscoredigest[14] & 0xff);
        output.append(cryptTo64(value, 4));
        value = ((md5_underscore1_underscoredigest[3] & 0xff) << 16) | ((md5_underscore1_underscoredigest[9] & 0xff) << 8) | (md5_underscore1_underscoredigest[15] & 0xff);
        output.append(cryptTo64(value, 4));
        value = ((md5_underscore1_underscoredigest[4] & 0xff) << 16) | ((md5_underscore1_underscoredigest[10] & 0xff) << 8) | (md5_underscore1_underscoredigest[5] & 0xff);
        output.append(cryptTo64(value, 4));
        value = md5_underscore1_underscoredigest[11] & 0xff;
        output.append(cryptTo64(value, 2));
        md5_underscore1 = null;
        md5_underscore2 = null;
        md5_underscore1_underscoredigest = null;
        md5_underscore2_underscoredigest = null;
        pwBytes = null;
        saltBytes = null;
        password = salt = null;
        return output.toString();
    }

