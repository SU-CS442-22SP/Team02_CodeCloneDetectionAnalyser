    private void writeStatsToDatabase(long transferJobAIPCount, long reprocessingJobAIPCount, long transferJobAIPVolume, long reprocessingJobAIPVolume, long overallBinaryAIPCount, Map<String, AIPStatistics> mimeTypeRegister) throws SQLException {
        int nextAIPStatsID;
        long nextMimetypeStatsID;
        Statement select = dbConnection.createStatement();
        String aipStatsQuery = "select max(aip_underscorestatistics_underscoreid) from aip_underscorestatistics";
        ResultSet result = select.executeQuery(aipStatsQuery);
        if (result.next()) {
            nextAIPStatsID = result.getInt(1) + 1;
        } else {
            throw new SQLException("Problem getting maximum AIP Statistics ID");
        }
        String mimetypeStatsQuery = "select max(mimetype_underscoreaip_underscorestatistics_underscoreid) from mimetype_underscoreaip_underscorestatistics";
        result = select.executeQuery(mimetypeStatsQuery);
        if (result.next()) {
            nextMimetypeStatsID = result.getLong(1) + 1;
        } else {
            throw new SQLException("Problem getting maximum MIME type AIP Statistics ID");
        }
        String insertAIPStatsEntryQuery = "insert into aip_underscorestatistics " + "(aip_underscorestatistics_underscoreid, tj_underscoreaip_underscorecount, tj_underscoreaip_underscorevolume, rj_underscoreaip_underscorecount, rj_underscoreaip_underscorevolume, " + "collation_underscoredate, binary_underscoreaip_underscorecount) " + "values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insert = dbConnection.prepareStatement(insertAIPStatsEntryQuery);
        insert.setInt(1, nextAIPStatsID);
        insert.setLong(2, transferJobAIPCount);
        insert.setLong(3, transferJobAIPVolume);
        insert.setLong(4, reprocessingJobAIPCount);
        insert.setLong(5, reprocessingJobAIPVolume);
        insert.setDate(6, new java.sql.Date(System.currentTimeMillis()));
        insert.setLong(7, overallBinaryAIPCount);
        int rowsAdded = insert.executeUpdate();
        if (rowsAdded != 1) {
            dbConnection.rollback();
            throw new SQLException("Could not insert row into AIP statistics table");
        }
        String insertMimeTypeStatsQuery = "insert into mimetype_underscoreaip_underscorestatistics " + "(mimetype_underscoreaip_underscorestatistics_underscoreid, aip_underscorestatistics_underscoreid, mimetype_underscoreaip_underscorecount, mimetype_underscoreaip_underscorevolume, mimetype) " + "values (?, ?, ?, ?, ?)";
        insert = dbConnection.prepareStatement(insertMimeTypeStatsQuery);
        insert.setInt(2, nextAIPStatsID);
        for (String mimeType : mimeTypeRegister.keySet()) {
            AIPStatistics mimeTypeStats = mimeTypeRegister.get(mimeType);
            insert.setLong(1, nextMimetypeStatsID);
            insert.setLong(3, mimeTypeStats.aipCount);
            insert.setLong(4, mimeTypeStats.aipVolume);
            insert.setString(5, mimeType);
            nextMimetypeStatsID++;
            rowsAdded = insert.executeUpdate();
            if (rowsAdded != 1) {
                dbConnection.rollback();
                throw new SQLException("Could not insert row into MIME Type AIP statistics table");
            }
        }
        dbConnection.commit();
    }

