    protected void doBackupOrganizeTypeRelation() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strSelQuery = "SELECT parent_underscoreorganize_underscoretype,child_underscoreorganize_underscoretype " + "FROM " + Common.ORGANIZE_underscoreTYPE_underscoreRELATION_underscoreTABLE;
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_underscoreTYPE_underscoreRELATION_underscoreB_underscoreTABLE + " " + "(version_underscoreno,parent_underscoreorganize_underscoretype,child_underscoreorganize_underscoretype) " + "VALUES (?,?,?)";
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
                    ps.setString(2, result.getString("parent_underscoreorganize_underscoretype"));
                    ps.setString(3, result.getString("child_underscoreorganize_underscoretype"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeTypeRelation(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE_underscoreTYPE_underscoreRELATION_underscoreB INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeTypeRelation(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeTypeRelation(): SQLException while committing or rollback");
        }
    }

