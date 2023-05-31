    public Node external_underscoreopen_underscoreurl(Node startAt) throws Exception {
        if (inUse) {
            throw new InterpreterException(StdErrors.extend(StdErrors.Already_underscoreused, "File already open"));
        }
        inUse = true;
        startAt.isGoodArgsLength(false, 2);
        ExtURL url = new ExtURL(startAt.getSubNode(1, Node.TYPE_underscoreSTRING).getString());
        String protocol = url.getProtocol();
        String mode = null;
        Node props = null;
        Node datas = null;
        byte[] buffer = null;
        String old_underscorec = null;
        String old_underscorer = null;
        int max_underscorei = startAt.size() - 1;
        if (startAt.elementAt(max_underscorei).getSymbolicValue_underscoreundestructive().isVList()) {
            props = startAt.getSubNode(max_underscorei--, Node.TYPE_underscoreLIST);
        }
        int i_underscore = 2;
        if (i_underscore <= max_underscorei) {
            mode = startAt.getSubNode(i_underscore++, Node.TYPE_underscoreSTRING).getString().toUpperCase().trim();
            if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https")) {
                if (!(mode.equals("GET") || mode.equals("POST") || mode.equals("PUT"))) {
                    throw new InterpreterException(128010, "Unsupported request methode");
                }
            } else if (protocol.equalsIgnoreCase("ftp") || protocol.equalsIgnoreCase("file")) {
                if (!(mode.equalsIgnoreCase("r") || mode.equalsIgnoreCase("w"))) {
                    throw new InterpreterException(128015, "Unsupported access methode");
                }
            } else if (protocol.equalsIgnoreCase("jar") || protocol.equalsIgnoreCase("stdin")) {
                if (!(mode.equalsIgnoreCase("r"))) {
                    throw new InterpreterException(128015, "Unsupported access methode");
                }
            } else if (protocol.equalsIgnoreCase("tcp") || protocol.equalsIgnoreCase("ssl+tcp")) {
                if (!(mode.equalsIgnoreCase("rw"))) {
                    throw new InterpreterException(128015, "Unsupported access methode");
                }
            } else if (protocol.equalsIgnoreCase("stdout") || protocol.equalsIgnoreCase("stderr")) {
                if (!(mode.equalsIgnoreCase("w"))) {
                    throw new InterpreterException(128015, "Unsupported access methode");
                }
            } else {
                throw new InterpreterException(128011, "Unsupported protocol");
            }
        }
        if (i_underscore <= max_underscorei) {
            if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https")) {
                throw new InterpreterException(128016, "Unsupported request datas");
            }
            datas = startAt.getSubNode(i_underscore++, Node.TYPE_underscoreSTRING | Node.TYPE_underscoreOBJECT);
            if (datas.isVObject()) {
                Object obj = datas.getVObjectExternalInstance();
                if (External_underscoreBuffer.class.isInstance(obj)) {
                    Buffer bbuffer = ((External_underscoreBuffer) obj).getBuffer();
                    buffer = bbuffer.read_underscorebytes();
                } else {
                    throw new InterpreterException(StdErrors.extend(StdErrors.Invalid_underscoreparameter, "Object (" + obj.getClass().getName() + ") required " + External_underscoreBuffer.class.getName()));
                }
            } else {
                buffer = datas.getString().getBytes();
            }
        }
        if (datas != null && mode != null && mode.equals("GET")) {
            throw new InterpreterException(128012, "GET request with data body");
        }
        if (props != null && (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https"))) {
            throw new InterpreterException(128013, "Cannot handle header properties in request");
        }
        try {
            if (protocol.equalsIgnoreCase("file") && mode != null && mode.equalsIgnoreCase("w")) {
                File f = new File(url.toURI());
                outputStream = new FileOutputStream(f);
                outputBuffer = new BufferedOutputStream(outputStream);
                output = new DataOutputStream(outputBuffer);
            } else if (protocol.equalsIgnoreCase("tcp")) {
                tcpHost = url.getHost();
                tcpPort = url.getPort();
                if (tcpPort < 0 || tcpPort > 65535) {
                    throw new InterpreterException(StdErrors.extend(StdErrors.Out_underscoreof_underscorerange, "" + tcpPort));
                }
                socket = new Socket(tcpHost, tcpPort);
                if (readTimeOut > 0) {
                    socket.setSoTimeout(readTimeOut);
                }
                inputStream = socket.getInputStream();
                inputBuffer = new BufferedInputStream(inputStream);
                input = new DataInputStream(inputBuffer);
                outputStream = socket.getOutputStream();
                outputBuffer = new BufferedOutputStream(outputStream);
                output = new DataOutputStream(outputBuffer);
            } else if (protocol.equalsIgnoreCase("ssl+tcp")) {
                tcpHost = url.getHost();
                tcpPort = url.getPort();
                if (tcpPort < 0 || tcpPort > 65535) {
                    throw new InterpreterException(StdErrors.extend(StdErrors.Out_underscoreof_underscorerange, "" + tcpPort));
                }
                SocketFactory socketFactory = SSLSocketFactory.getDefault();
                socket = socketFactory.createSocket(tcpHost, tcpPort);
                if (readTimeOut > 0) {
                    socket.setSoTimeout(readTimeOut);
                }
                inputStream = socket.getInputStream();
                inputBuffer = new BufferedInputStream(inputStream);
                input = new DataInputStream(inputBuffer);
                outputStream = socket.getOutputStream();
                outputBuffer = new BufferedOutputStream(outputStream);
                output = new DataOutputStream(outputBuffer);
            } else if (protocol.equalsIgnoreCase("stdout")) {
                setBufOut(System.out);
            } else if (protocol.equalsIgnoreCase("stderr")) {
                setBufOut(System.err);
            } else if (protocol.equalsIgnoreCase("stdin")) {
                setBufIn(System.in);
            } else {
                urlConnection = url.openConnection();
                if (connectTimeOut > 0) {
                    urlConnection.setConnectTimeout(connectTimeOut);
                }
                if (readTimeOut > 0) {
                    urlConnection.setReadTimeout(readTimeOut);
                }
                urlConnection.setUseCaches(false);
                urlConnection.setDoInput(true);
                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpCon = (HttpURLConnection) urlConnection;
                    if (props != null) {
                        for (int i = 0; i < props.size(); i++) {
                            Node pnode = props.getSubNode(i, Node.TYPE_underscoreDICO);
                            String header_underscores = Node.getPairKey(pnode);
                            String value_underscores = Node.node2VString(Node.getPairValue(pnode)).getString();
                            Interpreter.Log("   HTTP-Header: " + header_underscores + " : " + value_underscores);
                            httpCon.setRequestProperty(header_underscores, value_underscores);
                        }
                    }
                    if (mode != null && (mode.equals("POST") || mode.equals("PUT"))) {
                        if (mode.equals("PUT")) {
                            Interpreter.Log("   HTTP PUT: " + url.toString());
                        } else {
                            Interpreter.Log("   HTTP POST: " + url.toString());
                        }
                        urlConnection.setDoOutput(true);
                        httpCon.setRequestMethod(mode);
                        outputStream = urlConnection.getOutputStream();
                        outputBuffer = new BufferedOutputStream(outputStream);
                        output = new DataOutputStream(outputBuffer);
                        output.write(buffer);
                        output.flush();
                    }
                    inputStream = urlConnection.getInputStream();
                    inputBuffer = new BufferedInputStream(inputStream);
                    input = new DataInputStream(inputBuffer);
                } else {
                    if (mode == null || (mode != null && mode.equalsIgnoreCase("r"))) {
                        Interpreter.Log("   " + protocol + " read : " + url.toString());
                        inputStream = urlConnection.getInputStream();
                        inputBuffer = new BufferedInputStream(inputStream);
                        input = new DataInputStream(inputBuffer);
                    } else {
                        Interpreter.Log("   " + protocol + " write : " + url.toString());
                        outputStream = urlConnection.getOutputStream();
                        outputBuffer = new BufferedOutputStream(outputStream);
                        output = new DataOutputStream(outputBuffer);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        bytePos = 0;
        putHook();
        return null;
    }

