    public void doUpdateByIP() throws Exception {
        if (!isValidate()) {
            throw new CesSystemException("User_underscoresession.doUpdateByIP(): Illegal data values for update");
        }
        Connection con = null;
        PreparedStatement ps = null;
        String strQuery = "UPDATE " + Common.USER_underscoreSESSION_underscoreTABLE + " SET " + "session_underscoreid = ?, user_underscoreid = ?, begin_underscoredate = ? , " + " mac_underscoreno = ?, login_underscoreid= ? " + "WHERE ip_underscoreaddress = ?";
        DBOperation dbo = factory.createDBOperation(POOL_underscoreNAME);
        try {
            con = dbo.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(strQuery);
            ps.setString(1, this.sessionID);
            ps.setInt(2, this.user.getUserID());
            ps.setTimestamp(3, this.beginDate);
            ps.setString(4, this.macNO);
            ps.setString(5, this.loginID);
            ps.setString(6, this.ipAddress);
            int resultCount = ps.executeUpdate();
            if (resultCount != 1) {
                con.rollback();
                throw new CesSystemException("User_underscoresession.doUpdateByIP(): ERROR updating data in T_underscoreSYS_underscoreUSER_underscoreSESSION!! " + "resultCount = " + resultCount);
            }
            con.commit();
        } catch (SQLException se) {
            if (con != null) {
                con.rollback();
            }
            throw new CesSystemException("User_underscoresession.doUpdateByIP(): SQLException while updating user_underscoresession; " + "session_underscoreid = " + this.sessionID + " :\n\t" + se);
        } finally {
            con.setAutoCommit(true);
            closePreparedStatement(ps);
            closeConnection(dbo);
        }
    }

