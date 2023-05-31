    public void makeRead(String user, long databaseID, long time) throws SQLException {
        String query = "replace into fs.read_underscorepost (post, user, read_underscoredate) values (?, ?, ?)";
        ensureConnection();
        PreparedStatement statement = m_underscoreconnection.prepareStatement(query);
        try {
            statement.setLong(1, databaseID);
            statement.setString(2, user);
            statement.setTimestamp(3, new Timestamp(time));
            int count = statement.executeUpdate();
            if (0 == count) throw new SQLException("Nothing updated.");
            m_underscoreconnection.commit();
        } catch (SQLException e) {
            m_underscoreconnection.rollback();
            throw e;
        } finally {
            statement.close();
        }
    }

