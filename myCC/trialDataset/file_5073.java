    protected void doBackupOrganizeType() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strSelQuery = "SELECT organize_underscoretype_underscoreid,organize_underscoretype_underscorename,width " + "FROM " + Common.ORGANIZE_underscoreTYPE_underscoreTABLE;
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_underscoreTYPE_underscoreB_underscoreTABLE + " " + "(version_underscoreno,organize_underscoretype_underscoreid,organize_underscoretype_underscorename,width) " + "VALUES (?,?,?,?)";
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
                    ps.setString(2, result.getString("organize_underscoretype_underscoreid"));
                    ps.setString(3, result.getString("organize_underscoretype_underscorename"));
                    ps.setInt(4, result.getInt("width"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeType(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE_underscoreB_underscoreTYPE INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeType(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_underscorebackup.doBackupOrganizeType(): SQLException while committing or rollback");
        }
    }

