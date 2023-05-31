    private void handleNodeRegainedService(long eventID, long nodeID, String ipAddr, long serviceID, String eventTime) {
        Category log = ThreadCategory.getInstance(OutageWriter.class);
        if (eventID == -1 || nodeID == -1 || ipAddr == null || serviceID == -1) {
            log.warn(EventConstants.NODE_underscoreREGAINED_underscoreSERVICE_underscoreEVENT_underscoreUEI + " ignored - info incomplete - eventid/nodeid/ip/svc: " + eventID + "/" + nodeID + "/" + ipAddr + "/" + serviceID);
            return;
        }
        Connection dbConn = null;
        try {
            dbConn = DatabaseConnectionFactory.getInstance().getConnection();
            if (openOutageExists(dbConn, nodeID, ipAddr, serviceID)) {
                try {
                    dbConn.setAutoCommit(false);
                } catch (SQLException sqle) {
                    log.error("Unable to change database AutoCommit to FALSE", sqle);
                    return;
                }
                PreparedStatement outageUpdater = dbConn.prepareStatement(OutageConstants.DB_underscoreUPDATE_underscoreOUTAGE_underscoreFOR_underscoreSERVICE);
                outageUpdater.setLong(1, eventID);
                outageUpdater.setTimestamp(2, convertEventTimeIntoTimestamp(eventTime));
                outageUpdater.setLong(3, nodeID);
                outageUpdater.setString(4, ipAddr);
                outageUpdater.setLong(5, serviceID);
                outageUpdater.executeUpdate();
                outageUpdater.close();
                try {
                    dbConn.commit();
                    if (log.isDebugEnabled()) log.debug("nodeRegainedService: closed outage for nodeid/ip/service " + nodeID + "/" + ipAddr + "/" + serviceID + " in DB");
                } catch (SQLException se) {
                    log.warn("Rolling back transaction, nodeRegainedService could not be recorded  for nodeId/ipAddr/service: " + nodeID + "/" + ipAddr + "/" + serviceID, se);
                    try {
                        dbConn.rollback();
                    } catch (SQLException sqle) {
                        log.warn("SQL exception during rollback, reason", sqle);
                    }
                }
            } else {
                log.warn("\'" + EventConstants.NODE_underscoreREGAINED_underscoreSERVICE_underscoreEVENT_underscoreUEI + "\' for " + nodeID + "/" + ipAddr + "/" + serviceID + " does not have open record.");
            }
        } catch (SQLException se) {
            log.warn("SQL exception while handling \'nodeRegainedService\'", se);
        } finally {
            try {
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                log.warn("Exception closing JDBC connection", e);
            }
        }
    }

