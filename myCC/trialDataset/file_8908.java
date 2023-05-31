    public void parseFile(String dataurl, URL documentBase) {
        DataInputStream in;
        if (_underscoredebug > 2) System.out.println("PlotBox: parseFile(" + dataurl + " " + documentBase + ") _underscoredataurl = " + _underscoredataurl + " " + _underscoredocumentBase);
        if (dataurl == null || dataurl.length() == 0) {
            in = new DataInputStream(System.in);
        } else {
            try {
                URL url;
                if (documentBase == null && _underscoredocumentBase != null) {
                    documentBase = _underscoredocumentBase;
                }
                if (documentBase == null) {
                    url = new URL(_underscoredataurl);
                } else {
                    try {
                        url = new URL(documentBase, dataurl);
                    } catch (NullPointerException e) {
                        url = new URL(_underscoredataurl);
                    }
                }
                in = new DataInputStream(url.openStream());
            } catch (MalformedURLException e) {
                try {
                    in = new DataInputStream(new FileInputStream(dataurl));
                } catch (FileNotFoundException me) {
                    _underscoreerrorMsg = new String[2];
                    _underscoreerrorMsg[0] = "File not found: " + dataurl;
                    _underscoreerrorMsg[1] = me.getMessage();
                    return;
                } catch (SecurityException me) {
                    _underscoreerrorMsg = new String[2];
                    _underscoreerrorMsg[0] = "Security Exception: " + dataurl;
                    _underscoreerrorMsg[1] = me.getMessage();
                    return;
                }
            } catch (IOException ioe) {
                _underscoreerrorMsg = new String[2];
                _underscoreerrorMsg[0] = "Failure opening URL: " + dataurl;
                _underscoreerrorMsg[1] = ioe.getMessage();
                return;
            }
        }
        _underscorenewFile();
        try {
            if (_underscorebinary) {
                _underscoreparseBinaryStream(in);
            } else {
                String line = in.readLine();
                while (line != null) {
                    _underscoreparseLine(line);
                    line = in.readLine();
                }
            }
        } catch (MalformedURLException e) {
            _underscoreerrorMsg = new String[2];
            _underscoreerrorMsg[0] = "Malformed URL: " + dataurl;
            _underscoreerrorMsg[1] = e.getMessage();
            return;
        } catch (IOException e) {
            _underscoreerrorMsg = new String[2];
            _underscoreerrorMsg[0] = "Failure reading data: " + dataurl;
            _underscoreerrorMsg[1] = e.getMessage();
        } catch (PlotDataException e) {
            _underscoreerrorMsg = new String[2];
            _underscoreerrorMsg[0] = "Incorrectly formatted plot data in " + dataurl;
            _underscoreerrorMsg[1] = e.getMessage();
        } finally {
            try {
                in.close();
            } catch (IOException me) {
            }
        }
    }

