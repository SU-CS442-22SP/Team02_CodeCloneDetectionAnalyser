    public String calcMD5(String sequence) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(sequence.toString().toUpperCase().getBytes());
        BigInteger md5hash = new BigInteger(1, md5.digest());
        String sequence_underscoremd5 = md5hash.toString(16);
        while (sequence_underscoremd5.length() < 32) {
            sequence_underscoremd5 = "0" + sequence_underscoremd5;
        }
        return sequence_underscoremd5;
    }

