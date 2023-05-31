    public static String digestString(String data, String algorithm) {
        String result = null;
        if (data != null) {
            try {
                MessageDigest _underscoremd = MessageDigest.getInstance(algorithm);
                _underscoremd.update(data.getBytes());
                byte[] _underscoredigest = _underscoremd.digest();
                String _underscoreds;
                _underscoreds = toHexString(_underscoredigest, 0, _underscoredigest.length);
                result = _underscoreds;
            } catch (NoSuchAlgorithmException e) {
                result = null;
            }
        }
        return result;
    }

