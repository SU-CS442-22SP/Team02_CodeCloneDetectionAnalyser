    protected void doRestoreOrganizeType() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strDelQuery = "DELETE FROM " + Common.ORGANIZE_underscoreTYPE_underscoreTABLE;
        String strSelQuery = "SELECT organize_underscoretype_underscoreid,organize_underscoretype_underscorename,width " + "FROM " + Common.ORGANIZE_underscoreTYPE_underscoreB_underscoreTABLE + " " + "WHERE version_underscoreno = ?";
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_underscoreTYPE_underscoreTABLE + " " + "(organize_underscoretype_underscoreid,organize_underscoretype_underscorename,width) " + "VALUES (?,?,?)";
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
                    ps.setString(1, result.getString("organize_underscoretype_underscoreid"));
                    ps.setString(2, result.getString("organize_underscoretype_underscorename"));
                    ps.setInt(3, result.getInt("width"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeType(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE_underscoreTYPE INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeType(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_underscorebackup.doRestoreOrganizeType(): SQLException while committing or rollback");
        }
    }

