    public Long processAddHolding(Holding holdingBean, AuthSession authSession) {
        if (authSession == null) {
            return null;
        }
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_underscoreWM_underscoreLIST_underscoreHOLDING");
            seq.setTableName("WM_underscoreLIST_underscoreHOLDING");
            seq.setColumnName("ID_underscoreHOLDING");
            Long sequenceValue = dbDyn.getSequenceNextValue(seq);
            ps = dbDyn.prepareStatement("insert into WM_underscoreLIST_underscoreHOLDING " + "( ID_underscoreHOLDING, full_underscorename_underscoreHOLDING, NAME_underscoreHOLDING )" + "values " + (dbDyn.getIsNeedUpdateBracket() ? "(" : "") + " ?, ?, ? " + (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));
            int num = 1;
            RsetTools.setLong(ps, num++, sequenceValue);
            ps.setString(num++, holdingBean.getName());
            ps.setString(num++, holdingBean.getShortName());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of inserted records - " + i1);
            HoldingBean bean = new HoldingBean(holdingBean);
            bean.setId(sequenceValue);
            processInsertRelatedCompany(dbDyn, bean, authSession);
            dbDyn.commit();
            return sequenceValue;
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error add new holding";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

