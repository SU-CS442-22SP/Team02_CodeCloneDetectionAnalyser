    public void processAction(DatabaseAdapter db_underscore, DataDefinitionActionDataListType parameters) throws Exception {
        PreparedStatement ps = null;
        try {
            if (log.isDebugEnabled()) log.debug("db connect - " + db_underscore.getClass().getName());
            String seqName = DefinitionService.getString(parameters, "sequence_underscorename", null);
            if (seqName == null) {
                String errorString = "Name of sequnce not found";
                log.error(errorString);
                throw new Exception(errorString);
            }
            String tableName = DefinitionService.getString(parameters, "name_underscoretable", null);
            if (tableName == null) {
                String errorString = "Name of table not found";
                log.error(errorString);
                throw new Exception(errorString);
            }
            String columnName = DefinitionService.getString(parameters, "name_underscorepk_underscorefield", null);
            if (columnName == null) {
                String errorString = "Name of column not found";
                log.error(errorString);
                throw new Exception(errorString);
            }
            CustomSequenceType seqSite = new CustomSequenceType();
            seqSite.setSequenceName(seqName);
            seqSite.setTableName(tableName);
            seqSite.setColumnName(columnName);
            long seqValue = db_underscore.getSequenceNextValue(seqSite);
            String valueColumnName = DefinitionService.getString(parameters, "name_underscorevalue_underscorefield", null);
            if (columnName == null) {
                String errorString = "Name of valueColumnName not found";
                log.error(errorString);
                throw new Exception(errorString);
            }
            String insertValue = DefinitionService.getString(parameters, "insert_underscorevalue", null);
            if (columnName == null) {
                String errorString = "Name of insertValue not found";
                log.error(errorString);
                throw new Exception(errorString);
            }
            String sql = "insert into " + tableName + " " + "(" + columnName + "," + valueColumnName + ")" + "values" + "(?,?)";
            if (log.isDebugEnabled()) {
                log.debug(sql);
                log.debug("pk " + seqValue);
                log.debug("value " + insertValue);
            }
            ps = db_underscore.prepareStatement(sql);
            ps.setLong(1, seqValue);
            ps.setString(2, insertValue);
            ps.executeUpdate();
            db_underscore.commit();
        } catch (Exception e) {
            try {
                db_underscore.rollback();
            } catch (Exception e1) {
            }
            log.error("Error insert value", e);
            throw e;
        } finally {
            org.riverock.generic.db.DatabaseManager.close(ps);
            ps = null;
        }
    }

