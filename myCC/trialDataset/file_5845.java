    public void addBySiteChannelPath(String siteChannelPath, String[] docTypePaths, String[] showTemplateIds) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            connection.setAutoCommit(false);
            String sql = "delete from t_underscoreip_underscoredoctype_underscorechannel where chan_underscorepath='" + siteChannelPath + "'";
            connection.createStatement().executeUpdate(sql);
            sql = "insert into t_underscoreip_underscoredoctype_underscorechannel(doctype_underscorepath,chan_underscorepath,show_underscoretemplate_underscoreid) values(?,'" + siteChannelPath + "',?)";
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < docTypePaths.length; i++) {
                preparedStatement.setString(1, docTypePaths[i]);
                String temp = showTemplateIds != null && i < showTemplateIds.length ? showTemplateIds[i] : "null";
                if (temp == null || temp.trim().equals("") || temp.trim().equalsIgnoreCase("null")) {
                    preparedStatement.setInt(2, Types.NULL);
                } else {
                    preparedStatement.setInt(2, Integer.parseInt(temp));
                }
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
            close(resultSet, null, preparedStatement, connection, dbo);
        }
    }

