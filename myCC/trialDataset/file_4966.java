    public static String addTag(String tag_underscoreid, String tag_underscoredescription, String tag_underscoretext, String tag_underscoreauthor, String application_underscorecode) {
        String so = OctopusErrorMessages.UNKNOWN_underscoreERROR;
        if (tag_underscoreid == null || tag_underscoreid.trim().equals("")) {
            return OctopusErrorMessages.TAG_underscoreID_underscoreCANT_underscoreBE_underscoreEMPTY;
        }
        if (tag_underscoreid.trim().equals(application_underscorecode)) {
            return OctopusErrorMessages.TAG_underscoreID_underscoreTOO_underscoreSHORT;
        }
        if (!StringUtil.isAlphaNumerical(StringUtil.replace(StringUtil.replace(tag_underscoreid, "-", ""), "_underscore", ""))) {
            return OctopusErrorMessages.TAG_underscoreID_underscoreMUST_underscoreBE_underscoreALPHANUMERIC;
        }
        if (!tag_underscoreid.startsWith(application_underscorecode)) {
            return OctopusErrorMessages.TAG_underscoreID_underscoreMUST_underscoreSTART + " " + application_underscorecode;
        }
        String tag_underscoreexist = exist(tag_underscoreid);
        if (!tag_underscoreexist.equals(OctopusErrorMessages.DOESNT_underscoreALREADY_underscoreEXIST)) {
            return tag_underscoreexist;
        }
        if (tag_underscoredescription != null && !tag_underscoredescription.trim().equals("")) {
            tag_underscoredescription = StringUtil.replace(tag_underscoredescription, "\n", " ");
            tag_underscoredescription = StringUtil.replace(tag_underscoredescription, "\r", " ");
            tag_underscoredescription = StringUtil.replace(tag_underscoredescription, "\t", " ");
            tag_underscoredescription = StringUtil.replace(tag_underscoredescription, "<", "&#60;");
            tag_underscoredescription = StringUtil.replace(tag_underscoredescription, ">", "&#62;");
            tag_underscoredescription = StringUtil.replace(tag_underscoredescription, "'", "&#39;");
        } else {
            return OctopusErrorMessages.DESCRIPTION_underscoreTEXT_underscoreEMPTY;
        }
        if (tag_underscoretext != null && !tag_underscoretext.trim().equals("")) {
            tag_underscoretext = StringUtil.replace(tag_underscoretext, "\n", " ");
            tag_underscoretext = StringUtil.replace(tag_underscoretext, "\r", " ");
            tag_underscoretext = StringUtil.replace(tag_underscoretext, "\t", " ");
            tag_underscoretext = StringUtil.replace(tag_underscoretext, "<", "&#60;");
            tag_underscoretext = StringUtil.replace(tag_underscoretext, ">", "&#62;");
            tag_underscoretext = StringUtil.replace(tag_underscoretext, "'", "&#39;");
        } else {
            return OctopusErrorMessages.TRANSLATION_underscoreTEXT_underscoreEMPTY;
        }
        if (tag_underscoreauthor == null || tag_underscoreauthor.trim().equals("")) {
            return OctopusErrorMessages.MAIN_underscorePARAMETER_underscoreEMPTY;
        }
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            theConnection.setAutoCommit(false);
            String query = "INSERT INTO tr_underscoretag (tr_underscoretag_underscoreid,tr_underscoretag_underscoreapplicationid,tr_underscoretag_underscoreinfo,tr_underscoretag_underscorecreationdate) ";
            query += "VALUES (?,?,'" + tag_underscoredescription + "',getdate())";
            PreparedStatement state = theConnection.prepareStatement(query);
            state.setString(1, tag_underscoreid);
            state.setString(2, application_underscorecode);
            state.executeUpdate();
            String query2 = "INSERT INTO tr_underscoretranslation (tr_underscoretranslation_underscoretrtagid, tr_underscoretranslation_underscorelanguage, tr_underscoretranslation_underscoretext, tr_underscoretranslation_underscoreversion, tr_underscoretranslation_underscorelud, tr_underscoretranslation_underscorelun ) ";
            query2 += "VALUES(?,'" + OctopusApplication.MASTER_underscoreLANGUAGE + "','" + tag_underscoretext + "',0,getdate(),?)";
            PreparedStatement state2 = theConnection.prepareStatement(query2);
            state2.setString(1, tag_underscoreid);
            state2.setString(2, tag_underscoreauthor);
            state2.executeUpdate();
            theConnection.commit();
            so = OctopusErrorMessages.ACTION_underscoreDONE;
        } catch (SQLException e) {
            try {
                theConnection.rollback();
            } catch (SQLException ex) {
            }
            so = OctopusErrorMessages.ERROR_underscoreDATABASE;
        } finally {
            if (theConnection != null) {
                try {
                    theConnection.setAutoCommit(true);
                } catch (SQLException ex) {
                }
                theConnection.release();
            }
        }
        return so;
    }

