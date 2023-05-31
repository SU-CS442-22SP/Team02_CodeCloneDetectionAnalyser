    public void savaUserPerm(String userid, Collection user_underscoreperm_underscorecollect) throws DAOException, SQLException {
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PrivilegeFactory factory = PrivilegeFactory.getInstance();
        Operation op = factory.createOperation();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_underscoreSOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(DEL_underscoreUSER_underscorePERM);
            pstmt.setString(1, userid);
            pstmt.executeUpdate();
            if ((user_underscoreperm_underscorecollect == null) || (user_underscoreperm_underscorecollect.size() <= 0)) {
                return;
            } else {
                conn.setAutoCommit(false);
                pstmt = conn.prepareStatement(ADD_underscoreUSER_underscorePERM);
                Iterator user_underscoreperm_underscoreir = user_underscoreperm_underscorecollect.iterator();
                while (user_underscoreperm_underscoreir.hasNext()) {
                    UserPermission userPerm = (UserPermission) user_underscoreperm_underscoreir.next();
                    pstmt.setString(1, String.valueOf(userPerm.getUser_underscoreid()));
                    pstmt.setString(2, String.valueOf(userPerm.getResource_underscoreid()));
                    pstmt.setString(3, String.valueOf(userPerm.getResop_underscoreid()));
                    pstmt.executeUpdate();
                }
                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw new DAOException();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

