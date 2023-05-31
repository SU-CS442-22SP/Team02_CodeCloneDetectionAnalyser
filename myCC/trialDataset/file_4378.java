    private void auth() throws IOException {
        authorized = false;
        seqNumber = 0;
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(UDPHID_underscoreDEFAULT_underscoreTIMEOUT);
        ds.connect(addr, port);
        DatagramPacket p = new DatagramPacket(buffer.array(), buffer.capacity());
        for (int i = 0; i < UDPHID_underscoreDEFAULT_underscoreATTEMPTS; i++) {
            buffer.clear();
            buffer.put((byte) REQ_underscoreCHALLENGE);
            buffer.put(htons((short) UDPHID_underscorePROTO));
            buffer.put(name.getBytes());
            ds.send(new DatagramPacket(buffer.array(), buffer.position()));
            buffer.clear();
            try {
                ds.receive(p);
            } catch (SocketTimeoutException e) {
                continue;
            }
            switch(buffer.get()) {
                case ANS_underscoreCHALLENGE:
                    break;
                case ANS_underscoreFAILURE:
                    throw new IOException("REQ_underscoreFAILURE");
                default:
                    throw new IOException("invalid packet");
            }
            byte challenge_underscoreid = buffer.get();
            int challenge_underscorelen = (int) buffer.get();
            byte[] challenge = new byte[challenge_underscorelen];
            buffer.get(challenge, 0, p.getLength() - buffer.position());
            byte[] response;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(challenge_underscoreid);
                md.update(password.getBytes(), 0, password.length());
                md.update(challenge, 0, challenge.length);
                response = md.digest();
            } catch (NoSuchAlgorithmException e) {
                throw new IOException("NoSuchAlgorithmException: " + e.toString());
            }
            buffer.clear();
            buffer.put((byte) REQ_underscoreRESPONSE);
            buffer.put(challenge_underscoreid);
            buffer.put((byte) response.length);
            buffer.put(response);
            buffer.put(login.getBytes());
            ds.send(new DatagramPacket(buffer.array(), buffer.position()));
            buffer.clear();
            try {
                ds.receive(p);
            } catch (SocketTimeoutException e) {
                continue;
            }
            switch(buffer.get()) {
                case ANS_underscoreSUCCESS:
                    int sidLength = buffer.get();
                    sid = new byte[sidLength];
                    buffer.get(sid, 0, sidLength);
                    authorized = true;
                    return;
                case ANS_underscoreFAILURE:
                    throw new IOException("access deny");
                default:
                    throw new IOException("invalid packet");
            }
        }
        throw new IOException("operation time out");
    }

