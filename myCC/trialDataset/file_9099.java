    public static byte[] crypt(String key, String salt) throws NoSuchAlgorithmException {
        int key_underscorelen = key.length();
        if (salt.startsWith(saltPrefix)) {
            salt = salt.substring(saltPrefix.length());
        }
        int salt_underscorelen = Math.min(getDollarLessLength(salt), 8);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(key.getBytes());
        md5.update(saltPrefix.getBytes());
        md5.update(salt.getBytes(), 0, salt_underscorelen);
        MessageDigest md5_underscorealt = MessageDigest.getInstance("MD5");
        md5_underscorealt.update(key.getBytes());
        md5_underscorealt.update(salt.getBytes(), 0, salt_underscorelen);
        md5_underscorealt.update(key.getBytes());
        byte[] altResult = md5_underscorealt.digest();
        int cnt;
        for (cnt = key_underscorelen; cnt > 16; cnt -= 16) {
            md5.update(altResult, 0, 16);
        }
        md5.update(altResult, 0, cnt);
        altResult[0] = 0;
        for (cnt = key_underscorelen; cnt > 0; cnt >>= 1) {
            md5.update(((cnt & 1) != 0) ? altResult : key.getBytes(), 0, 1);
        }
        altResult = md5.digest();
        for (cnt = 0; cnt < 1000; cnt++) {
            md5.reset();
            if ((cnt & 1) != 0) {
                md5.update(key.getBytes());
            } else {
                md5.update(altResult, 0, 16);
            }
            if ((cnt % 3) != 0) {
                md5.update(salt.getBytes(), 0, salt_underscorelen);
            }
            if ((cnt % 7) != 0) {
                md5.update(key.getBytes());
            }
            if ((cnt & 1) != 0) {
                md5.update(altResult, 0, 16);
            } else {
                md5.update(key.getBytes());
            }
            altResult = md5.digest();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(saltPrefix);
        sb.append(new String(salt.getBytes(), 0, salt_underscorelen));
        sb.append('$');
        sb.append(b64From24bit(altResult[0], altResult[6], altResult[12], 4));
        sb.append(b64From24bit(altResult[1], altResult[7], altResult[13], 4));
        sb.append(b64From24bit(altResult[2], altResult[8], altResult[14], 4));
        sb.append(b64From24bit(altResult[3], altResult[9], altResult[15], 4));
        sb.append(b64From24bit(altResult[4], altResult[10], altResult[5], 4));
        sb.append(b64From24bit((byte) 0, (byte) 0, altResult[11], 2));
        return sb.toString().getBytes();
    }

