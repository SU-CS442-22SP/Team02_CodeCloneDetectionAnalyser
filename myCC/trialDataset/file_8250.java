    public void updateUser(User portalUserBean, AuthSession authSession) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            String sql = "update WM_underscoreLIST_underscoreUSER " + "set    FIRST_underscoreNAME=?,MIDDLE_underscoreNAME=?,LAST_underscoreNAME=?, " + "       ADDRESS=?,TELEPHONE=?,EMAIL=? " + "where  ID_underscoreUSER=? and is_underscoredeleted=0 and  ID_underscoreFIRM in ";
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
            ps.setString(num++, portalUserBean.getFirstName());
            ps.setString(num++, portalUserBean.getMiddleName());
            ps.setString(num++, portalUserBean.getLastName());
            ps.setString(num++, portalUserBean.getAddress());
            ps.setString(num++, portalUserBean.getPhone());
            ps.setString(num++, portalUserBean.getEmail());
            ps.setLong(num++, portalUserBean.getUserId());
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
                if (dbDyn != null) {
                    dbDyn.rollback();
                }
            } catch (Exception e001) {
            }
            String es = "Error update of portal user";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

