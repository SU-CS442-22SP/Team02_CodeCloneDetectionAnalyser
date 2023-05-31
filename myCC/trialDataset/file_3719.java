    public void setFlag(Flags.Flag oFlg, boolean bFlg) throws MessagingException {
        String sColunm;
        super.setFlag(oFlg, bFlg);
        if (oFlg.equals(Flags.Flag.ANSWERED)) sColunm = DB.bo_underscoreanswered; else if (oFlg.equals(Flags.Flag.DELETED)) sColunm = DB.bo_underscoredeleted; else if (oFlg.equals(Flags.Flag.DRAFT)) sColunm = DB.bo_underscoredraft; else if (oFlg.equals(Flags.Flag.FLAGGED)) sColunm = DB.bo_underscoreflagged; else if (oFlg.equals(Flags.Flag.RECENT)) sColunm = DB.bo_underscorerecent; else if (oFlg.equals(Flags.Flag.SEEN)) sColunm = DB.bo_underscoreseen; else sColunm = null;
        if (null != sColunm && oFolder instanceof DBFolder) {
            JDCConnection oConn = null;
            PreparedStatement oUpdt = null;
            try {
                oConn = ((DBFolder) oFolder).getConnection();
                String sSQL = "UPDATE " + DB.k_underscoremime_underscoremsgs + " SET " + sColunm + "=" + (bFlg ? "1" : "0") + " WHERE " + DB.gu_underscoremimemsg + "='" + getMessageGuid() + "'";
                if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
                oUpdt = oConn.prepareStatement(sSQL);
                oUpdt.executeUpdate();
                oUpdt.close();
                oUpdt = null;
                oConn.commit();
                oConn = null;
            } catch (SQLException e) {
                if (null != oConn) {
                    try {
                        oConn.rollback();
                    } catch (Exception ignore) {
                    }
                }
                if (null != oUpdt) {
                    try {
                        oUpdt.close();
                    } catch (Exception ignore) {
                    }
                }
                if (DebugFile.trace) DebugFile.decIdent();
                throw new MessagingException(e.getMessage(), e);
            }
        }
    }

