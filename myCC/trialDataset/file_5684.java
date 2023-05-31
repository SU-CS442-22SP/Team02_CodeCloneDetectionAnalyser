    public Blowfish(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA1");
            digest.update(password.getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }
        m_underscorebfish = new BlowfishCBC(digest.digest(), 0);
        digest.reset();
    }

