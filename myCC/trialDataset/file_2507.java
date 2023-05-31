    @Override
    public String compute_underscorehash(String plaintext) {
        MessageDigest d;
        try {
            d = MessageDigest.getInstance(get_underscorealgorithm_underscorename());
            d.update(plaintext.getBytes());
            byte[] hash = d.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

