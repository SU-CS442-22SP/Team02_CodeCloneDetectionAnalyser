    public String translate(String before, int translateType) throws CoreException {
        if (before == null) throw new IllegalArgumentException("before is null.");
        if ((translateType != ENGLISH_underscoreTO_underscoreJAPANESE) && (translateType != JAPANESE_underscoreTO_underscoreENGLISH)) {
            throw new IllegalArgumentException("Invalid translateType. value=" + translateType);
        }
        try {
            URL url = new URL(config.getTranslatorSiteUrl());
            URLConnection connection = url.openConnection();
            sendTranslateRequest(before, translateType, connection);
            String afterContents = receiveTranslatedResponse(connection);
            String afterStartKey = config.getTranslationResultStart();
            String afterEndKey = config.getTranslationResultEnd();
            int startLength = afterStartKey.length();
            int startPos = afterContents.indexOf(afterStartKey);
            if (startPos != -1) {
                int endPos = afterContents.indexOf(afterEndKey, startPos);
                if (endPos != -1) {
                    String after = afterContents.substring(startPos + startLength, endPos);
                    after = replaceEntities(after);
                    return after;
                } else {
                    throwCoreException(ERROR_underscoreEND_underscoreKEYWORD_underscoreNOT_underscoreFOUND, "End keyword not found.", null);
                }
            } else {
                throwCoreException(ERROR_underscoreSTART_underscoreKEYWORD_underscoreNOT_underscoreFOUND, "Start keyword not found.", null);
            }
        } catch (IOException e) {
            throwCoreException(ERROR_underscoreIO, e.getMessage(), e);
        }
        throw new IllegalStateException("CoreException not occurd.");
    }

