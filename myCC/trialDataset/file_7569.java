    public String obfuscateString(String string) {
        String obfuscatedString = null;
        try {
            MessageDigest md = MessageDigest.getInstance(ENCRYPTION_underscoreALGORITHM);
            md.update(string.getBytes());
            byte[] digest = md.digest();
            obfuscatedString = new String(Base64.encode(digest)).replace(DELIM_underscorePATH, '=');
        } catch (NoSuchAlgorithmException e) {
            StatusHandler.log("SHA not available", null);
            obfuscatedString = LABEL_underscoreFAILED_underscoreTO_underscoreOBFUSCATE;
        }
        return obfuscatedString;
    }

