    public void add(Site site) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sqlStr = "insert into t_underscoreip_underscoresite (id,name,description,ascii_underscorename,site_underscorepath,remark_underscorenumber,increment_underscoreindex,use_underscorestatus,appserver_underscoreid) VALUES(?,?,?,?,?,?,?,?,?)";
            dbo = createDBOperation();
            connection = dbo.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sqlStr);
            preparedStatement.setInt(1, site.getSiteID());
            preparedStatement.setString(2, site.getName());
            preparedStatement.setString(3, site.getDescription());
            preparedStatement.setString(4, site.getAsciiName());
            preparedStatement.setString(5, site.getPath());
            preparedStatement.setInt(6, site.getRemarkNumber());
            preparedStatement.setString(7, site.getIncrementIndex().trim());
            preparedStatement.setString(8, String.valueOf(site.getUseStatus()));
            preparedStatement.setString(9, String.valueOf(site.getAppserverID()));
            preparedStatement.executeUpdate();
            String[] path = new String[1];
            path[0] = site.getPath();
            selfDefineAdd(path, site, connection, preparedStatement);
            connection.commit();
            int resID = site.getSiteID() + Const.SITE_underscoreTYPE_underscoreRES;
            String resName = site.getName();
            int resTypeID = Const.RES_underscoreTYPE_underscoreID;
            int operateTypeID = Const.OPERATE_underscoreTYPE_underscoreID;
            String remark = "";
            AuthorityManager am = new AuthorityManager();
            am.createExtResource(Integer.toString(resID), resName, resTypeID, operateTypeID, remark);
            site.wirteFile();
        } catch (SQLException ex) {
            connection.rollback();
            log.error("����վ��ʧ��!", ex);
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
    }

