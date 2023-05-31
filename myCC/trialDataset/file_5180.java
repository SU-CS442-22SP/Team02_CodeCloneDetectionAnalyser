    public boolean open() {
        if (null == _underscoreuu) {
            try {
                if (null == url) return false; else {
                    if (null != this.query) this.url = constructQuery(url, this.query);
                    _underscoreuu = url.openConnection();
                    _underscoreuu.setAllowUserInteraction(false);
                    _underscoreuu.setDoInput(true);
                    if (null != super._underscoreloc) {
                        try {
                            _underscoreuu.setRequestProperty("Accept-Language", jsGet_underscorelocale());
                        } catch (JavaScriptException jsx) {
                        }
                    }
                    encoding = _underscoreuu.getContentEncoding();
                    bytesize = _underscoreuu.getContentLength();
                    mimetype = _underscoreuu.getContentType();
                    serviceDate = _underscoreuu.getDate();
                    contentExpires = _underscoreuu.getExpiration();
                    contentLastmod = _underscoreuu.getLastModified();
                    return true;
                }
            } catch (Exception exc) {
                close();
                return false;
            }
        } else return true;
    }

