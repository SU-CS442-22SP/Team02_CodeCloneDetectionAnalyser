    public Long addRole(AuthSession authSession, RoleBean roleBean) {
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_underscoreWM_underscoreAUTH_underscoreACCESS_underscoreGROUP");
            seq.setTableName("WM_underscoreAUTH_underscoreACCESS_underscoreGROUP");
            seq.setColumnName("ID_underscoreACCESS_underscoreGROUP");
            Long sequenceValue = dbDyn.getSequenceNextValue(seq);
            ps = dbDyn.prepareStatement("insert into WM_underscoreAUTH_underscoreACCESS_underscoreGROUP " + "( ID_underscoreACCESS_underscoreGROUP, NAME_underscoreACCESS_underscoreGROUP ) values " + (dbDyn.getIsNeedUpdateBracket() ? "(" : "") + " ?, ? " + (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));
            RsetTools.setLong(ps, 1, sequenceValue);
            ps.setString(2, roleBean.getName());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of inserted records - " + i1);
            dbDyn.commit();
            return sequenceValue;
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error add new role";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

