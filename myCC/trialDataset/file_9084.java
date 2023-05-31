    protected Object insertSingle(Object name, Object fact) throws SQLException {
        DFAgentDescription dfd = (DFAgentDescription) fact;
        AID agentAID = dfd.getName();
        String agentName = agentAID.getName();
        DFAgentDescription dfdToReturn = null;
        String batchErrMsg = "";
        Connection conn = getConnectionWrapper().getConnection();
        PreparedStatements pss = getPreparedStatements();
        try {
            dfdToReturn = (DFAgentDescription) removeSingle(dfd.getName());
            Date leaseTime = dfd.getLeaseTime();
            long lt = (leaseTime != null ? leaseTime.getTime() : -1);
            String descrId = getGUID();
            pss.stm_underscoreinsAgentDescr.setString(1, descrId);
            pss.stm_underscoreinsAgentDescr.setString(2, agentName);
            pss.stm_underscoreinsAgentDescr.setString(3, String.valueOf(lt));
            pss.stm_underscoreinsAgentDescr.executeUpdate();
            saveAID(agentAID);
            Iterator iter = dfd.getAllLanguages();
            if (iter.hasNext()) {
                pss.stm_underscoreinsLanguage.clearBatch();
                while (iter.hasNext()) {
                    pss.stm_underscoreinsLanguage.setString(1, descrId);
                    pss.stm_underscoreinsLanguage.setString(2, (String) iter.next());
                    pss.stm_underscoreinsLanguage.addBatch();
                }
                pss.stm_underscoreinsLanguage.executeBatch();
            }
            iter = dfd.getAllOntologies();
            if (iter.hasNext()) {
                pss.stm_underscoreinsOntology.clearBatch();
                while (iter.hasNext()) {
                    pss.stm_underscoreinsOntology.setString(1, descrId);
                    pss.stm_underscoreinsOntology.setString(2, (String) iter.next());
                    pss.stm_underscoreinsOntology.addBatch();
                }
                pss.stm_underscoreinsOntology.executeBatch();
            }
            iter = dfd.getAllProtocols();
            if (iter.hasNext()) {
                pss.stm_underscoreinsProtocol.clearBatch();
                while (iter.hasNext()) {
                    pss.stm_underscoreinsProtocol.setString(1, descrId);
                    pss.stm_underscoreinsProtocol.setString(2, (String) iter.next());
                    pss.stm_underscoreinsProtocol.addBatch();
                }
                pss.stm_underscoreinsProtocol.executeBatch();
            }
            saveServices(descrId, dfd.getAllServices());
            regsCnt++;
            if (regsCnt > MAX_underscoreREGISTER_underscoreWITHOUT_underscoreCLEAN) {
                regsCnt = 0;
                clean();
            }
            conn.commit();
        } catch (SQLException sqle) {
            try {
                conn.rollback();
            } catch (SQLException se) {
                logger.log(Logger.SEVERE, "Rollback for incomplete insertion of DFD for agent " + dfd.getName() + " failed.", se);
            }
            throw sqle;
        }
        return dfdToReturn;
    }

