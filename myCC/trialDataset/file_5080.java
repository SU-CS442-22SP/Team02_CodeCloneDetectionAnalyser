    protected void doRestoreOrganizeRelation() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strDelQuery = "DELETE FROM " + Common.ORGANIZE_underscoreRELATION_underscoreTABLE;
        String strSelQuery = "SELECT organize_underscoreid,organize_underscoretype_underscoreid,child_underscoreid,child_underscoretype_underscoreid,remark " + "FROM " + Common.ORGANIZE_underscoreRELATION_underscoreB_underscoreTABLE + " " + "WHERE version_underscoreno = ?";
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_underscoreRELATION_underscoreTABLE + " " + "(organize_underscoreid,organize_underscoretype,child_underscoreid,child_underscoretype,remark) " + "VALUES (?,?,?,?,?)";
        DBOperation dbo = factory.createDBOperation(POOL_underscoreNAME);
        try {
            try {
                con = dbo.getConnection();
                con.setAutoCommit(false);
                ps = con.prepareStatement(strDelQuery);
                ps.executeUpdate();
                ps = con.prepareStatement(strSelQuery);
                ps.setInt(1, this.versionNO);
                result = ps.executeQuery();
                ps = con.prepareStatement(strInsQuery);
                while (result.next()) {
                    ps.setString(1, result.getString("organize_underscoreid"));
                    ps.setString(2, result.getString("organize_underscoretype_underscoreid"));
                    ps.setString(3, result.getString("child_underscoreid"));
                    ps.setString(4, result.getString("child_underscoretype_underscoreid"));
                    ps.setString(5, result.getString("remark"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeRelation(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE_underscoreRELATION INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeRelation(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeRelation(): SQLException while committing or rollback");
        }
    }

