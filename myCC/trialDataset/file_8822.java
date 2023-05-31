    public void createVendorSignature() {
        byte b;
        try {
            _underscorevendorMessageDigest = MessageDigest.getInstance("MD5");
            _underscorevendorSig = Signature.getInstance("MD5/RSA/PKCS#1");
            _underscorevendorSig.initSign((PrivateKey) _underscorevendorPrivateKey);
            _underscorevendorMessageDigest.update(getBankString().getBytes());
            _underscorevendorMessageDigestBytes = _underscorevendorMessageDigest.digest();
            _underscorevendorSig.update(_underscorevendorMessageDigestBytes);
            _underscorevendorSignatureBytes = _underscorevendorSig.sign();
        } catch (Exception e) {
        }
        ;
    }

