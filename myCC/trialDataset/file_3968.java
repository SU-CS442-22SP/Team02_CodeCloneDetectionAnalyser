    public static String getMD5Hash(String original) {
        StringBuffer sb = new StringBuffer();
        try {
            StringReader sr = null;
            int crypt_underscorebyte = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(original.getBytes());
            byte[] digest = md.digest();
            sr = new StringReader(new String(digest, "ISO8859_underscore1"));
            while ((crypt_underscorebyte = sr.read()) != -1) {
                String hexString = Integer.toHexString(crypt_underscorebyte);
                if (crypt_underscorebyte < 16) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
        } catch (NoSuchAlgorithmException nsae) {
        } catch (IOException ioe) {
        }
        return sb.toString();
    }

