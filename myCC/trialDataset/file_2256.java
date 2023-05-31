    public void delete(Site site) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String chkSql = "select id from t_underscoreip_underscoredoc where channel_underscorepath=?";
            dbo = createDBOperation();
            connection = dbo.getConnection();
            connection.setAutoCommit(false);
            String[] selfDefinePath = getSelfDefinePath(site.getPath(), "1", connection, preparedStatement, resultSet);
            selfDefineDelete(selfDefinePath, connection, preparedStatement);
            preparedStatement = connection.prepareStatement(chkSql);
            preparedStatement.setString(1, site.getPath());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("ɾ��ʧ�ܣ�" + site.getName() + "���Ѿ����ĵ����ڣ�");
            } else {
                String sqlStr = "delete from t_underscoreip_underscoresite where site_underscorepath=?";
                dbo = createDBOperation();
                connection = dbo.getConnection();
                preparedStatement = connection.prepareStatement(sqlStr);
                preparedStatement.setString(1, site.getPath());
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
    }

