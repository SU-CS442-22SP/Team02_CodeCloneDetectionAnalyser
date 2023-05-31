public void parseFile(String filespec, URL documentBase) {
        DataInputStream in = null;
        if (filespec == null || filespec.length() == 0) {
            in = new DataInputStream(System.in);
        } else {
            try {
                URL url = null;
                if (documentBase == null && _underscoredocumentBase != null) {
                    documentBase = _underscoredocumentBase;
                }
                if (documentBase == null) {
                    url = new URL(filespec);
                } else {
                    try {
                        url = new URL(documentBase, filespec);
                    } catch (NullPointerException e) {
                        url = new URL(filespec);
                    }
                }
                in = new DataInputStream(url.openStream());
            } catch (MalformedURLException e) {
                try {
                    in = new DataInputStream(new FileInputStream(filespec));
                } catch (FileNotFoundException me) {
                    _underscoreerrorMsg = new String[2];
                    _underscoreerrorMsg[0] = "File not found: " + filespec;
                    _underscoreerrorMsg[1] = me.getMessage();
                    return;
                } catch (SecurityException me) {
                    _underscoreerrorMsg = new String[2];
                    _underscoreerrorMsg[0] = "Security Exception: " + filespec;
                    _underscoreerrorMsg[1] = me.getMessage();
                    return;
                }
            } catch (IOException ioe) {
                _underscoreerrorMsg = new String[3];
                _underscoreerrorMsg[0] = "Failure opening URL: ";
                _underscoreerrorMsg[1] = " " + filespec;
                _underscoreerrorMsg[2] = ioe.getMessage();
                return;
            }
        }
        try {
            BufferedReader din = new BufferedReader(new InputStreamReader(in));
            String line = din.readLine();
            while (line != null) {
                _underscoreparseLine(line);
                line = din.readLine();
            }
        } catch (MalformedURLException e) {
            _underscoreerrorMsg = new String[2];
            _underscoreerrorMsg[0] = "Malformed URL: " + filespec;
            _underscoreerrorMsg[1] = e.getMessage();
            return;
        } catch (IOException e) {
            _underscoreerrorMsg = new String[2];
            _underscoreerrorMsg[0] = "Failure reading data: " + filespec;
            _underscoreerrorMsg[1] = e.getMessage();
            _underscoreerrorMsg[1] = e.getMessage();
        } finally {
            try {
                in.close();
            } catch (IOException me) {
            }
        }
    }
