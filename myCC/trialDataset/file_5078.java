    protected void doRestoreOrganizeTypeRelation() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strDelQuery = "DELETE FROM " + Common.ORGANIZE_underscoreTYPE_underscoreRELATION_underscoreTABLE;
        String strSelQuery = "SELECT parent_underscoreorganize_underscoretype,child_underscoreorganize_underscoretype " + "FROM " + Common.ORGANIZE_underscoreTYPE_underscoreRELATION_underscoreB_underscoreTABLE + " " + "WHERE version_underscoreno = ?";
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_underscoreTYPE_underscoreRELATION_underscoreTABLE + " " + "(parent_underscoreorganize_underscoretype,child_underscoreorganize_underscoretype) " + "VALUES (?,?)";
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
                    ps.setString(1, result.getString("parent_underscoreorganize_underscoretype"));
                    ps.setString(2, result.getString("child_underscoreorganize_underscoretype"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeTypeRelation(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE_underscoreTYPE_underscoreRELATION INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeTypeRelation(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeTypeRelation(): SQLException while committing or rollback");
        }
    }

