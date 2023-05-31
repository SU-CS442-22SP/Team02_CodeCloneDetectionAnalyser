    public void savaRolePerm(String roleid, Collection role_underscoreperm_underscorecollect) throws DAOException, SQLException {
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PrivilegeFactory factory = PrivilegeFactory.getInstance();
        Operation op = factory.createOperation();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_underscoreSOURCE);
            conn = cp.getConnection();
            try {
                pstmt = conn.prepareStatement(DEL_underscoreROLE_underscorePERM);
                pstmt.setString(1, roleid);
                pstmt.executeUpdate();
            } catch (Exception e) {
            }
            if ((role_underscoreperm_underscorecollect == null) || (role_underscoreperm_underscorecollect.size() == 0)) {
                return;
            } else {
                conn.setAutoCommit(false);
                pstmt = conn.prepareStatement(ADD_underscoreROLE_underscorePERM);
                Iterator role_underscoreperm_underscoreir = role_underscoreperm_underscorecollect.iterator();
                while (role_underscoreperm_underscoreir.hasNext()) {
                    RolePermission rolePerm = (RolePermission) role_underscoreperm_underscoreir.next();
                    pstmt.setString(1, String.valueOf(rolePerm.getRoleid()));
                    pstmt.setString(2, String.valueOf(rolePerm.getResourceid()));
                    pstmt.setString(3, String.valueOf(rolePerm.getResopid()));
                    pstmt.executeUpdate();
                }
                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
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

