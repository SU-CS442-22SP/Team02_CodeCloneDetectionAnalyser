    public void cleanup(long timeout) throws PersisterException {
        long threshold = System.currentTimeMillis() - timeout;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = _underscoreds.getConnection();
            conn.setAutoCommit(true);
            ps = conn.prepareStatement("delete from " + _underscoretable_underscorename + " where " + _underscorets_underscorecol + " < ?");
            ps.setLong(1, threshold);
            ps.executeUpdate();
        } catch (Throwable th) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Throwable th2) {
                }
            }
            throw new PersisterException("Failed to cleanup timed out objects: ", th);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Throwable th) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Throwable th) {
                }
            }
        }
    }

