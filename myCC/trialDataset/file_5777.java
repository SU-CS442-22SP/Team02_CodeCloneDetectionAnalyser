    private void duplicateIndices(Connection scon, Connection dcon, String table) {
        try {
            String idx_underscorename, idx_underscoreatt, query;
            ResultSet idxs = scon.getMetaData().getIndexInfo(null, null, table, false, false);
            Statement stmt = dcon.createStatement();
            while (idxs.next()) {
                idx_underscorename = idxs.getString(6);
                idx_underscoreatt = idxs.getString(9);
                idx_underscorename += "_underscore" + idx_underscoreatt + "_underscoreidx";
                logger.debug("Creating index " + idx_underscorename);
                query = "CREATE INDEX " + idx_underscorename + " ON " + table + "(" + idx_underscoreatt + ")";
                stmt.executeUpdate(query);
                dcon.commit();
            }
        } catch (Exception e) {
            logger.error("Unable to copy indices " + e);
            try {
                dcon.rollback();
            } catch (SQLException e1) {
                logger.fatal(e1);
            }
        }
    }

