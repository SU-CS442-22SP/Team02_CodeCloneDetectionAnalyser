    @Override
    public JSONObject runCommand(JSONObject payload, HttpSession session) throws DefinedException {
        String sessionId = session.getId();
        log.debug("Login -> runCommand SID: " + sessionId);
        JSONObject toReturn = new JSONObject();
        boolean isOK = true;
        String username = null;
        try {
            username = payload.getString(ComConstants.LogIn.Request.USERNAME);
        } catch (JSONException e) {
            log.error("SessionId=" + sessionId + ", Missing username parameter", e);
            throw new DefinedException(StatusCodesV2.PARAMETER_underscoreERROR);
        }
        String password = null;
        if (isOK) {
            try {
                password = payload.getString(ComConstants.LogIn.Request.PASSWORD);
            } catch (JSONException e) {
                log.error("SessionId=" + sessionId + ", Missing password parameter", e);
                throw new DefinedException(StatusCodesV2.PARAMETER_underscoreERROR);
            }
        }
        if (isOK) {
            MessageDigest m = null;
            try {
                m = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                log.error("SessionId=" + sessionId + ", MD5 algorithm does not exist", e);
                e.printStackTrace();
                throw new DefinedException(StatusCodesV2.INTERNAL_underscoreSYSTEM_underscoreFAILURE);
            }
            m.update(password.getBytes(), 0, password.length());
            password = new BigInteger(1, m.digest()).toString(16);
            UserSession userSession = pli.login(username, password);
            try {
                if (userSession != null) {
                    session.setAttribute("user", userSession);
                    toReturn.put(ComConstants.Response.STATUS_underscoreCODE, StatusCodesV2.LOGIN_underscoreOK.getStatusCode());
                    toReturn.put(ComConstants.Response.STATUS_underscoreMESSAGE, StatusCodesV2.LOGIN_underscoreOK.getStatusMessage());
                } else {
                    log.error("SessionId=" + sessionId + ", Login failed: username=" + username + " not found");
                    toReturn.put(ComConstants.Response.STATUS_underscoreCODE, StatusCodesV2.LOGIN_underscoreUSER_underscoreOR_underscorePASSWORD_underscoreINCORRECT.getStatusCode());
                    toReturn.put(ComConstants.Response.STATUS_underscoreMESSAGE, StatusCodesV2.LOGIN_underscoreUSER_underscoreOR_underscorePASSWORD_underscoreINCORRECT.getStatusMessage());
                }
            } catch (JSONException e) {
                log.error("SessionId=" + sessionId + ", JSON exception occured in response", e);
                e.printStackTrace();
                throw new DefinedException(StatusCodesV2.INTERNAL_underscoreSYSTEM_underscoreFAILURE);
            }
        }
        log.debug("Login <- runCommand SID: " + sessionId);
        return toReturn;
    }

