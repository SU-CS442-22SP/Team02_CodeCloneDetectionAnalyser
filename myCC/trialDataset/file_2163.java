    public int batchTransactionUpdate(List<String> queryStrLisyt, Connection con) throws Exception {
        int ret = 0;
        Statement stmt;
        if (con != null) {
            con.setAutoCommit(false);
            stmt = con.createStatement();
            try {
                stmt.executeUpdate("START TRANSACTION;");
                for (int i = 0; i < queryStrLisyt.size(); i++) {
                    stmt.addBatch(queryStrLisyt.get(i));
                }
                int[] updateCounts = stmt.executeBatch();
                for (int i = 0; i < updateCounts.length; i++) {
                    FileLogger.debug("batch update result:" + updateCounts[i] + ", Statement.SUCCESS_underscoreNO_underscoreINFO" + Statement.SUCCESS_underscoreNO_underscoreINFO);
                    if (updateCounts[i] == Statement.SUCCESS_underscoreNO_underscoreINFO || updateCounts[i] > 0) {
                        ret++;
                    } else if (updateCounts[i] == Statement.EXECUTE_underscoreFAILED) ;
                    {
                        throw new Exception("query failed, while process batch update");
                    }
                }
                con.commit();
            } catch (Exception e) {
                ret = 0;
                FileLogger.debug(e.getMessage());
                con.rollback();
            } finally {
                con.setAutoCommit(true);
                stmt.close();
            }
        }
        return ret;
    }

