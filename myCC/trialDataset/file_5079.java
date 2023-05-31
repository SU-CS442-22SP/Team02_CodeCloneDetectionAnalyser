    protected void doRestoreOrganize() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strDelQuery = "DELETE FROM " + Common.ORGANIZE_underscoreTABLE;
        String strSelQuery = "SELECT organize_underscoreid,organize_underscoretype_underscoreid,organize_underscorename,organize_underscoremanager," + "organize_underscoredescribe,work_underscoretype,show_underscoreorder,position_underscorex,position_underscorey " + "FROM " + Common.ORGANIZE_underscoreB_underscoreTABLE + " " + "WHERE version_underscoreno = ?";
        String strInsQuery = "INSERT INTO " + Common.ORGANIZE_underscoreTABLE + " " + "(organize_underscoreid,organize_underscoretype_underscoreid,organize_underscorename,organize_underscoremanager," + "organize_underscoredescribe,work_underscoretype,show_underscoreorder,position_underscorex,position_underscorey) " + "VALUES (?,?,?,?,?,?,?,?,?)";
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
                    ps.setString(3, result.getString("organize_underscorename"));
                    ps.setString(4, result.getString("organize_underscoremanager"));
                    ps.setString(5, result.getString("organize_underscoredescribe"));
                    ps.setString(6, result.getString("work_underscoretype"));
                    ps.setInt(7, result.getInt("show_underscoreorder"));
                    ps.setInt(8, result.getInt("position_underscorex"));
                    ps.setInt(9, result.getInt("position_underscorey"));
                    int resultCount = ps.executeUpdate();
                    if (resultCount != 1) {
                        con.rollback();
                        throw new CesSystemException("Organize_underscorebackup.doRestoreOrganize(): ERROR Inserting data " + "in T_underscoreSYS_underscoreORGANIZE INSERT !! resultCount = " + resultCount);
                    }
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("Organize_underscorebackup.doRestoreOrganize(): SQLException:  " + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Organize_underscorebackup.doRestoreOrganize(): SQLException while committing or rollback");
        }
    }

