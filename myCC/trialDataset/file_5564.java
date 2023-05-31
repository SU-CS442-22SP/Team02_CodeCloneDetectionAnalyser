    public void doUpdateByLoginID() throws Exception {
        if (!isValidate()) {
            throw new CesSystemException("User_underscoresession.doUpdateByLoginID(): Illegal data values for update");
        }
        Connection con = null;
        PreparedStatement ps = null;
        String strQuery = "UPDATE " + Common.USER_underscoreSESSION_underscoreTABLE + " SET " + "session_underscoreid = ?, user_underscoreid = ?, begin_underscoredate = ? , " + "ip_underscoreaddress = ?, mac_underscoreno = ? " + "WHERE  login_underscoreid= ?";
        DBOperation dbo = factory.createDBOperation(POOL_underscoreNAME);
        try {
            con = dbo.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(strQuery);
            ps.setString(1, this.sessionID);
            ps.setInt(2, this.user.getUserID());
            ps.setTimestamp(3, this.beginDate);
            ps.setString(4, this.ipAddress);
            ps.setString(5, this.macNO);
            ps.setString(6, this.loginID);
            int resultCount = ps.executeUpdate();
            if (resultCount != 1) {
                con.rollback();
                throw new CesSystemException("User_underscoresession.doUpdateByLoginID(): ERROR updating data in T_underscoreSYS_underscoreUSER_underscoreSESSION!! " + "resultCount = " + resultCount);
            }
            con.commit();
        } catch (SQLException se) {
            if (con != null) {
                con.rollback();
            }
            throw new CesSystemException("User_underscoresession.doUpdateByLoginID(): SQLException while updating user_underscoresession; " + "session_underscoreid = " + this.sessionID + " :\n\t" + se);
        } finally {
            con.setAutoCommit(true);
            closePreparedStatement(ps);
            closeConnection(dbo);
        }
    }

