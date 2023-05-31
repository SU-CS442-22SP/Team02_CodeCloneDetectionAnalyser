    public Message[] expunge() throws MessagingException {
        Statement oStmt = null;
        CallableStatement oCall = null;
        PreparedStatement oUpdt = null;
        ResultSet oRSet;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DBFolder.expunge()");
            DebugFile.incIdent();
        }
        if (0 == (iOpenMode & READ_underscoreWRITE)) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new javax.mail.FolderClosedException(this, "Folder is not open is READ_underscoreWRITE mode");
        }
        if ((0 == (iOpenMode & MODE_underscoreMBOX)) && (0 == (iOpenMode & MODE_underscoreBLOB))) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new javax.mail.FolderClosedException(this, "Folder is not open in MBOX nor BLOB mode");
        }
        MboxFile oMBox = null;
        DBSubset oDeleted = new DBSubset(DB.k_underscoremime_underscoremsgs, DB.gu_underscoremimemsg + "," + DB.pg_underscoremessage, DB.bo_underscoredeleted + "=1 AND " + DB.gu_underscorecategory + "='" + oCatg.getString(DB.gu_underscorecategory) + "'", 100);
        try {
            int iDeleted = oDeleted.load(getConnection());
            File oFile = getFile();
            if (oFile.exists() && iDeleted > 0) {
                oMBox = new MboxFile(oFile, MboxFile.READ_underscoreWRITE);
                int[] msgnums = new int[iDeleted];
                for (int m = 0; m < iDeleted; m++) msgnums[m] = oDeleted.getInt(1, m);
                oMBox.purge(msgnums);
                oMBox.close();
            }
            oStmt = oConn.createStatement(ResultSet.TYPE_underscoreFORWARD_underscoreONLY, ResultSet.CONCUR_underscoreREAD_underscoreONLY);
            oRSet = oStmt.executeQuery("SELECT p." + DB.file_underscorename + " FROM " + DB.k_underscoremime_underscoreparts + " p," + DB.k_underscoremime_underscoremsgs + " m WHERE p." + DB.gu_underscoremimemsg + "=m." + DB.gu_underscoremimemsg + " AND m." + DB.id_underscoredisposition + "='reference' AND m." + DB.bo_underscoredeleted + "=1 AND m." + DB.gu_underscorecategory + "='" + oCatg.getString(DB.gu_underscorecategory) + "'");
            while (oRSet.next()) {
                String sFileName = oRSet.getString(1);
                if (!oRSet.wasNull()) {
                    try {
                        File oRef = new File(sFileName);
                        oRef.delete();
                    } catch (SecurityException se) {
                        if (DebugFile.trace) DebugFile.writeln("SecurityException " + sFileName + " " + se.getMessage());
                    }
                }
            }
            oRSet.close();
            oRSet = null;
            oStmt.close();
            oStmt = null;
            oFile = getFile();
            oStmt = oConn.createStatement();
            oStmt.executeUpdate("UPDATE " + DB.k_underscorecategories + " SET " + DB.len_underscoresize + "=" + String.valueOf(oFile.length()) + " WHERE " + DB.gu_underscorecategory + "='" + getCategory().getString(DB.gu_underscorecategory) + "'");
            oStmt.close();
            oStmt = null;
            if (oConn.getDataBaseProduct() == JDCConnection.DBMS_underscorePOSTGRESQL) {
                oStmt = oConn.createStatement();
                for (int d = 0; d < iDeleted; d++) oStmt.executeQuery("SELECT k_underscoresp_underscoredel_underscoremime_underscoremsg('" + oDeleted.getString(0, d) + "')");
                oStmt.close();
                oStmt = null;
            } else {
                oCall = oConn.prepareCall("{ call k_underscoresp_underscoredel_underscoremime_underscoremsg(?) }");
                for (int d = 0; d < iDeleted; d++) {
                    oCall.setString(1, oDeleted.getString(0, d));
                    oCall.execute();
                }
                oCall.close();
                oCall = null;
            }
            if (oFile.exists() && iDeleted > 0) {
                BigDecimal oUnit = new BigDecimal(1);
                oStmt = oConn.createStatement(ResultSet.TYPE_underscoreFORWARD_underscoreONLY, ResultSet.CONCUR_underscoreREAD_underscoreONLY);
                oRSet = oStmt.executeQuery("SELECT MAX(" + DB.pg_underscoremessage + ") FROM " + DB.k_underscoremime_underscoremsgs + " WHERE " + DB.gu_underscorecategory + "='getCategory().getString(DB.gu_underscorecategory)'");
                oRSet.next();
                BigDecimal oMaxPg = oRSet.getBigDecimal(1);
                if (oRSet.wasNull()) oMaxPg = new BigDecimal(0);
                oRSet.close();
                oRSet = null;
                oStmt.close();
                oStmt = null;
                oMaxPg = oMaxPg.add(oUnit);
                oStmt = oConn.createStatement();
                oStmt.executeUpdate("UPDATE " + DB.k_underscoremime_underscoremsgs + " SET " + DB.pg_underscoremessage + "=" + DB.pg_underscoremessage + "+" + oMaxPg.toString() + " WHERE " + DB.gu_underscorecategory + "='" + getCategory().getString(DB.gu_underscorecategory) + "'");
                oStmt.close();
                oStmt = null;
                DBSubset oMsgSet = new DBSubset(DB.k_underscoremime_underscoremsgs, DB.gu_underscoremimemsg + "," + DB.pg_underscoremessage, DB.gu_underscorecategory + "='" + getCategory().getString(DB.gu_underscorecategory) + "' ORDER BY " + DB.pg_underscoremessage, 1000);
                int iMsgCount = oMsgSet.load(oConn);
                oMBox = new MboxFile(oFile, MboxFile.READ_underscoreONLY);
                long[] aPositions = oMBox.getMessagePositions();
                oMBox.close();
                if (iMsgCount != aPositions.length) {
                    throw new IOException("DBFolder.expunge() Message count of " + String.valueOf(aPositions.length) + " at MBOX file " + oFile.getName() + " does not match message count at database index of " + String.valueOf(iMsgCount));
                }
                oMaxPg = new BigDecimal(0);
                oUpdt = oConn.prepareStatement("UPDATE " + DB.k_underscoremime_underscoremsgs + " SET " + DB.pg_underscoremessage + "=?," + DB.nu_underscoreposition + "=? WHERE " + DB.gu_underscoremimemsg + "=?");
                for (int m = 0; m < iMsgCount; m++) {
                    oUpdt.setBigDecimal(1, oMaxPg);
                    oUpdt.setBigDecimal(2, new BigDecimal(aPositions[m]));
                    oUpdt.setString(3, oMsgSet.getString(0, m));
                    oUpdt.executeUpdate();
                    oMaxPg = oMaxPg.add(oUnit);
                }
                oUpdt.close();
            }
            oConn.commit();
        } catch (SQLException sqle) {
            try {
                if (oMBox != null) oMBox.close();
            } catch (Exception e) {
            }
            try {
                if (oStmt != null) oStmt.close();
            } catch (Exception e) {
            }
            try {
                if (oCall != null) oCall.close();
            } catch (Exception e) {
            }
            try {
                if (oConn != null) oConn.rollback();
            } catch (Exception e) {
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        } catch (IOException sqle) {
            try {
                if (oMBox != null) oMBox.close();
            } catch (Exception e) {
            }
            try {
                if (oStmt != null) oStmt.close();
            } catch (Exception e) {
            }
            try {
                if (oCall != null) oCall.close();
            } catch (Exception e) {
            }
            try {
                if (oConn != null) oConn.rollback();
            } catch (Exception e) {
            }
            throw new MessagingException(sqle.getMessage(), sqle);
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End DBFolder.expunge()");
        }
        return null;
    }

