    public void processSaveCompany(Company companyBean, AuthSession authSession) {
        if (authSession == null) {
            return;
        }
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            String sql = "UPDATE WM_underscoreLIST_underscoreCOMPANY " + "SET " + "	full_underscorename = ?, " + "	short_underscorename = ?, " + "	address = ?, " + "	telefon_underscorebuh = ?, " + "	telefon_underscorechief = ?, " + "	chief = ?, " + "	buh = ?, " + "	fax = ?, " + "	email = ?, " + "	icq = ?, " + "	short_underscoreclient_underscoreinfo = ?, " + "	url = ?, " + "	short_underscoreinfo = ? " + "WHERE ID_underscoreFIRM = ? and ID_underscoreFIRM in ";
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_underscoreFAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    sql += " (" + idList + ") ";
                    break;
                default:
                    sql += "(select z1.ID_underscoreFIRM from v$_underscoreread_underscorelist_underscorefirm z1 where z1.user_underscorelogin = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement(sql);
            int num = 1;
            ps.setString(num++, companyBean.getName());
            ps.setString(num++, companyBean.getShortName());
            ps.setString(num++, companyBean.getAddress());
            ps.setString(num++, "");
            ps.setString(num++, "");
            ps.setString(num++, companyBean.getCeo());
            ps.setString(num++, companyBean.getCfo());
            ps.setString(num++, "");
            ps.setString(num++, "");
            RsetTools.setLong(ps, num++, null);
            ps.setString(num++, "");
            ps.setString(num++, companyBean.getWebsite());
            ps.setString(num++, companyBean.getInfo());
            RsetTools.setLong(ps, num++, companyBean.getId());
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_underscoreFAMALY:
                    break;
                default:
                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of updated record - " + i1);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error save company";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

