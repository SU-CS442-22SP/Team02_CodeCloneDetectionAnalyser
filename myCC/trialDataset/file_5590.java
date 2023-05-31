    public void requestConfirm() throws Exception {
        if (!this._underscorec.checkProperty("directory.request", "request")) {
            throw new Exception("product has no active request");
        }
        if (!new File(WBSAgnitioConfiguration.getHARequestFile()).canWrite()) {
            throw new Exception("cannot remove request from system");
        }
        HashMap<String, String> values = getValues(WBSAgnitioConfiguration.getHARequestFile());
        if (!values.containsKey("address.virtual")) {
            throw new Exception("failed to determine the virtual address");
        }
        if (!values.containsKey("address.real")) {
            throw new Exception("failed to determine the remote address");
        }
        HTTPClient _underscorehc = new HTTPClient(values.get("address.real"));
        if (TomcatConfiguration.checkHTTPS()) {
            _underscorehc.setSecure(true);
        }
        _underscorehc.load("/admin/Comm?type=" + CommResponse.TYPE_underscoreHA + "&command=" + CommResponse.COMMAND_underscoreREQUEST_underscoreCONFIRM + "&virtual=" + values.get("address.virtual"));
        String _underscorereply = new String(_underscorehc.getContent());
        if (_underscorereply.isEmpty()) {
            throw new Exception("remote product has not sent any reply");
        } else if (_underscorereply.indexOf("done") == -1) {
            throw new Exception(_underscorereply);
        }
        HAConfiguration.setSlave(values.get("address.virtual"), values.get("address.real"));
        File _underscoref = new File(WBSAgnitioConfiguration.getOptionalSchemaRequestFile());
        if (_underscoref.exists()) {
            FileOutputStream _underscorefos = new FileOutputStream(WBSAgnitioConfiguration.getOptionalSchemaFile());
            FileInputStream _underscorefis = new FileInputStream(_underscoref);
            while (_underscorefis.available() > 0) {
                _underscorefos.write(_underscorefis.read());
            }
            _underscorefis.close();
            _underscorefos.close();
            _underscoref.delete();
        }
        _underscoref = new File(WBSAgnitioConfiguration.getSchemaObjectRequestFile());
        if (_underscoref.exists()) {
            FileOutputStream _underscorefos = new FileOutputStream(WBSAgnitioConfiguration.getSchemaObjectFile());
            FileInputStream _underscorefis = new FileInputStream(_underscoref);
            while (_underscorefis.available() > 0) {
                _underscorefos.write(_underscorefis.read());
            }
            _underscorefis.close();
            _underscorefos.close();
            _underscoref.delete();
        }
        new File(WBSAgnitioConfiguration.getHARequestFile()).delete();
        this._underscorec.removeProperty("directory.request");
        this._underscorec.setProperty("directory.virtual", values.get("address.virtual"));
        this._underscorec.setProperty("directory.status", "slave");
        this._underscorec.store();
    }

