    public static synchronized Integer getNextSequence(String seqNum) throws ApplicationException {
        Connection dbConn = null;
        java.sql.PreparedStatement preStat = null;
        java.sql.ResultSet rs = null;
        boolean noTableMatchFlag = false;
        int currID = 0;
        int nextID = 0;
        try {
            dbConn = getConnection();
        } catch (Exception e) {
            log.error("Error Getting Connection.", e);
            throw new ApplicationException("errors.framework.db_underscoreconn", e);
        }
        synchronized (hashPkKeyLock) {
            if (hashPkKeyLock.get(seqNum) == null) {
                hashPkKeyLock.put(seqNum, new Object());
            }
        }
        synchronized (hashPkKeyLock.get(seqNum)) {
            synchronized (dbConn) {
                try {
                    preStat = dbConn.prepareStatement("SELECT TABLE_underscoreKEY_underscoreMAX FROM SYS_underscoreTABLE_underscoreKEY WHERE TABLE_underscoreNAME=?");
                    preStat.setString(1, seqNum);
                    rs = preStat.executeQuery();
                    if (rs.next()) {
                        currID = rs.getInt(1);
                    } else {
                        noTableMatchFlag = true;
                    }
                } catch (Exception e) {
                    log.error(e, e);
                    try {
                        dbConn.close();
                    } catch (Exception ignore) {
                    } finally {
                        dbConn = null;
                    }
                    throw new ApplicationException("errors.framework.get_underscorenext_underscoreseq", e, seqNum);
                } finally {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    } finally {
                        rs = null;
                    }
                    try {
                        preStat.close();
                    } catch (Exception ignore) {
                    } finally {
                        preStat = null;
                    }
                }
                if (noTableMatchFlag) {
                    try {
                        currID = 0;
                        preStat = dbConn.prepareStatement("INSERT INTO SYS_underscoreTABLE_underscoreKEY(TABLE_underscoreNAME, TABLE_underscoreKEY_underscoreMAX) VALUES(?, ?)", java.sql.ResultSet.TYPE_underscoreSCROLL_underscoreINSENSITIVE, java.sql.ResultSet.CONCUR_underscoreUPDATABLE);
                        preStat.setString(1, seqNum);
                        preStat.setInt(2, currID);
                        preStat.executeUpdate();
                    } catch (Exception e) {
                        log.error(e, e);
                        try {
                            dbConn.close();
                        } catch (Exception ignore) {
                        } finally {
                            dbConn = null;
                        }
                        throw new ApplicationException("errors.framework.get_underscorenext_underscoreseq", e, seqNum);
                    } finally {
                        try {
                            preStat.close();
                        } catch (Exception ignore) {
                        } finally {
                            preStat = null;
                        }
                    }
                }
                try {
                    int updateCnt = 0;
                    nextID = currID;
                    do {
                        nextID++;
                        preStat = dbConn.prepareStatement("UPDATE SYS_underscoreTABLE_underscoreKEY SET TABLE_underscoreKEY_underscoreMAX=? WHERE TABLE_underscoreNAME=? AND TABLE_underscoreKEY_underscoreMAX=?", java.sql.ResultSet.TYPE_underscoreSCROLL_underscoreINSENSITIVE, java.sql.ResultSet.CONCUR_underscoreUPDATABLE);
                        preStat.setInt(1, nextID);
                        preStat.setString(2, seqNum);
                        preStat.setInt(3, currID);
                        updateCnt = preStat.executeUpdate();
                        currID++;
                        if (updateCnt == 0 && (currID % 2) == 0) {
                            Thread.sleep(50);
                        }
                    } while (updateCnt == 0);
                    dbConn.commit();
                    return (new Integer(nextID));
                } catch (Exception e) {
                    log.error(e, e);
                    try {
                        dbConn.rollback();
                    } catch (Exception ignore) {
                    }
                    throw new ApplicationException("errors.framework.get_underscorenext_underscoreseq", e, seqNum);
                } finally {
                    try {
                        preStat.close();
                    } catch (Exception ignore) {
                    } finally {
                        preStat = null;
                    }
                    try {
                        dbConn.close();
                    } catch (Exception ignore) {
                    } finally {
                        dbConn = null;
                    }
                }
            }
        }
    }

