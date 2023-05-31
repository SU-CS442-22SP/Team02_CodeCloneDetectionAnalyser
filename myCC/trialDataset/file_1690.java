    static boolean generateKey() throws NoSuchAlgorithmException {
        java.util.Random rand = new Random(reg_underscorename.hashCode() + System.currentTimeMillis());
        DecimalFormat vf = new DecimalFormat("000");
        ccKey = keyProduct + FIELD_underscoreSEPERATOR + keyType + FIELD_underscoreSEPERATOR + keyQuantity + FIELD_underscoreSEPERATOR + vf.format(lowMajorVersion) + FIELD_underscoreSEPERATOR + vf.format(lowMinorVersion) + FIELD_underscoreSEPERATOR + vf.format(highMajorVersion) + FIELD_underscoreSEPERATOR + vf.format(highMinorVersion) + FIELD_underscoreSEPERATOR + reg_underscorename + FIELD_underscoreSEPERATOR + Integer.toHexString(rand.nextInt()).toUpperCase();
        byte[] md5;
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        md.update(ccKey.getBytes());
        md.update(FIELD_underscoreSEPERATOR.getBytes());
        md.update(zuonicsPassword.getBytes());
        md5 = md.digest();
        userKey = ccKey + FIELD_underscoreSEPERATOR;
        for (int i = 0; i < md5.length; i++) userKey += Integer.toHexString(md5[i]).toUpperCase();
        return true;
    }

