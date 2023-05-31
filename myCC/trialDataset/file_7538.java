    public static int getNextID(int AD_underscoreClient_underscoreID, String TableName, String trxName) {
        if (TableName == null || TableName.length() == 0) throw new IllegalArgumentException("TableName missing");
        int retValue = -1;
        boolean adempiereSys = Ini.isPropertyBool(Ini.P_underscoreADEMPIERESYS);
        if (adempiereSys && AD_underscoreClient_underscoreID > 11) adempiereSys = false;
        if (CLogMgt.isLevel(LOGLEVEL)) s_underscorelog.log(LOGLEVEL, TableName + " - AdempiereSys=" + adempiereSys + " [" + trxName + "]");
        String selectSQL = null;
        if (DB.isPostgreSQL()) {
            selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_underscoreSequence_underscoreID " + "FROM AD_underscoreSequence " + "WHERE Name=?" + " AND IsActive='Y' AND IsTableID='Y' AND IsAutoSequence='Y' " + " FOR UPDATE OF AD_underscoreSequence ";
            USE_underscorePROCEDURE = false;
        } else if (DB.isOracle()) {
            selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_underscoreSequence_underscoreID " + "FROM AD_underscoreSequence " + "WHERE Name=?" + " AND IsActive='Y' AND IsTableID='Y' AND IsAutoSequence='Y' " + "FOR UPDATE";
            USE_underscorePROCEDURE = true;
        } else {
            selectSQL = "SELECT CurrentNext, CurrentNextSys, IncrementNo, AD_underscoreSequence_underscoreID " + "FROM AD_underscoreSequence " + "WHERE Name=?" + " AND IsActive='Y' AND IsTableID='Y' AND IsAutoSequence='Y' ";
            USE_underscorePROCEDURE = false;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        for (int i = 0; i < 3; i++) {
            try {
                conn = DB.getConnectionID();
                if (conn == null) return -1;
                pstmt = conn.prepareStatement(selectSQL, ResultSet.TYPE_underscoreFORWARD_underscoreONLY, ResultSet.CONCUR_underscoreUPDATABLE);
                pstmt.setString(1, TableName);
                if (!USE_underscorePROCEDURE && DB.getDatabase().isQueryTimeoutSupported()) pstmt.setQueryTimeout(QUERY_underscoreTIME_underscoreOUT);
                rs = pstmt.executeQuery();
                if (CLogMgt.isLevelFinest()) s_underscorelog.finest("AC=" + conn.getAutoCommit() + ", RO=" + conn.isReadOnly() + " - Isolation=" + conn.getTransactionIsolation() + "(" + Connection.TRANSACTION_underscoreREAD_underscoreCOMMITTED + ") - RSType=" + pstmt.getResultSetType() + "(" + ResultSet.TYPE_underscoreSCROLL_underscoreSENSITIVE + "), RSConcur=" + pstmt.getResultSetConcurrency() + "(" + ResultSet.CONCUR_underscoreUPDATABLE + ")");
                if (rs.next()) {
                    MTable table = MTable.get(Env.getCtx(), TableName);
                    int AD_underscoreSequence_underscoreID = rs.getInt(4);
                    boolean gotFromHTTP = false;
                    if (adempiereSys) {
                        String isUseCentralizedID = MSysConfig.getValue("DICTIONARY_underscoreID_underscoreUSE_underscoreCENTRALIZED_underscoreID", "Y");
                        if ((!isUseCentralizedID.equals("N")) && (!isExceptionCentralized(TableName))) {
                            retValue = getNextOfficialID_underscoreHTTP(TableName);
                            if (retValue > 0) {
                                PreparedStatement updateSQL;
                                updateSQL = conn.prepareStatement("UPDATE AD_underscoreSequence SET CurrentNextSys = ? + 1 WHERE AD_underscoreSequence_underscoreID = ?");
                                try {
                                    updateSQL.setInt(1, retValue);
                                    updateSQL.setInt(2, AD_underscoreSequence_underscoreID);
                                    updateSQL.executeUpdate();
                                } finally {
                                    updateSQL.close();
                                }
                            }
                            gotFromHTTP = true;
                        }
                    }
                    boolean queryProjectServer = false;
                    if (table.getColumn("EntityType") != null) queryProjectServer = true;
                    if (!queryProjectServer && MSequence.Table_underscoreName.equalsIgnoreCase(TableName)) queryProjectServer = true;
                    if (queryProjectServer && (adempiereSys) && (!isExceptionCentralized(TableName))) {
                        String isUseProjectCentralizedID = MSysConfig.getValue("PROJECT_underscoreID_underscoreUSE_underscoreCENTRALIZED_underscoreID", "N");
                        if (isUseProjectCentralizedID.equals("Y")) {
                            retValue = getNextProjectID_underscoreHTTP(TableName);
                            if (retValue > 0) {
                                PreparedStatement updateSQL;
                                updateSQL = conn.prepareStatement("UPDATE AD_underscoreSequence SET CurrentNext = GREATEST(CurrentNext, ? + 1) WHERE AD_underscoreSequence_underscoreID = ?");
                                try {
                                    updateSQL.setInt(1, retValue);
                                    updateSQL.setInt(2, AD_underscoreSequence_underscoreID);
                                    updateSQL.executeUpdate();
                                } finally {
                                    updateSQL.close();
                                }
                            }
                            gotFromHTTP = true;
                        }
                    }
                    if (!gotFromHTTP) {
                        if (USE_underscorePROCEDURE) {
                            retValue = nextID(conn, AD_underscoreSequence_underscoreID, adempiereSys);
                        } else {
                            PreparedStatement updateSQL;
                            int incrementNo = rs.getInt(3);
                            if (adempiereSys) {
                                updateSQL = conn.prepareStatement("UPDATE AD_underscoreSequence SET CurrentNextSys = CurrentNextSys + ? WHERE AD_underscoreSequence_underscoreID = ?");
                                retValue = rs.getInt(2);
                            } else {
                                updateSQL = conn.prepareStatement("UPDATE AD_underscoreSequence SET CurrentNext = CurrentNext + ? WHERE AD_underscoreSequence_underscoreID = ?");
                                retValue = rs.getInt(1);
                            }
                            try {
                                updateSQL.setInt(1, incrementNo);
                                updateSQL.setInt(2, AD_underscoreSequence_underscoreID);
                                updateSQL.executeUpdate();
                            } finally {
                                updateSQL.close();
                            }
                        }
                    }
                    conn.commit();
                } else s_underscorelog.severe("No record found - " + TableName);
                break;
            } catch (Exception e) {
                s_underscorelog.log(Level.SEVERE, TableName + " - " + e.getMessage(), e);
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException e1) {
                }
            } finally {
                DB.close(rs, pstmt);
                pstmt = null;
                rs = null;
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                    conn = null;
                }
            }
            Thread.yield();
        }
        return retValue;
    }

