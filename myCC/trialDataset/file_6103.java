    public Long processAddCompany(Company companyBean, Long holdingId) {
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_underscoreWM_underscoreLIST_underscoreCOMPANY");
            seq.setTableName("WM_underscoreLIST_underscoreCOMPANY");
            seq.setColumnName("ID_underscoreFIRM");
            Long sequenceValue = dbDyn.getSequenceNextValue(seq);
            ps = dbDyn.prepareStatement("insert into WM_underscoreLIST_underscoreCOMPANY (" + "	ID_underscoreFIRM, " + "	full_underscorename, " + "	short_underscorename, " + "	address, " + "	chief, " + "	buh, " + "	url, " + "	short_underscoreinfo, " + "   is_underscoredeleted" + ")values " + (dbDyn.getIsNeedUpdateBracket() ? "(" : "") + "	?," + "	?," + "	?," + "	?," + "	?," + "	?," + "	?," + "	?," + "   0 " + (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));
            int num = 1;
            RsetTools.setLong(ps, num++, sequenceValue);
            ps.setString(num++, companyBean.getName());
            ps.setString(num++, companyBean.getShortName());
            ps.setString(num++, companyBean.getAddress());
            ps.setString(num++, companyBean.getCeo());
            ps.setString(num++, companyBean.getCfo());
            ps.setString(num++, companyBean.getWebsite());
            ps.setString(num++, companyBean.getInfo());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of inserted records - " + i1);
            if (holdingId != null) {
                InternalDaoFactory.getInternalHoldingDao().setRelateHoldingCompany(dbDyn, holdingId, sequenceValue);
            }
            dbDyn.commit();
            return sequenceValue;
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error add new company";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

