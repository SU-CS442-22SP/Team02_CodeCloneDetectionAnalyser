    public static SOAPMessage call(SOAPMessage request, URL url) throws IOException, SOAPException {
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();
        request.writeTo(conn.getOutputStream());
        MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_underscore1_underscore2_underscorePROTOCOL);
        return mf.createMessage(null, conn.getInputStream());
    }

