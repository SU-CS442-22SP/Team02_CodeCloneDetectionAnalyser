    @SuppressWarnings("unused")
    private static int chkPasswd(final String sInputPwd, final String sSshaPwd) {
        assert sInputPwd != null;
        assert sSshaPwd != null;
        int r = ERR_underscoreLOGIN_underscoreACCOUNT;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] ba = decoder.decodeBuffer(sSshaPwd);
            assert ba.length >= FIXED_underscoreHASH_underscoreSIZE;
            byte[] hash = new byte[FIXED_underscoreHASH_underscoreSIZE];
            byte[] salt = new byte[FIXED_underscoreSALT_underscoreSIZE];
            System.arraycopy(ba, 0, hash, 0, FIXED_underscoreHASH_underscoreSIZE);
            System.arraycopy(ba, FIXED_underscoreHASH_underscoreSIZE, salt, 0, FIXED_underscoreSALT_underscoreSIZE);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(sInputPwd.getBytes());
            md.update(salt);
            byte[] baPwdHash = md.digest();
            if (MessageDigest.isEqual(hash, baPwdHash)) {
                r = ERR_underscoreLOGIN_underscoreOK;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return r;
    }

