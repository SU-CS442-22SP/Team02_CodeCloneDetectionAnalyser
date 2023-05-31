    public void updatePortletName(PortletNameBean portletNameBean) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            String sql = "update WM_underscorePORTAL_underscorePORTLET_underscoreNAME " + "set    TYPE=? " + "where  ID_underscoreSITE_underscoreCTX_underscoreTYPE=?";
            ps = dbDyn.prepareStatement(sql);
            ps.setString(1, portletNameBean.getPortletName());
            RsetTools.setLong(ps, 2, portletNameBean.getPortletId());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of updated record - " + i1);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error save portlet name";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

