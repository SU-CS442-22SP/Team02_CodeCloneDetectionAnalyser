    protected void doBackupOrganizeRelation() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strSelQuery = "SELECT organize_underscoreid,organize_underscoretype_underscoreid,child_underscoreid,child_underscoretype_underscoreid,remark " + "FROM " + Common.ORGANIZE_underscoreRELATION_underscoreTABLE;
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_underscoreRELATION_underscoreB_underscoreTABLE + " " + "(version_underscoreno,organize_underscoreid,organize_underscoretype,child_underscoreid,child_underscoretype,remark) " + "VALUES (?,?,?,?,?,?)";
        DBOperation dbo = factory.createDBOperation(POOL_underscoreNAME);
        try {
            try {
                con = dbo.getConnection();
                con.setAutoCommit(false);
                ps = con.prepareStatement(strSelQuery);
                result = ps.executeQuery();
                ps = con.prepareStatement(strInsQuery);
                while (result.next()) {
                    ps.setInt(1, this.versionNO);
                    ps.setString(2, result.getString("organize_underscoreid"));
                    ps.setString(3, result.getString("organize_underscoretype_underscoreid"));
                    ps.setString(4, result.getString("child_underscoreid"));
                    ps.setString(5, result.getString("child_underscoretype_underscoreid"));
                    ps.setString(6, result.getString("remark"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeRelation(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE_underscoreRELATION_underscoreB INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeRelation(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeRelation(): SQLException while committing or rollback");
        }
    }

