    public String digest(String algorithm, String text) {
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance(algorithm);
            digester.update(text.getBytes(Digester.ENCODING));
        } catch (NoSuchAlgorithmException nsae) {
            _underscorelog.error(nsae, nsae);
        } catch (UnsupportedEncodingException uee) {
            _underscorelog.error(uee, uee);
        }
        byte[] bytes = digester.digest();
        if (_underscoreBASE_underscore64) {
            return Base64.encode(bytes);
        } else {
            return new String(Hex.encodeHex(bytes));
        }
    }

