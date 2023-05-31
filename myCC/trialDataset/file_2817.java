    public void deleteMessageBuffer(String messageBufferName) throws AppFabricException {
        MessageBufferUtil msgBufferUtilObj = new MessageBufferUtil(solutionName, TokenConstants.getSimpleAuthAuthenticationType());
        String requestUri = msgBufferUtilObj.getRequestUri();
        String messageBufferUri = msgBufferUtilObj.getCreateMessageBufferUri(messageBufferName);
        String authorizationToken = "";
        try {
            ACSTokenProvider tp = new ACSTokenProvider(httpWebProxyServer_underscore, httpWebProxyPort_underscore, this.credentials);
            authorizationToken = tp.getACSToken(requestUri, messageBufferUri);
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
        try {
            messageBufferUri = messageBufferUri.replaceAll("http", "https");
            URL urlConn = new URL(messageBufferUri);
            HttpURLConnection connection;
            if (httpWebProxy_underscore != null) connection = (HttpURLConnection) urlConn.openConnection(httpWebProxy_underscore); else connection = (HttpURLConnection) urlConn.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", MessageBufferConstants.getCONTENT_underscoreTYPE_underscorePROPERTY_underscoreFOR_underscoreATOM_underscoreXML());
            String authStr = TokenConstants.getWrapAuthenticationType() + " " + TokenConstants.getWrapAuthorizationHeaderKey() + "=\"" + authorizationToken + "\"";
            connection.setRequestProperty("Authorization", authStr);
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logRequest(connection, SDKLoggerHelper.RecordType.DeleteMessageBuffer_underscoreREQUEST);
            String responseCode = "<responseCode>" + connection.getResponseCode() + "</responseCode>";
            if ((connection.getResponseCode() == MessageBufferConstants.HTTP_underscoreSTATUS_underscoreCODE_underscoreOK)) {
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
            } else {
                throw new AppFabricException("MessageBuffer could not be deleted.Error...Response code:  " + connection.getResponseCode());
            }
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.DeleteMessageBuffer_underscoreRESPONSE);
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
    }

