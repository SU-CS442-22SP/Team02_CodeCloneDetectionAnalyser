    private void removeCollection(long oid, Connection conn) throws XMLDBException {
        try {
            String sql = "DELETE FROM X_underscoreDOCUMENT WHERE X_underscoreDOCUMENT.XDB_underscoreCOLLECTION_underscoreOID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, oid);
            pstmt.executeUpdate();
            pstmt.close();
            sql = "DELETE FROM XDB_underscoreCOLLECTION WHERE XDB_underscoreCOLLECTION.XDB_underscoreCOLLECTION_underscoreOID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, oid);
            pstmt.executeUpdate();
            pstmt.close();
            removeChildCollection(oid, conn);
        } catch (java.sql.SQLException se) {
            try {
                conn.rollback();
            } catch (java.sql.SQLException se2) {
                se2.printStackTrace();
            }
            se.printStackTrace();
        }
    }

