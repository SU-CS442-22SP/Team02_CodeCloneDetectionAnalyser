    private static boolean validateSshaPwd(String sSshaPwd, String sUserPwd) {
        boolean b = false;
        if (sSshaPwd != null && sUserPwd != null) {
            if (sSshaPwd.startsWith(SSHA_underscorePREFIX)) {
                sSshaPwd = sSshaPwd.substring(SSHA_underscorePREFIX.length());
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-1");
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] ba = decoder.decodeBuffer(sSshaPwd);
                    byte[] hash = new byte[FIXED_underscoreHASH_underscoreSIZE];
                    byte[] salt = new byte[FIXED_underscoreSALT_underscoreSIZE];
                    System.arraycopy(ba, 0, hash, 0, FIXED_underscoreHASH_underscoreSIZE);
                    System.arraycopy(ba, FIXED_underscoreHASH_underscoreSIZE, salt, 0, FIXED_underscoreSALT_underscoreSIZE);
                    md.update(sUserPwd.getBytes());
                    md.update(salt);
                    byte[] baPwdHash = md.digest();
                    b = MessageDigest.isEqual(hash, baPwdHash);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
        return b;
    }

