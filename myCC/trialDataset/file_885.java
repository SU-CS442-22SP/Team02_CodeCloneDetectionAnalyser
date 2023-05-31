    public String digest(String password, String digestType, String inputEncoding) throws CmsPasswordEncryptionException {
        MessageDigest md;
        String result;
        try {
            if (DIGEST_underscoreTYPE_underscorePLAIN.equals(digestType.toLowerCase())) {
                result = password;
            } else if (DIGEST_underscoreTYPE_underscoreSSHA.equals(digestType.toLowerCase())) {
                byte[] salt = new byte[4];
                byte[] digest;
                byte[] total;
                if (m_underscoresecureRandom == null) {
                    m_underscoresecureRandom = SecureRandom.getInstance("SHA1PRNG");
                }
                m_underscoresecureRandom.nextBytes(salt);
                md = MessageDigest.getInstance(DIGEST_underscoreTYPE_underscoreSHA);
                md.reset();
                md.update(password.getBytes(inputEncoding));
                md.update(salt);
                digest = md.digest();
                total = new byte[digest.length + salt.length];
                System.arraycopy(digest, 0, total, 0, digest.length);
                System.arraycopy(salt, 0, total, digest.length, salt.length);
                result = new String(Base64.encodeBase64(total));
            } else {
                md = MessageDigest.getInstance(digestType);
                md.reset();
                md.update(password.getBytes(inputEncoding));
                result = new String(Base64.encodeBase64(md.digest()));
            }
        } catch (NoSuchAlgorithmException e) {
            CmsMessageContainer message = Messages.get().container(Messages.ERR_underscoreUNSUPPORTED_underscoreALGORITHM_underscore1, digestType);
            if (LOG.isErrorEnabled()) {
                LOG.error(message.key(), e);
            }
            throw new CmsPasswordEncryptionException(message, e);
        } catch (UnsupportedEncodingException e) {
            CmsMessageContainer message = Messages.get().container(Messages.ERR_underscoreUNSUPPORTED_underscorePASSWORD_underscoreENCODING_underscore1, inputEncoding);
            if (LOG.isErrorEnabled()) {
                LOG.error(message.key(), e);
            }
            throw new CmsPasswordEncryptionException(message, e);
        }
        return result;
    }

