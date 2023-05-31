    @Override
    public int onPut(Operation operation) {
        synchronized (MuleObexRequestHandler.connections) {
            MuleObexRequestHandler.connections++;
            if (logger.isDebugEnabled()) {
                logger.debug("Connection accepted, total number of connections: " + MuleObexRequestHandler.connections);
            }
        }
        int result = ResponseCodes.OBEX_underscoreHTTP_underscoreOK;
        try {
            headers = operation.getReceivedHeaders();
            if (!this.maxFileSize.equals(ObexServer.UNLIMMITED_underscoreFILE_underscoreSIZE)) {
                Long fileSize = (Long) headers.getHeader(HeaderSet.LENGTH);
                if (fileSize == null) {
                    result = ResponseCodes.OBEX_underscoreHTTP_underscoreLENGTH_underscoreREQUIRED;
                }
                if (fileSize > this.maxFileSize) {
                    result = ResponseCodes.OBEX_underscoreHTTP_underscoreREQ_underscoreTOO_underscoreLARGE;
                }
            }
            if (result != ResponseCodes.OBEX_underscoreHTTP_underscoreOK) {
                InputStream in = operation.openInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copy(in, out);
                in.close();
                out.close();
                data = out.toByteArray();
                if (interrupted) {
                    data = null;
                    result = ResponseCodes.OBEX_underscoreHTTP_underscoreGONE;
                }
            }
            return result;
        } catch (IOException e) {
            return ResponseCodes.OBEX_underscoreHTTP_underscoreUNAVAILABLE;
        } finally {
            synchronized (this) {
                this.notify();
            }
            synchronized (MuleObexRequestHandler.connections) {
                MuleObexRequestHandler.connections--;
                if (logger.isDebugEnabled()) {
                    logger.debug("Connection closed, total number of connections: " + MuleObexRequestHandler.connections);
                }
            }
        }
    }

