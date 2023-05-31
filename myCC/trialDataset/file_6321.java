    @Test
    public void pk() throws Exception {
        Connection conn = s.getConnection();
        conn.setAutoCommit(false);
        PreparedStatement ps = conn.prepareStatement("insert into t_underscoretest(t_underscorename,t_underscorecname,t_underscoredata,t_underscoredate,t_underscoredouble) values(?,?,?,?,?)");
        for (int i = 10; i < 20; ++i) {
            ps.setString(1, "name-" + i);
            ps.setString(2, "cname-" + i);
            ps.setBlob(3, null);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setNull(5, java.sql.Types.DOUBLE);
            ps.executeUpdate();
        }
        conn.rollback();
        conn.setAutoCommit(true);
        ps.close();
        conn.close();
    }

