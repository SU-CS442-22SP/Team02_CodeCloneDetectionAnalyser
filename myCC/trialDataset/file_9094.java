    protected static String encodePassword(String raw_underscorepassword) throws DatabaseException {
        String clean_underscorepassword = validatePassword(raw_underscorepassword);
        try {
            MessageDigest md = MessageDigest.getInstance(DEFAULT_underscorePASSWORD_underscoreDIGEST);
            md.update(clean_underscorepassword.getBytes(DEFAULT_underscorePASSWORD_underscoreENCODING));
            String digest = new String(Base64.encodeBase64(md.digest()));
            if (log.isDebugEnabled()) log.debug("encodePassword: digest=" + digest);
            return digest;
        } catch (UnsupportedEncodingException e) {
            throw new DatabaseException("encoding-problem with password", e);
        } catch (NoSuchAlgorithmException e) {
            throw new DatabaseException("digest-problem encoding password", e);
        }
    }

