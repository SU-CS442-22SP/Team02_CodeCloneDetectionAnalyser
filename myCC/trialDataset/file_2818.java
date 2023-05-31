    public void sendMessage(String messageBufferName, String messageStr, String timeout) throws AppFabricException {
        MessageBufferUtil msgBufferUtilObj = new MessageBufferUtil(solutionName, TokenConstants.getSimpleAuthAuthenticationType());
        String requestUri = msgBufferUtilObj.getRequestUri();
        String sendPath = MessageBufferConstants.getPATH_underscoreFOR_underscoreSEND_underscoreMESSAGE();
        String timeOutParameter = MessageBufferConstants.getTIMEOUTPARAMETER();
        String messageBufferUri = msgBufferUtilObj.getMessageUri(messageBufferName, sendPath);
        String message = msgBufferUtilObj.getFormattedMessage(messageStr);
        String authorizationToken = "";
        try {
            ACSTokenProvider tp = new ACSTokenProvider(httpWebProxyServer_underscore, httpWebProxyPort_underscore, this.credentials);
            authorizationToken = tp.getACSToken(requestUri, messageBufferUri);
            messageBufferUri = messageBufferUri.replaceAll("http", "https");
            String sendUri = messageBufferUri + "?" + timeOutParameter + "=" + timeout;
            URL urlConn = new URL(sendUri);
            HttpURLConnection connection;
            if (httpWebProxy_underscore != null) connection = (HttpURLConnection) urlConn.openConnection(httpWebProxy_underscore); else connection = (HttpURLConnection) urlConn.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", MessageBufferConstants.getCONTENT_underscoreTYPE_underscorePROPERTY_underscoreFOR_underscoreTEXT());
            connection.setRequestProperty("Content-Length", "" + message.length());
            connection.setRequestProperty("Expect", "100-continue");
            connection.setRequestProperty("Accept", "*/*");
            String authStr = TokenConstants.getWrapAuthenticationType() + " " + TokenConstants.getWrapAuthorizationHeaderKey() + "=\"" + authorizationToken + "\"";
            connection.setRequestProperty("Authorization", authStr);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(message);
            wr.flush();
            wr.close();
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logRequest(connection, SDKLoggerHelper.RecordType.SendMessage_underscoreREQUEST);
            String responseCode = "<responseCode>" + connection.getResponseCode() + "</responseCode>";
            if (!((connection.getResponseCode() == MessageBufferConstants.HTTP_underscoreSTATUS_underscoreCODE_underscoreACCEPTED) || (connection.getResponseCode() == MessageBufferConstants.HTTP_underscoreSTATUS_underscoreCODE_underscoreCREATED) || (connection.getResponseCode() == MessageBufferConstants.HTTP_underscoreSTATUS_underscoreCODE_underscoreOK))) {
                throw new AppFabricException("Message could not be sent. Error.Response code: " + connection.getResponseCode());
            }
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.SendMessage_underscoreRESPONSE);
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
    }

