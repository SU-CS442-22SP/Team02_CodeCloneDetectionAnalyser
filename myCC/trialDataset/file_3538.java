    public void init() {
        this.setFormulaCalculationMode(WorkBookHandle.CALCULATE_underscoreALWAYS);
        try {
            if (memeId < 0) {
            } else {
                conurl = new URL(ServerURL + "?meme_underscoreid=" + memeId);
                java.io.InputStream xmlstr = conurl.openStream();
                this.removeAllWorkSheets();
                this.setFormulaCalculationMode(WorkBookHandle.CALCULATE_underscoreEXPLICIT);
                this.setStringEncodingMode(WorkBookHandle.STRING_underscoreENCODING_underscoreUNICODE);
                this.setDupeStringMode(WorkBookHandle.SHAREDUPES);
                ExtenXLS.parseNBind(this, xmlstr);
                this.setFormulaCalculationMode(WorkBookHandle.CALCULATE_underscoreALWAYS);
            }
        } catch (Exception ex) {
            throw new WorkBookException("Error while connecting to: " + ServerURL + ":" + ex.toString(), WorkBookException.RUNTIME_underscoreERROR);
        }
    }

