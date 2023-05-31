        public static String digest(String password) {
            try {
                byte[] digest;
                synchronized (_underscore_underscoremd5Lock) {
                    if (_underscore_underscoremd == null) {
                        try {
                            _underscore_underscoremd = MessageDigest.getInstance("MD5");
                        } catch (Exception e) {
                            Log.warn(e);
                            return null;
                        }
                    }
                    _underscore_underscoremd.reset();
                    _underscore_underscoremd.update(password.getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                    digest = _underscore_underscoremd.digest();
                }
                return _underscore_underscoreTYPE + TypeUtil.toString(digest, 16);
            } catch (Exception e) {
                Log.warn(e);
                return null;
            }
        }

