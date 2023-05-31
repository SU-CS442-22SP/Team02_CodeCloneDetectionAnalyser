    public void remove(String oid) throws PersisterException {
        String key = getLock(oid);
        if (key == null) {
            throw new PersisterException("Object does not exist: OID = " + oid);
        } else if (!NULL.equals(key)) {
            throw new PersisterException("The object is currently locked: OID = " + oid + ", LOCK = " + key);
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = _underscoreds.getConnection();
            conn.setAutoCommit(true);
            ps = conn.prepareStatement("delete from " + _underscoretable_underscorename + " where " + _underscoreoid_underscorecol + " = ?");
            ps.setString(1, oid);
            ps.executeUpdate();
        } catch (Throwable th) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Throwable th2) {
                }
            }
            throw new PersisterException("Failed to delete object: OID = " + oid, th);
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

