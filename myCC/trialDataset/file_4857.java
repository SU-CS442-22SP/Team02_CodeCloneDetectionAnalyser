    public void moveNext(String[] showOrder, String[] orgID, String targetShowOrder, String targetOrgID) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        int moveCount = showOrder.length;
        DBOperation dbo = factory.createDBOperation(POOL_underscoreNAME);
        String strQuery = "select show_underscoreorder from " + Common.ORGANIZE_underscoreTABLE + " where show_underscoreorder=" + showOrder[0] + " and organize_underscoreid= '" + orgID[0] + "'";
        try {
            con = dbo.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(strQuery);
            result = ps.executeQuery();
            int minOrderNo = 0;
            if (result.next()) {
                minOrderNo = result.getInt(1);
            }
            String[] sqls = new String[moveCount + 1];
            sqls[0] = "update " + Common.ORGANIZE_underscoreTABLE + " set show_underscoreorder=" + minOrderNo + " where show_underscoreorder=" + targetShowOrder + " and organize_underscoreid= '" + targetOrgID + "'";
            for (int i = 0; i < showOrder.length; i++) {
                sqls[i + 1] = "update " + Common.ORGANIZE_underscoreTABLE + " set show_underscoreorder=show_underscoreorder+1" + " where show_underscoreorder=" + showOrder[i] + " and organize_underscoreid= '" + orgID[i] + "'";
            }
            for (int j = 0; j < sqls.length; j++) {
                ps = con.prepareStatement(sqls[j]);
                int resultCount = ps.executeUpdate();
                if (resultCount != 1) {
                    throw new CesSystemException("Organize.moveNext(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE update !! resultCount = " + resultCount);
                }
            }
            con.commit();
        } catch (SQLException se) {
            if (con != null) {
                con.rollback();
            }
            throw new CesSystemException("Organize.moveNext(): SQLException while mov organize order " + " :\n\t" + se);
        } finally {
            con.setAutoCommit(true);
            close(dbo, ps, result);
        }
    }

