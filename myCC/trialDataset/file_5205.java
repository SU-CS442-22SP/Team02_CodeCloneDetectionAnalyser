    protected byte[] generateHashBytes() {
        String s = createString(false);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsa) {
            System.out.println("Can't get MD5 implementation " + nsa);
            throw new RuntimeException("DynanmicAddress2: Can't get MD5 implementation");
        }
        if (m_underscorekey != null) md.update(m_underscorekey.getBytes(), 0, m_underscorekey.length());
        md.update(s.getBytes(), 0, s.length());
        byte[] hash = md.digest();
        return hash;
    }

