    private void handleInterfaceReparented(String ipAddr, Parms eventParms) {
        Category log = ThreadCategory.getInstance(OutageWriter.class);
        if (log.isDebugEnabled()) log.debug("interfaceReparented event received...");
        if (ipAddr == null || eventParms == null) {
            log.warn(EventConstants.INTERFACE_underscoreREPARENTED_underscoreEVENT_underscoreUEI + " ignored - info incomplete - ip/parms: " + ipAddr + "/" + eventParms);
            return;
        }
        long oldNodeId = -1;
        long newNodeId = -1;
        String parmName = null;
        Value parmValue = null;
        String parmContent = null;
        Enumeration parmEnum = eventParms.enumerateParm();
        while (parmEnum.hasMoreElements()) {
            Parm parm = (Parm) parmEnum.nextElement();
            parmName = parm.getParmName();
            parmValue = parm.getValue();
            if (parmValue == null) continue; else parmContent = parmValue.getContent();
            if (parmName.equals(EventConstants.PARM_underscoreOLD_underscoreNODEID)) {
                try {
                    oldNodeId = Integer.valueOf(parmContent).intValue();
                } catch (NumberFormatException nfe) {
                    log.warn("Parameter " + EventConstants.PARM_underscoreOLD_underscoreNODEID + " cannot be non-numeric");
                    oldNodeId = -1;
                }
            } else if (parmName.equals(EventConstants.PARM_underscoreNEW_underscoreNODEID)) {
                try {
                    newNodeId = Integer.valueOf(parmContent).intValue();
                } catch (NumberFormatException nfe) {
                    log.warn("Parameter " + EventConstants.PARM_underscoreNEW_underscoreNODEID + " cannot be non-numeric");
                    newNodeId = -1;
                }
            }
        }
        if (newNodeId == -1 || oldNodeId == -1) {
            log.warn("Unable to process 'interfaceReparented' event, invalid event parm.");
            return;
        }
        Connection dbConn = null;
        try {
            dbConn = DatabaseConnectionFactory.getInstance().getConnection();
            try {
                dbConn.setAutoCommit(false);
            } catch (SQLException sqle) {
                log.error("Unable to change database AutoCommit to FALSE", sqle);
                return;
            }
            PreparedStatement reparentOutagesStmt = dbConn.prepareStatement(OutageConstants.DB_underscoreREPARENT_underscoreOUTAGES);
            reparentOutagesStmt.setLong(1, newNodeId);
            reparentOutagesStmt.setLong(2, oldNodeId);
            reparentOutagesStmt.setString(3, ipAddr);
            int count = reparentOutagesStmt.executeUpdate();
            try {
                dbConn.commit();
                if (log.isDebugEnabled()) log.debug("Reparented " + count + " outages - ip: " + ipAddr + " reparented from " + oldNodeId + " to " + newNodeId);
            } catch (SQLException se) {
                log.warn("Rolling back transaction, reparent outages failed for newNodeId/ipAddr: " + newNodeId + "/" + ipAddr);
                try {
                    dbConn.rollback();
                } catch (SQLException sqle) {
                    log.warn("SQL exception during rollback, reason", sqle);
                }
            }
            reparentOutagesStmt.close();
        } catch (SQLException se) {
            log.warn("SQL exception while handling \'interfaceReparented\'", se);
        } finally {
            try {
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                log.warn("Exception closing JDBC connection", e);
            }
        }
    }

