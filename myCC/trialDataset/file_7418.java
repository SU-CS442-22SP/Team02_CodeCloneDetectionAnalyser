    public void addStadium(Stadium stadium) throws StadiumException {
        Connection conn = ConnectionManager.getManager().getConnection();
        if (findStadiumBy_underscoreN_underscoreC(stadium.getName(), stadium.getCity()) != -1) throw new StadiumException("Stadium already exists");
        try {
            PreparedStatement stm = conn.prepareStatement(Statements.INSERT_underscoreSTADIUM);
            conn.setAutoCommit(false);
            stm.setString(1, stadium.getName());
            stm.setString(2, stadium.getCity());
            stm.executeUpdate();
            int id = getMaxId();
            TribuneLogic logic = TribuneLogic.getInstance();
            for (Tribune trib : stadium.getTribunes()) {
                int tribuneId = logic.addTribune(trib);
                if (tribuneId != -1) {
                    stm = conn.prepareStatement(Statements.INSERT_underscoreSTAD_underscoreTRIBUNE);
                    stm.setInt(1, id);
                    stm.setInt(2, tribuneId);
                    stm.executeUpdate();
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new StadiumException("Adding stadium failed", e);
        }
        try {
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

