    public static String deleteTag(String tag_underscoreid) {
        String so = OctopusErrorMessages.UNKNOWN_underscoreERROR;
        if (tag_underscoreid == null || tag_underscoreid.trim().equals("")) {
            return OctopusErrorMessages.TAG_underscoreID_underscoreCANT_underscoreBE_underscoreEMPTY;
        }
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            theConnection.setAutoCommit(false);
            String query = "DELETE FROM tr_underscoretranslation WHERE tr_underscoretranslation_underscoretrtagid=?";
            PreparedStatement state = theConnection.prepareStatement(query);
            state.setString(1, tag_underscoreid);
            state.executeUpdate();
            String query2 = "DELETE FROM tr_underscoretag WHERE tr_underscoretag_underscoreid=? ";
            PreparedStatement state2 = theConnection.prepareStatement(query2);
            state2.setString(1, tag_underscoreid);
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

