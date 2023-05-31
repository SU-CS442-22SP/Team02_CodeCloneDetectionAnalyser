    public void deleteUser(User portalUserBean, AuthSession authSession) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            if (portalUserBean.getUserId() == null) throw new IllegalArgumentException("id of portal user is null");
            String sql = "update WM_underscoreLIST_underscoreUSER " + "set    is_underscoredeleted=1 " + "where  ID_underscoreUSER=? and is_underscoredeleted = 0 and ID_underscoreFIRM in ";
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
            ps.setLong(num++, portalUserBean.getUserId());
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_underscoreFAMALY:
                    break;
                default:
                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of deleted records - " + i1);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                if (dbDyn != null) {
                    dbDyn.rollback();
                }
            } catch (Exception e001) {
            }
            String es = "Error delete of portal user";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

