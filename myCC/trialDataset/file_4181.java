    public ByteBuffer[] write(ByteBuffer[] byteBuffers) {
        if (!m_underscoresslInitiated) {
            return m_underscorewriter.write(byteBuffers);
        }
        if (m_underscoreengine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_underscoreHANDSHAKING) {
            if (!NIOUtils.isEmpty(byteBuffers)) {
                m_underscoreinitialOutBuffer = NIOUtils.concat(m_underscoreinitialOutBuffer, m_underscorewriter.write(byteBuffers));
                byteBuffers = new ByteBuffer[0];
            }
            ByteBuffer buffer = SSL_underscoreBUFFER.get();
            ByteBuffer[] buffers = null;
            try {
                SSLEngineResult result = null;
                while (m_underscoreengine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_underscoreWRAP) {
                    buffer.clear();
                    result = m_underscoreengine.wrap(byteBuffers, buffer);
                    buffer.flip();
                    buffers = NIOUtils.concat(buffers, NIOUtils.copy(buffer));
                }
                if (result == null) return null;
                if (result.getStatus() != SSLEngineResult.Status.OK) throw new SSLException("Unexpectedly not ok wrapping handshake data, was " + result.getStatus());
                reactToHandshakeStatus(result.getHandshakeStatus());
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }
            return buffers;
        }
        ByteBuffer buffer = SSL_underscoreBUFFER.get();
        buffer.clear();
        if (NIOUtils.isEmpty(byteBuffers)) {
            if (m_underscoreinitialOutBuffer == null) return null;
        } else {
            byteBuffers = m_underscorewriter.write(byteBuffers);
        }
        if (m_underscoreinitialOutBuffer != null) {
            byteBuffers = NIOUtils.concat(m_underscoreinitialOutBuffer, byteBuffers);
            m_underscoreinitialOutBuffer = null;
        }
        ByteBuffer[] encrypted = null;
        while (!NIOUtils.isEmpty(byteBuffers)) {
            buffer.clear();
            try {
                m_underscoreengine.wrap(byteBuffers, buffer);
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }
            buffer.flip();
            encrypted = NIOUtils.concat(encrypted, NIOUtils.copy(buffer));
        }
        return encrypted;
    }

