    public void removeRoom(int thisRoom) {
        DBConnection con = null;
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            String query = "DELETE FROM cafe_underscoreChat_underscoreCategory WHERE cafe_underscoreChat_underscoreCategory_underscoreid=? ";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, thisRoom);
            ps.executeUpdate();
            query = "DELETE FROM cafe_underscoreChatroom WHERE cafe_underscorechatroom_underscorecategory=? ";
            ps = con.prepareStatement(query);
            ps.setInt(1, thisRoom);
            ps.executeUpdate();
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
    }

