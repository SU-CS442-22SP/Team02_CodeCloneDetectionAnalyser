    private void handleServerIntroduction(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        String version = iter.nextString();
        int serverReportedUDPPort = iter.nextUByte2();
        _underscoreauthKey = iter.nextUByte4();
        _underscoreintroKey = iter.nextUByte4();
        _underscoreclientKey = makeClientKey(_underscoreauthKey, _underscoreintroKey);
        String passwordKey = iter.nextString();
        _underscorelogger.log(Level.INFO, "Connection to version " + version + " with udp port " + serverReportedUDPPort);
        DataPacket packet = null;
        if (initUDPSocketAndStartPacketReader(_underscoretcpSocket.getInetAddress(), serverReportedUDPPort)) {
            ParameterBuilder builder = new ParameterBuilder();
            builder.appendUByte2(_underscoreudpSocket.getLocalPort());
            builder.appendString(_underscoreuser);
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ignore) {
            }
            md5.update(_underscoreserverKey.getBytes());
            md5.update(passwordKey.getBytes());
            md5.update(_underscorepassword.getBytes());
            for (byte b : md5.digest()) {
                builder.appendByte(b);
            }
            packet = new DataPacketImpl(ClientCommandConstants.INTRODUCTION, builder.toParameter());
        } else {
            packet = new DataPacketImpl(ClientCommandConstants.TCP_underscoreONLY);
        }
        sendTCPPacket(packet);
    }

