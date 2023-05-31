    public static int createEmptyCart() {
        int SHOPPING_underscoreID = 0;
        Connection con = null;
        try {
            con = getConnection();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        try {
            PreparedStatement insert_underscorecart = null;
            SHOPPING_underscoreID = Integer.parseInt(Sequence.getSequenceNumber("shopping_underscorecart"));
            insert_underscorecart = con.prepareStatement("INSERT INTO shopping_underscorecart (sc_underscoreid, sc_underscoretime) VALUES ( ? , NOW() )");
            insert_underscorecart.setInt(1, SHOPPING_underscoreID);
            insert_underscorecart.executeUpdate();
            con.commit();
            insert_underscorecart.close();
            returnConnection(con);
        } catch (java.lang.Exception ex) {
            try {
                con.rollback();
                ex.printStackTrace();
            } catch (Exception se) {
                System.err.println("Transaction rollback failed.");
            }
        }
        return SHOPPING_underscoreID;
    }

