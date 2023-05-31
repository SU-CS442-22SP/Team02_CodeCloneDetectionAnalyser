    public void createMessageBuffer(String messageBufferName, MessageBufferPolicy messageBufferPolicyObj) throws AppFabricException {
        String messageBufferPolicy = messageBufferPolicyObj.getMessageBufferPolicy();
        if (messageBufferPolicy == null) {
            throw new AppFabricException("MessageBufferPolicy can not be null");
        }
        MessageBufferUtil msgBufferUtilObj = new MessageBufferUtil(solutionName, TokenConstants.getSimpleAuthAuthenticationType());
        String requestUri = msgBufferUtilObj.getRequestUri();
        String messageBufferUri = msgBufferUtilObj.getCreateMessageBufferUri(messageBufferName);
        if (messageBufferUri == null) {
            throw new AppFabricException("MessageBufferUri can not be null");
        }
        String authorizationToken = "";
        try {
            ACSTokenProvider tp = new ACSTokenProvider(httpWebProxyServer_underscore, httpWebProxyPort_underscore, this.credentials);
            authorizationToken = tp.getACSToken(requestUri, messageBufferUri);
        } catch (AppFabricException e) {
            throw e;
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
        try {
            messageBufferUri = messageBufferUri.replaceAll("http", "https");
            URL urlConn = new URL(messageBufferUri);
            HttpURLConnection connection;
            if (httpWebProxy_underscore != null) connection = (HttpURLConnection) urlConn.openConnection(httpWebProxy_underscore); else connection = (HttpURLConnection) urlConn.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", MessageBufferConstants.getCONTENT_underscoreTYPE_underscorePROPERTY_underscoreFOR_underscoreATOM_underscoreXML());
            connection.setRequestProperty("Content-Length", "" + messageBufferPolicy.length());
            String authStr = TokenConstants.getWrapAuthenticationType() + " " + TokenConstants.getWrapAuthorizationHeaderKey() + "=\"" + authorizationToken + "\"";
            connection.setRequestProperty("Authorization", authStr);
            connection.setRequestProperty("Expect", "100-continue");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(messageBufferPolicy);
            wr.flush();
            wr.close();
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logRequest(connection, SDKLoggerHelper.RecordType.CreateMessageBuffer_underscoreREQUEST);
            String responseCode = "<responseCode>" + connection.getResponseCode() + "</responseCode>";
            if ((connection.getResponseCode() == MessageBufferConstants.HTTP_underscoreSTATUS_underscoreCODE_underscoreACCEPTED) || (connection.getResponseCode() == MessageBufferConstants.HTTP_underscoreSTATUS_underscoreCODE_underscoreCREATED) || (connection.getResponseCode() == MessageBufferConstants.HTTP_underscoreSTATUS_underscoreCODE_underscoreOK)) {
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                if (LoggerUtil.getIsLoggingOn()) {
                    StringBuilder responseXML = new StringBuilder();
                    responseXML.append(responseCode);
                    responseXML.append(response.toString());
                    SDKLoggerHelper.logMessage(URLEncoder.encode(responseXML.toString(), "UTF-8"), SDKLoggerHelper.RecordType.CreateMessageBuffer_underscoreRESPONSE);
                }
            } else {
                if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.CreateMessageBuffer_underscoreRESPONSE);
                throw new AppFabricException("MessageBuffer could not be created or updated. Error. Response code:  " + connection.getResponseCode());
            }
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
    }

