    public void reset(int currentPilot) {
        try {
            PreparedStatement psta = jdbc.prepareStatement("DELETE FROM component_underscoreprop " + "WHERE pilot_underscoreid = ? ");
            psta.setInt(1, currentPilot);
            psta.executeUpdate();
            jdbc.commit();
        } catch (SQLException e) {
            jdbc.rollback();
            log.debug(e);
        }
    }

