    private void insert(Connection c) throws SQLException {
        if (m_underscorefromDb) throw new IllegalStateException("The record already exists in the database");
        StringBuffer names = new StringBuffer("INSERT INTO ifServices (nodeID,ipAddr,serviceID");
        StringBuffer values = new StringBuffer("?,?,?");
        if ((m_underscorechanged & CHANGED_underscoreIFINDEX) == CHANGED_underscoreIFINDEX) {
            values.append(",?");
            names.append(",ifIndex");
        }
        if ((m_underscorechanged & CHANGED_underscoreSTATUS) == CHANGED_underscoreSTATUS) {
            values.append(",?");
            names.append(",status");
        }
        if ((m_underscorechanged & CHANGED_underscoreLASTGOOD) == CHANGED_underscoreLASTGOOD) {
            values.append(",?");
            names.append(",lastGood");
        }
        if ((m_underscorechanged & CHANGED_underscoreLASTFAIL) == CHANGED_underscoreLASTFAIL) {
            values.append(",?");
            names.append(",lastFail");
        }
        if ((m_underscorechanged & CHANGED_underscoreSOURCE) == CHANGED_underscoreSOURCE) {
            values.append(",?");
            names.append(",source");
        }
        if ((m_underscorechanged & CHANGED_underscoreNOTIFY) == CHANGED_underscoreNOTIFY) {
            values.append(",?");
            names.append(",notify");
        }
        if ((m_underscorechanged & CHANGED_underscoreQUALIFIER) == CHANGED_underscoreQUALIFIER) {
            values.append(",?");
            names.append(",qualifier");
        }
        names.append(") VALUES (").append(values).append(')');
        if (log().isDebugEnabled()) log().debug("DbIfServiceEntry.insert: SQL insert statment = " + names.toString());
        PreparedStatement stmt = null;
        PreparedStatement delStmt = null;
        final DBUtils d = new DBUtils(getClass());
        try {
            stmt = c.prepareStatement(names.toString());
            d.watch(stmt);
            names = null;
            int ndx = 1;
            stmt.setInt(ndx++, m_underscorenodeId);
            stmt.setString(ndx++, m_underscoreipAddr.getHostAddress());
            stmt.setInt(ndx++, m_underscoreserviceId);
            if ((m_underscorechanged & CHANGED_underscoreIFINDEX) == CHANGED_underscoreIFINDEX) stmt.setInt(ndx++, m_underscoreifIndex);
            if ((m_underscorechanged & CHANGED_underscoreSTATUS) == CHANGED_underscoreSTATUS) stmt.setString(ndx++, new String(new char[] { m_underscorestatus }));
            if ((m_underscorechanged & CHANGED_underscoreLASTGOOD) == CHANGED_underscoreLASTGOOD) {
                stmt.setTimestamp(ndx++, m_underscorelastGood);
            }
            if ((m_underscorechanged & CHANGED_underscoreLASTFAIL) == CHANGED_underscoreLASTFAIL) {
                stmt.setTimestamp(ndx++, m_underscorelastFail);
            }
            if ((m_underscorechanged & CHANGED_underscoreSOURCE) == CHANGED_underscoreSOURCE) stmt.setString(ndx++, new String(new char[] { m_underscoresource }));
            if ((m_underscorechanged & CHANGED_underscoreNOTIFY) == CHANGED_underscoreNOTIFY) stmt.setString(ndx++, new String(new char[] { m_underscorenotify }));
            if ((m_underscorechanged & CHANGED_underscoreQUALIFIER) == CHANGED_underscoreQUALIFIER) stmt.setString(ndx++, m_underscorequalifier);
            int rc;
            try {
                rc = stmt.executeUpdate();
            } catch (SQLException e) {
                log().warn("ifServices DB insert got exception; will retry after " + "deletion of any existing records for this ifService " + "that are marked for deletion.", e);
                c.rollback();
                String delCmd = "DELETE FROM ifServices WHERE status = 'D' " + "AND nodeid = ? AND ipAddr = ? AND serviceID = ?";
                delStmt = c.prepareStatement(delCmd);
                d.watch(delStmt);
                delStmt.setInt(1, m_underscorenodeId);
                delStmt.setString(2, m_underscoreipAddr.getHostAddress());
                delStmt.setInt(3, m_underscoreserviceId);
                rc = delStmt.executeUpdate();
                rc = stmt.executeUpdate();
            }
            log().debug("insert(): SQL update result = " + rc);
        } finally {
            d.cleanUp();
        }
        m_underscorefromDb = true;
        m_underscorechanged = 0;
    }

