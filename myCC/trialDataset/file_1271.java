    public void createBankSignature() {
        byte b;
        try {
            _underscorebankMessageDigest = MessageDigest.getInstance("MD5");
            _underscorebankSig = Signature.getInstance("MD5/RSA/PKCS#1");
            _underscorebankSig.initSign((PrivateKey) _underscorebankPrivateKey);
            _underscorebankMessageDigest.update(getBankString().getBytes());
            _underscorebankMessageDigestBytes = _underscorebankMessageDigest.digest();
            _underscorebankSig.update(_underscorebankMessageDigestBytes);
            _underscorebankSignatureBytes = _underscorebankSig.sign();
        } catch (Exception e) {
        }
        ;
    }

