    public static boolean copyDataToNewTable(EboContext p_underscoreeboctx, String srcTableName, String destTableName, String where, boolean log, int mode) throws boRuntimeException {
        srcTableName = srcTableName.toUpperCase();
        destTableName = destTableName.toUpperCase();
        Connection cn = null;
        Connection cndef = null;
        boolean ret = false;
        try {
            boolean srcexists = false;
            boolean destexists = false;
            final InitialContext ic = new InitialContext();
            cn = p_underscoreeboctx.getConnectionData();
            cndef = p_underscoreeboctx.getConnectionDef();
            PreparedStatement pstm = cn.prepareStatement("SELECT TABLE_underscoreNAME FROM USER_underscoreTABLES WHERE TABLE_underscoreNAME=?");
            pstm.setString(1, srcTableName);
            ResultSet rslt = pstm.executeQuery();
            if (rslt.next()) {
                srcexists = true;
            }
            rslt.close();
            pstm.setString(1, destTableName);
            rslt = pstm.executeQuery();
            if (rslt.next()) {
                destexists = true;
            }
            if (!destexists) {
                rslt.close();
                pstm.close();
                pstm = cn.prepareStatement("SELECT VIEW_underscoreNAME FROM USER_underscoreVIEWS WHERE VIEW_underscoreNAME=?");
                pstm.setString(1, destTableName);
                rslt = pstm.executeQuery();
                if (rslt.next()) {
                    CallableStatement cstm = cn.prepareCall("DROP VIEW " + destTableName);
                    cstm.execute();
                    cstm.close();
                }
            }
            rslt.close();
            pstm.close();
            if (srcexists && !destexists) {
                if (log) {
                    logger.finest(LoggerMessageLocalizer.getMessage("CREATING_underscoreAND_underscoreCOPY_underscoreDATA_underscoreFROM") + " [" + srcTableName + "] " + LoggerMessageLocalizer.getMessage("TO") + " [" + destTableName + "]");
                }
                CallableStatement cstm = cn.prepareCall("CREATE TABLE " + destTableName + " AS SELECT * FROM " + srcTableName + " " + (((where != null) && (where.length() > 0)) ? (" WHERE " + where) : ""));
                cstm.execute();
                cstm.close();
                if (log) {
                    logger.finest(LoggerMessageLocalizer.getMessage("UPDATING_underscoreNGTDIC"));
                }
                cn.commit();
                ret = true;
            } else if (srcexists && destexists) {
                if (log) {
                    logger.finest(LoggerMessageLocalizer.getMessage("COPY_underscoreDATA_underscoreFROM") + " [" + srcTableName + "] " + LoggerMessageLocalizer.getMessage("TO") + "  [" + destTableName + "]");
                }
                PreparedStatement pstm2 = cn.prepareStatement("SELECT COLUMN_underscoreNAME FROM USER_underscoreTAB_underscoreCOLUMNS WHERE TABLE_underscoreNAME = ? ");
                pstm2.setString(1, destTableName);
                ResultSet rslt2 = pstm2.executeQuery();
                StringBuffer fields = new StringBuffer();
                PreparedStatement pstm3 = cn.prepareStatement("SELECT COLUMN_underscoreNAME FROM USER_underscoreTAB_underscoreCOLUMNS WHERE TABLE_underscoreNAME = ? and COLUMN_underscoreNAME=?");
                while (rslt2.next()) {
                    pstm3.setString(1, srcTableName);
                    pstm3.setString(2, rslt2.getString(1));
                    ResultSet rslt3 = pstm3.executeQuery();
                    if (rslt3.next()) {
                        if (fields.length() > 0) {
                            fields.append(',');
                        }
                        fields.append('"').append(rslt2.getString(1)).append('"');
                    }
                    rslt3.close();
                }
                pstm3.close();
                rslt2.close();
                pstm2.close();
                CallableStatement cstm;
                int recs = 0;
                if ((mode == 0) || (mode == 1)) {
                    cstm = cn.prepareCall("INSERT INTO " + destTableName + "( " + fields.toString() + " ) ( SELECT " + fields.toString() + " FROM " + srcTableName + " " + (((where != null) && (where.length() > 0)) ? (" WHERE " + where) : "") + ")");
                    recs = cstm.executeUpdate();
                    cstm.close();
                    if (log) {
                        logger.finest(LoggerMessageLocalizer.getMessage("DONE") + " [" + recs + "] " + LoggerMessageLocalizer.getMessage("RECORDS_underscoreCOPIED"));
                    }
                }
                cn.commit();
                ret = true;
            }
        } catch (Exception e) {
            try {
                cn.rollback();
            } catch (Exception z) {
                throw new boRuntimeException("boBuildDB.moveTable", "BO-1304", z);
            }
            throw new boRuntimeException("boBuildDB.moveTable", "BO-1304", e);
        } finally {
            try {
                cn.close();
            } catch (Exception e) {
            }
            try {
                cndef.close();
            } catch (Exception e) {
            }
        }
        return ret;
    }

