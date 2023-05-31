    private void load() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = FidoDataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            clearTables(stmt);
            stmt.executeQuery("select setval('objects_underscoreobjectid_underscoreseq', 1000)");
            stmt.executeQuery("select setval('instructions_underscoreinstructionid_underscoreseq', 1)");
            stmt.executeQuery("select setval('transactions_underscoretransactionid_underscoreseq', 1)");
            stmt.executeQuery("select setval('verbtransactions_underscoreverbid_underscoreseq', 1)");
            stmt.executeUpdate("update SystemProperties set value = 'Minimal Data' where name = 'DB Data Version'");
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

