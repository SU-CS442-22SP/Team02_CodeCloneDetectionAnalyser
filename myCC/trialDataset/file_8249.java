    @Override
    protected String doIt() throws Exception {
        PreparedStatement insertStmt = null;
        try {
            insertStmt = DB.prepareStatement("INSERT INTO AD_underscoreSequence_underscoreNo(AD_underscoreSEQUENCE_underscoreID, CALENDARYEAR, " + "AD_underscoreCLIENT_underscoreID, AD_underscoreORG_underscoreID, ISACTIVE, CREATED, CREATEDBY, " + "UPDATED, UPDATEDBY, CURRENTNEXT) " + "(SELECT AD_underscoreSequence_underscoreID, '" + year + "', " + "AD_underscoreClient_underscoreID, AD_underscoreOrg_underscoreID, IsActive, Created, CreatedBy, " + "Updated, UpdatedBy, StartNo " + "FROM AD_underscoreSequence a " + "WHERE StartNewYear = 'Y' AND NOT EXISTS ( " + "SELECT AD_underscoreSequence_underscoreID " + "FROM AD_underscoreSequence_underscoreNo b " + "WHERE a.AD_underscoreSequence_underscoreID = b.AD_underscoreSequence_underscoreID " + "AND CalendarYear = ?)) ", get_underscoreTrxName());
            insertStmt.setString(1, year);
            insertStmt.executeUpdate();
            commit();
        } catch (Exception ex) {
            rollback();
            throw ex;
        } finally {
            DB.close(insertStmt);
        }
        return "Sequence No updated successfully";
    }

