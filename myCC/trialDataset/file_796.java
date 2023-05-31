    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {
        if (validateData()) {
            LoginUser me = AdministrationPanelView.getMe();
            Connection dbConnection = null;
            try {
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                dbConnection = DriverManager.getConnection(me.getSqlReportsURL(), me.getSqlReportsUser(), me.getSqlReportsPassword());
                dbConnection.setAutoCommit(false);
                dbConnection.setSavepoint();
                String sql = "INSERT INTO campaigns (type, name, dateCreated, createdBy) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = dbConnection.prepareStatement(sql);
                statement.setByte(1, (optTypeAgents.isSelected()) ? CampaignStatics.CAMP_underscoreTYPE_underscoreAGENT : CampaignStatics.CAMP_underscoreTYPE_underscoreIVR);
                statement.setString(2, txtCampaignName.getText());
                statement.setTimestamp(3, new Timestamp(Calendar.getInstance().getTime().getTime()));
                statement.setLong(4, me.getId());
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                long campaignId = rs.getLong(1);
                sql = "INSERT INTO usercampaigns (userid, campaignid, role) VALUES (?, ?, ?)";
                statement = dbConnection.prepareStatement(sql);
                statement.setLong(1, me.getId());
                statement.setLong(2, campaignId);
                statement.setString(3, "admin");
                statement.executeUpdate();
                dbConnection.commit();
                dbConnection.close();
                CampaignAdmin ca = new CampaignAdmin();
                ca.setCampaign(txtCampaignName.getText());
                ca.setVisible(true);
                dispose();
            } catch (SQLException ex) {
                try {
                    dbConnection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(Logger.GLOBAL_underscoreLOGGER_underscoreNAME).log(Level.SEVERE, null, ex1);
                }
                JOptionPane.showMessageDialog(this.getRootPane(), ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_underscoreMESSAGE);
                Logger.getLogger(Logger.GLOBAL_underscoreLOGGER_underscoreNAME).log(Level.SEVERE, null, ex);
            }
        }
    }

