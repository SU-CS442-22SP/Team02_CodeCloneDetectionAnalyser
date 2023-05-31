    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int id = 0;
            String sql = "SELECT MAX(ID) as MAX_underscoreID from CORE_underscoreUSER_underscoreGROUPS";
            PreparedStatement pStmt = Database.getMyConnection().prepareStatement(sql);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("MAX_underscoreID") + 1;
            } else {
                id = 1;
            }
            Database.close(pStmt);
            sql = "INSERT INTO CORE_underscoreUSER_underscoreGROUPS" + " (ID, GRP_underscoreNAME, GRP_underscoreDESC, DATE_underscoreINITIAL, DATE_underscoreFINAL, IND_underscoreSTATUS)" + " VALUES (?, ?, ?, ?, ?, ?)";
            pStmt = Database.getMyConnection().prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.setString(2, txtGrpName.getText());
            pStmt.setString(3, txtGrpDesc.getText());
            pStmt.setDate(4, Utils.getTodaySql());
            pStmt.setDate(5, Date.valueOf("9999-12-31"));
            pStmt.setString(6, "A");
            pStmt.executeUpdate();
            Database.getMyConnection().commit();
            Database.close(pStmt);
            MessageBox.ok("New group added successfully", this);
            rs = getGroups();
            tblGroups.setModel(new GroupsTableModel(rs));
            Database.close(rs);
        } catch (SQLException e) {
            log.error("Failed with update operation \n" + e.getMessage());
            MessageBox.ok("Failed to create the new group in the database", this);
            try {
                Database.getMyConnection().rollback();
            } catch (Exception inner) {
            }
            ;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument for the DATE_underscoreFINAL \n" + e.getMessage());
            MessageBox.ok("Failed to create the new group in the database", this);
            try {
                Database.getMyConnection().rollback();
            } catch (Exception inner) {
            }
            ;
        } finally {
            txtGrpName.setEnabled(false);
            txtGrpDesc.setEnabled(false);
            btnOk.setEnabled(false);
            btnCancel.requestFocus();
        }
    }

