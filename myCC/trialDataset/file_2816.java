    public String getPolicy(String messageBufferName) throws AppFabricException {
        String responseString = null;
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
            StringBuffer sBuf = new StringBuffer();
            if (httpWebProxy_underscore != null) connection = (HttpURLConnection) urlConn.openConnection(httpWebProxy_underscore); else connection = (HttpURLConnection) urlConn.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", MessageBufferConstants.getCONTENT_underscoreTYPE_underscorePROPERTY_underscoreFOR_underscoreATOM_underscoreXML());
            String authStr = TokenConstants.getWrapAuthenticationType() + " " + TokenConstants.getWrapAuthorizationHeaderKey() + "=\"" + authorizationToken + "\"";
            connection.setRequestProperty("Authorization", authStr);
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logRequest(connection, SDKLoggerHelper.RecordType.GetPolicy_underscoreREQUEST);
            String responseCode = "<responseCode>" + connection.getResponseCode() + "</responseCode>";
            if ((connection.getResponseCode() == MessageBufferConstants.HTTP_underscoreSTATUS_underscoreCODE_underscoreOK)) {
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = rd.readLine()) != null) {
                    sBuf.append(line);
                    sBuf.append('\r');
                }
                rd.close();
                if (sBuf.toString().indexOf("<entry xmlns=") != -1) {
                    responseString = sBuf.toString();
                    if (LoggerUtil.getIsLoggingOn()) {
                        StringBuilder responseXML = new StringBuilder();
                        responseXML.append(responseCode);
                        responseXML.append(responseString);
                        SDKLoggerHelper.logMessage(URLEncoder.encode(responseXML.toString(), "UTF-8"), SDKLoggerHelper.RecordType.GetPolicy_underscoreRESPONSE);
                    }
                    return responseString;
                } else {
                    throw new AppFabricException("Message buffer policy could not be retrieved");
                }
            } else {
                if (LoggerUtil.getIsLoggingOn()) {
                    SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.GetPolicy_underscoreRESPONSE);
                }
                throw new AppFabricException("Message buffer policy could not be retrieved. Error.Response code:  " + connection.getResponseCode());
            }
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
    }

