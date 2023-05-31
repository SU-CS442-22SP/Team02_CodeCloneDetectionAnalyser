    public int saveRoom(String name, String icon, String stringid) {
        DBConnection con = null;
        int categoryId = -1;
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            String query = "INSERT INTO cafe_underscoreChat_underscoreCategory " + "(cafe_underscoreChat_underscoreCategory_underscorepid,cafe_underscoreChat_underscoreCategory_underscorename, cafe_underscoreChat_underscoreCategory_underscoreicon) " + "VALUES (null,?,?) ";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, icon);
            ps.executeUpdate();
            query = "SELECT cafe_underscoreChat_underscoreCategory_underscoreid FROM cafe_underscoreChat_underscoreCategory " + "WHERE cafe_underscoreChat_underscoreCategory_underscorename=? ";
            ps = con.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                query = "INSERT INTO cafe_underscoreChatroom (cafe_underscorechatroom_underscorecategory, cafe_underscorechatroom_underscorename, cafe_underscorechatroom_underscorestringid) " + "VALUES (?,?,?) ";
                ps = con.prepareStatement(query);
                ps.setInt(1, rs.getInt("cafe_underscoreChat_underscoreCategory_underscoreid"));
                categoryId = rs.getInt("cafe_underscoreChat_underscoreCategory_underscoreid");
                ps.setString(2, name);
                ps.setString(3, stringid);
                ps.executeUpdate();
            }
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException sqle) {
            }
        } finally {
            if (con != null) con.release();
        }
        return categoryId;
    }

