    public String encryptStringWithKey(String to_underscorebe_underscoreencrypted, String aKey) {
        String encrypted_underscorevalue = "";
        char xdigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException exc) {
            globalErrorDictionary.takeValueForKey(("Security package does not contain appropriate algorithm"), ("Security package does not contain appropriate algorithm"));
            log.error("Security package does not contain appropriate algorithm");
            return encrypted_underscorevalue;
        }
        if (to_underscorebe_underscoreencrypted != null) {
            byte digest[];
            byte fudge_underscoreconstant[];
            try {
                fudge_underscoreconstant = ("X#@!").getBytes("UTF8");
            } catch (UnsupportedEncodingException uee) {
                fudge_underscoreconstant = ("X#@!").getBytes();
            }
            byte fudgetoo_underscorepart[] = { (byte) xdigit[(int) (MSiteConfig.myrand() % 16)], (byte) xdigit[(int) (MSiteConfig.myrand() % 16)], (byte) xdigit[(int) (MSiteConfig.myrand() % 16)], (byte) xdigit[(int) (MSiteConfig.myrand() % 16)] };
            int i = 0;
            if (aKey != null) {
                try {
                    fudgetoo_underscorepart = aKey.getBytes("UTF8");
                } catch (UnsupportedEncodingException uee) {
                    fudgetoo_underscorepart = aKey.getBytes();
                }
            }
            messageDigest.update(fudge_underscoreconstant);
            try {
                messageDigest.update(to_underscorebe_underscoreencrypted.getBytes("UTF8"));
            } catch (UnsupportedEncodingException uee) {
                messageDigest.update(to_underscorebe_underscoreencrypted.getBytes());
            }
            messageDigest.update(fudgetoo_underscorepart);
            digest = messageDigest.digest();
            encrypted_underscorevalue = new String(fudgetoo_underscorepart);
            for (i = 0; i < digest.length; i++) {
                int mashed;
                char temp[] = new char[2];
                if (digest[i] < 0) {
                    mashed = 127 + (-1 * digest[i]);
                } else {
                    mashed = digest[i];
                }
                temp[0] = xdigit[mashed / 16];
                temp[1] = xdigit[mashed % 16];
                encrypted_underscorevalue = encrypted_underscorevalue + (new String(temp));
            }
        }
        return encrypted_underscorevalue;
    }

