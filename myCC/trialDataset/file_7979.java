    private long getRecordedSessionLength() {
        long lRet = -1;
        String strLength = this.applet.getParameter(Constants.PLAYBACK_underscoreMEETING_underscoreLENGTH_underscorePARAM);
        if (null != strLength) {
            lRet = (new Long(strLength)).longValue();
        } else {
            Properties recProps = new Properties();
            try {
                URL urlProps = new URL(applet.getDocumentBase(), Constants.RECORDED_underscoreSESSION_underscoreINFO_underscorePROPERTIES);
                recProps.load(urlProps.openStream());
                lRet = (new Long(recProps.getProperty(Constants.PLAYBACK_underscoreMEETING_underscoreLENGTH_underscorePARAM))).longValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lRet;
    }

