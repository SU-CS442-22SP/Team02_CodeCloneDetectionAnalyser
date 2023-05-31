    private boolean passwordMatches(String user, String plainPassword, String scrambledPassword) {
        MessageDigest md;
        byte[] temp_underscoredigest, pass_underscoredigest;
        byte[] hex_underscoredigest = new byte[35];
        byte[] scrambled = scrambledPassword.getBytes();
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainPassword.getBytes("US-ASCII"));
            md.update(user.getBytes("US-ASCII"));
            temp_underscoredigest = md.digest();
            Utils.bytesToHex(temp_underscoredigest, hex_underscoredigest, 0);
            md.update(hex_underscoredigest, 0, 32);
            md.update(salt.getBytes());
            pass_underscoredigest = md.digest();
            Utils.bytesToHex(pass_underscoredigest, hex_underscoredigest, 3);
            hex_underscoredigest[0] = (byte) 'm';
            hex_underscoredigest[1] = (byte) 'd';
            hex_underscoredigest[2] = (byte) '5';
            for (int i = 0; i < hex_underscoredigest.length; i++) {
                if (scrambled[i] != hex_underscoredigest[i]) {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return true;
    }

