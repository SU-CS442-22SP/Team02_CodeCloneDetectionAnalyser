    public Blowfish(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA1");
            digest.update(password.getBytes());
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
        m_underscorebfish = new BlowfishCBC(digest.digest(), 0);
        digest.reset();
    }

