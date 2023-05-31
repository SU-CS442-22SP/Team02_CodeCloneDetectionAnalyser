        public boolean check(Object credentials) {
            try {
                byte[] digest = null;
                if (credentials instanceof Password || credentials instanceof String) {
                    synchronized (_underscore_underscoreTYPE) {
                        if (_underscore_underscoremd == null) _underscore_underscoremd = MessageDigest.getInstance("MD5");
                        _underscore_underscoremd.reset();
                        _underscore_underscoremd.update(credentials.toString().getBytes(StringUtil._underscore_underscoreISO_underscore8859_underscore1));
                        digest = _underscore_underscoremd.digest();
                    }
                    if (digest == null || digest.length != _underscoredigest.length) return false;
                    for (int i = 0; i < digest.length; i++) if (digest[i] != _underscoredigest[i]) return false;
                    return true;
                } else if (credentials instanceof MD5) {
                    MD5 md5 = (MD5) credentials;
                    if (_underscoredigest.length != md5._underscoredigest.length) return false;
                    for (int i = 0; i < _underscoredigest.length; i++) if (_underscoredigest[i] != md5._underscoredigest[i]) return false;
                    return true;
                } else if (credentials instanceof Credential) {
                    return ((Credential) credentials).check(this);
                } else {
                    log.warn("Can't check " + credentials.getClass() + " against MD5");
                    return false;
                }
            } catch (Exception e) {
                log.warn(LogSupport.EXCEPTION, e);
                return false;
            }
        }

