    public void modify(String strName, String strNewPass) {
        String str = "update jb_underscoreuser set V_underscorePASSWORD =? where V_underscoreUSERNAME =?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbForumFactory.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(str);
            pstmt.setString(1, SecurityUtil.md5ByHex(strNewPass));
            pstmt.setString(2, strName);
            pstmt.executeUpdate();
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
            }
        } finally {
            try {
                DbForumFactory.closeDB(null, pstmt, null, con);
            } catch (Exception e) {
            }
        }
    }

