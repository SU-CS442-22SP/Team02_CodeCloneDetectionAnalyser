    public static void refreshSession(int C_underscoreID) {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement updateLogin = con.prepareStatement("UPDATE customer SET c_underscorelogin = NOW(), c_underscoreexpiration = DATE_underscoreADD(NOW(), INTERVAL 2 HOUR) WHERE c_underscoreid = ?");
            updateLogin.setInt(1, C_underscoreID);
            updateLogin.executeUpdate();
            con.commit();
            updateLogin.close();
            returnConnection(con);
        } catch (java.lang.Exception ex) {
            try {
                con.rollback();
                ex.printStackTrace();
            } catch (Exception se) {
                System.err.println("Transaction rollback failed.");
            }
        }
    }

