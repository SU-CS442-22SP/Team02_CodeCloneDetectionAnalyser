    public SysSequences getSeqs(String tableName) throws SQLException {
        SysSequences seq = new SysSequences();
        if (tableName == null || tableName.trim().equals("")) return null;
        Connection conn = null;
        try {
            conn = ConnectUtil.getConnect();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("update ss_underscoresys_underscoresequences set next_underscorevalue=next_underscorevalue+step_underscorevalue where table_underscorename='" + tableName + "'");
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement("select * from ss_underscoresys_underscoresequences where table_underscorename='" + tableName + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long nextValue = rs.getLong(2);
                long stepValue = rs.getLong(3);
                seq.setTableName(tableName);
                seq.setNextValue(nextValue - stepValue + 1);
                seq.setStepValue(stepValue);
            }
            rs.close();
            ps.close();
            if (seq.getTableName() == null) {
                ps = conn.prepareStatement("insert into ss_underscoresys_underscoresequences values('" + tableName + "'," + (Constants.DEFAULT_underscoreCURR_underscoreVALUE + Constants.DEFAULT_underscoreSTEP_underscoreVALUE) + "," + Constants.DEFAULT_underscoreSTEP_underscoreVALUE + ")");
                ps.executeUpdate();
                ps.close();
                seq.setTableName(tableName);
                seq.setNextValue(Constants.DEFAULT_underscoreCURR_underscoreVALUE + 1);
                seq.setStepValue(Constants.DEFAULT_underscoreSTEP_underscoreVALUE);
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            ConnectUtil.closeConn(conn);
        }
        return seq;
    }

