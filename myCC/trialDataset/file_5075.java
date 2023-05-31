    protected void doBackupOrganize() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strSelQuery = "SELECT organize_underscoreid,organize_underscoretype_underscoreid,organize_underscorename,organize_underscoremanager," + "organize_underscoredescribe,work_underscoretype,show_underscoreorder,position_underscorex,position_underscorey " + "FROM " + Common.ORGANIZE_underscoreTABLE;
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_underscoreB_underscoreTABLE + " " + "(version_underscoreno,organize_underscoreid,organize_underscoretype_underscoreid,organize_underscorename,organize_underscoremanager," + "organize_underscoredescribe,work_underscoretype,show_underscoreorder,position_underscorex,position_underscorey) " + "VALUES (?,?,?,?,?,?,?,?,?,?)";
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
                    ps.setString(4, result.getString("organize_underscorename"));
                    ps.setString(5, result.getString("organize_underscoremanager"));
                    ps.setString(6, result.getString("organize_underscoredescribe"));
                    ps.setString(7, result.getString("work_underscoretype"));
                    ps.setInt(8, result.getInt("show_underscoreorder"));
                    ps.setInt(9, result.getInt("position_underscorex"));
                    ps.setInt(10, result.getInt("position_underscorey"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_underscorebackup.doBackupOrganize(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE_underscoreB INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_underscorebackup.doBackupOrganize(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_underscorebackup.doBackupOrganize(): SQLException while committing or rollback");
        }
    }

