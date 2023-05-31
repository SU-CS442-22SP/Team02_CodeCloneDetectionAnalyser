    public void newGuidSeed(boolean secure) {
        SecureRandom sr = new SecureRandom();
        long secureInitializer = sr.nextLong();
        Random rand = new Random(secureInitializer);
        String host_underscoreip = "";
        try {
            host_underscoreip = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException err) {
            err.printStackTrace();
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException err) {
            err.printStackTrace();
        }
        try {
            long time = System.currentTimeMillis();
            long randNumber = 0;
            if (secure) {
                randNumber = sr.nextLong();
            } else {
                randNumber = rand.nextLong();
            }
            sbBeforeMd5.append(host_underscoreip);
            sbBeforeMd5.append(":");
            sbBeforeMd5.append(Long.toString(time));
            sbBeforeMd5.append(":");
            sbBeforeMd5.append(Long.toString(randNumber));
            seed = sbBeforeMd5.toString();
            md5.update(seed.getBytes());
            byte[] array = md5.digest();
            StringBuffer temp_underscoresb = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                int b = array[i] & 0xFF;
                if (b < 0x10) temp_underscoresb.append('0');
                temp_underscoresb.append(Integer.toHexString(b));
            }
            rawGUID = temp_underscoresb.toString();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

