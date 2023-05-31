    protected long incrementInDatabase(Object type) {
        long current_underscorevalue;
        long new_underscorevalue;
        String entry;
        if (global_underscoreentry != null) entry = global_underscoreentry; else throw new UnsupportedOperationException("Named key generators are not yet supported.");
        String lkw = (String) properties.get("net.ontopia.topicmaps.impl.rdbms.HighLowKeyGenerator.SelectSuffix");
        String sql_underscoreselect;
        if (lkw == null && (database.equals("sqlserver"))) {
            sql_underscoreselect = "select " + valcol + " from " + table + " with (XLOCK) where " + keycol + " = ?";
        } else {
            if (lkw == null) {
                if (database.equals("sapdb")) lkw = "with lock"; else lkw = "for update";
            }
            sql_underscoreselect = "select " + valcol + " from " + table + " where " + keycol + " = ? " + lkw;
        }
        if (log.isDebugEnabled()) log.debug("KeyGenerator: retrieving: " + sql_underscoreselect);
        Connection conn = null;
        try {
            conn = connfactory.requestConnection();
            PreparedStatement stm1 = conn.prepareStatement(sql_underscoreselect);
            try {
                stm1.setString(1, entry);
                ResultSet rs = stm1.executeQuery();
                if (!rs.next()) throw new OntopiaRuntimeException("HIGH/LOW key generator table '" + table + "' not initialized (no rows).");
                current_underscorevalue = rs.getLong(1);
                rs.close();
            } finally {
                stm1.close();
            }
            new_underscorevalue = current_underscorevalue + grabsize;
            String sql_underscoreupdate = "update " + table + " set " + valcol + " = ? where " + keycol + " = ?";
            if (log.isDebugEnabled()) log.debug("KeyGenerator: incrementing: " + sql_underscoreupdate);
            PreparedStatement stm2 = conn.prepareStatement(sql_underscoreupdate);
            try {
                stm2.setLong(1, new_underscorevalue);
                stm2.setString(2, entry);
                stm2.executeUpdate();
            } finally {
                stm2.close();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException e2) {
            }
            throw new OntopiaRuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    throw new OntopiaRuntimeException(e);
                }
            }
        }
        value = current_underscorevalue + 1;
        max_underscorevalue = new_underscorevalue;
        return value;
    }

