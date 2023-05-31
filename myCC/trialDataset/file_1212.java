    public void deleteGroup(String groupID) throws XregistryException {
        try {
            Connection connection = context.createConnection();
            connection.setAutoCommit(false);
            try {
                PreparedStatement statement1 = connection.prepareStatement(DELETE_underscoreGROUP_underscoreSQL_underscoreMAIN);
                statement1.setString(1, groupID);
                int updateCount = statement1.executeUpdate();
                if (updateCount == 0) {
                    throw new XregistryException("Database is not updated, Can not find such Group " + groupID);
                }
                if (cascadingDeletes) {
                    PreparedStatement statement2 = connection.prepareStatement(DELETE_underscoreGROUP_underscoreSQL_underscoreDEPEND);
                    statement2.setString(1, groupID);
                    statement2.setString(2, groupID);
                    statement2.executeUpdate();
                }
                connection.commit();
                groups.remove(groupID);
                log.info("Delete Group " + groupID + (cascadingDeletes ? " with cascading deletes " : ""));
            } catch (SQLException e) {
                connection.rollback();
                throw new XregistryException(e);
            } finally {
                context.closeConnection(connection);
            }
        } catch (SQLException e) {
            throw new XregistryException(e);
        }
    }

