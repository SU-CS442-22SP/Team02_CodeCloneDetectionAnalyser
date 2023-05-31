    @Override
    protected void removeOrphansElements() throws DatabaseException {
        this.getIdChache().clear();
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement;
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallElementsSchemaAndTableName() + " FROM " + this.getCallElementsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallElementsSchemaAndTableName() + ".element_underscoreid =  " + this.getCallInvocationsSchemaAndTableName() + ".element_underscoreid WHERE " + this.getCallInvocationsSchemaAndTableName() + ".element_underscoreid IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallExceptionsSchemaAndTableName() + " FROM " + this.getCallExceptionsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallExceptionsSchemaAndTableName() + ".exception_underscoreid =  " + this.getCallInvocationsSchemaAndTableName() + ".exception_underscoreid WHERE " + this.getCallInvocationsSchemaAndTableName() + ".exception_underscoreid IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallPrincipalsSchemaAndTableName() + " FROM " + this.getCallPrincipalsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallPrincipalsSchemaAndTableName() + ".principal_underscoreid =  " + this.getCallInvocationsSchemaAndTableName() + ".principal_underscoreid WHERE " + this.getCallInvocationsSchemaAndTableName() + ".principal_underscoreid IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getHttpSessionElementsSchemaAndTableName() + " FROM " + this.getHttpSessionElementsSchemaAndTableName() + " LEFT JOIN " + this.getHttpSessionInvocationsSchemaAndTableName() + " ON " + this.getHttpSessionElementsSchemaAndTableName() + ".element_underscoreid =  " + this.getHttpSessionInvocationsSchemaAndTableName() + ".element_underscoreid WHERE " + this.getHttpSessionInvocationsSchemaAndTableName() + ".element_underscoreid IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getJvmElementsSchemaAndTableName() + " FROM " + this.getJvmElementsSchemaAndTableName() + " LEFT JOIN " + this.getJvmInvocationsSchemaAndTableName() + " ON " + this.getJvmElementsSchemaAndTableName() + ".element_underscoreid =  " + this.getJvmInvocationsSchemaAndTableName() + ".element_underscoreid WHERE " + this.getJvmInvocationsSchemaAndTableName() + ".element_underscoreid IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getPersistenceEntityElementsSchemaAndTableName() + " FROM " + this.getPersistenceEntityElementsSchemaAndTableName() + " LEFT JOIN " + this.getPersistenceEntityStatisticsSchemaAndTableName() + " ON " + this.getPersistenceEntityElementsSchemaAndTableName() + ".element_underscoreid =  " + this.getPersistenceEntityStatisticsSchemaAndTableName() + ".element_underscoreid WHERE " + this.getPersistenceEntityStatisticsSchemaAndTableName() + ".element_underscoreid IS NULL ");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getPersistenceQueryElementsSchemaAndTableName() + " FROM " + this.getPersistenceQueryElementsSchemaAndTableName() + " LEFT JOIN " + this.getPersistenceQueryStatisticsSchemaAndTableName() + " ON " + this.getPersistenceQueryElementsSchemaAndTableName() + ".element_underscoreid =  " + this.getPersistenceQueryStatisticsSchemaAndTableName() + ".element_underscoreid WHERE " + this.getPersistenceQueryStatisticsSchemaAndTableName() + ".element_underscoreid IS NULL ");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getHardDiskElementsSchemaAndTableName() + " FROM " + this.getHardDiskElementsSchemaAndTableName() + " LEFT JOIN " + this.getHardDiskInvocationsSchemaAndTableName() + " ON " + this.getHardDiskElementsSchemaAndTableName() + ".element_underscoreid =  " + this.getHardDiskInvocationsSchemaAndTableName() + ".element_underscoreid WHERE " + this.getHardDiskInvocationsSchemaAndTableName() + ".element_underscoreid IS NULL ");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error cleaning database.", e);
        } finally {
            this.releaseConnection(connection);
        }
        return;
    }

