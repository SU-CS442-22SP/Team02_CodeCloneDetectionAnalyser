        public boolean check(Object credentials) {
            String password = (credentials instanceof String) ? (String) credentials : credentials.toString();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] ha1;
                if (credentials instanceof Credential.MD5) {
                    ha1 = ((Credential.MD5) credentials).getDigest();
                } else {
                    md.update(username.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                    md.update((byte) ':');
                    md.update(realm.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                    md.update((byte) ':');
                    md.update(password.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                    ha1 = md.digest();
                }
                md.reset();
                md.update(method.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                md.update((byte) ':');
                md.update(uri.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                byte[] ha2 = md.digest();
                md.update(TypeUtil.toString(ha1, 16).getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                md.update((byte) ':');
                md.update(nonce.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                md.update((byte) ':');
                md.update(nc.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                md.update((byte) ':');
                md.update(cnonce.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                md.update((byte) ':');
                md.update(qop.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                md.update((byte) ':');
                md.update(TypeUtil.toString(ha2, 16).getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                byte[] digest = md.digest();
                return (TypeUtil.toString(digest, 16).equalsIgnoreCase(response));
            } catch (Exception e) {
                Log.warn(e);
            }
            return false;
        }

