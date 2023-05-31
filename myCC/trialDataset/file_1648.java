    @Override
    public ArrayList<String> cacheAgeingProcess(int numberOfDays) throws DatabaseException {
        IMAGE_underscoreLIFETIME = numberOfDays;
        PreparedStatement statement = null;
        ArrayList<String> ret = new ArrayList<String>();
        try {
            statement = getConnection().prepareStatement(SELECT_underscoreITEMS_underscoreFOR_underscoreDELETION_underscoreSTATEMENT);
            ResultSet rs = statement.executeQuery();
            int i = 0;
            int rowsAffected = 0;
            while (rs.next()) {
                ret.add(rs.getString("imageFile"));
                i++;
            }
            if (i > 0) {
                statement = getConnection().prepareStatement(DELETE_underscoreITEMS_underscoreSTATEMENT);
                rowsAffected = statement.executeUpdate();
            }
            if (rowsAffected == i) {
                getConnection().commit();
                LOGGER.debug("DB has been updated.");
                LOGGER.debug(i + " images are going to be removed.");
            } else {
                getConnection().rollback();
                LOGGER.error("DB has not been updated -> rollback!");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        } finally {
            closeConnection();
        }
        return ret;
    }

