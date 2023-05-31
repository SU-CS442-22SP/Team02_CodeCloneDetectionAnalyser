    public void add(Channel channel) throws Exception {
        String sqlStr = null;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            sqlStr = "insert into t_underscoreip_underscorechannel (id,name,description,ascii_underscorename,channel_underscorepath,site_underscoreid,type,data_underscoreurl,template_underscoreid,use_underscorestatus,order_underscoreno,style,creator,create_underscoredate,refresh_underscoreflag,page_underscorenum) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            dbo = createDBOperation();
            connection = dbo.getConnection();
            connection.setAutoCommit(false);
            String[] path = new String[1];
            path[0] = channel.getPath();
            selfDefineAdd(path, channel, connection, preparedStatement);
            preparedStatement = connection.prepareStatement(sqlStr);
            preparedStatement.setInt(1, channel.getChannelID());
            preparedStatement.setString(2, channel.getName());
            preparedStatement.setString(3, channel.getDescription());
            preparedStatement.setString(4, channel.getAsciiName());
            preparedStatement.setString(5, channel.getPath());
            preparedStatement.setInt(6, channel.getSiteId());
            preparedStatement.setString(7, channel.getChannelType());
            preparedStatement.setString(8, channel.getDataUrl());
            if (channel.getTemplateId() == null || channel.getTemplateId().trim().equals("")) preparedStatement.setNull(9, Types.INTEGER); else preparedStatement.setInt(9, Integer.parseInt(channel.getTemplateId()));
            preparedStatement.setString(10, channel.getUseStatus());
            preparedStatement.setInt(11, channel.getOrderNo());
            preparedStatement.setString(12, channel.getStyle());
            preparedStatement.setInt(13, channel.getCreator());
            preparedStatement.setTimestamp(14, (Timestamp) channel.getCreateDate());
            preparedStatement.setString(15, channel.getRefPath());
            preparedStatement.setInt(16, channel.getPageNum());
            preparedStatement.executeUpdate();
            connection.commit();
            int operateTypeID = Const.OPERATE_underscoreTYPE_underscoreID;
            int resID = channel.getChannelID() + Const.CHANNEL_underscoreTYPE_underscoreRES;
            String resName = channel.getName();
            int resTypeID = Const.RES_underscoreTYPE_underscoreID;
            String remark = "";
            AuthorityManager am = new AuthorityManager();
            am.createExtResource(Integer.toString(resID), resName, resTypeID, operateTypeID, remark);
        } catch (SQLException ex) {
            connection.rollback();
            log.error("���Ƶ��ʱSql�쳣��ִ����䣺" + sqlStr);
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
    }

