    public void store(Component component, String componentName, int currentPilot) {
        try {
            PreparedStatement psta = jdbc.prepareStatement("UPDATE component_underscoreprop " + "SET size_underscoreheight = ?, size_underscorewidth = ?, pos_underscorex = ?, pos_underscorey = ? " + "WHERE pilot_underscoreid = ? " + "AND component_underscorename = ?");
            psta.setInt(1, component.getHeight());
            psta.setInt(2, component.getWidth());
            Point point = component.getLocation();
            psta.setInt(3, point.x);
            psta.setInt(4, point.y);
            psta.setInt(5, currentPilot);
            psta.setString(6, componentName);
            int update = psta.executeUpdate();
            if (update == 0) {
                psta = jdbc.prepareStatement("INSERT INTO component_underscoreprop " + "(size_underscoreheight, size_underscorewidth, pos_underscorex, pos_underscorey, pilot_underscoreid, component_underscorename) " + "VALUES (?,?,?,?,?,?)");
                psta.setInt(1, component.getHeight());
                psta.setInt(2, component.getWidth());
                psta.setInt(3, point.x);
                psta.setInt(4, point.y);
                psta.setInt(5, currentPilot);
                psta.setString(6, componentName);
                psta.executeUpdate();
            }
            jdbc.commit();
        } catch (SQLException e) {
            jdbc.rollback();
            log.debug(e);
        }
    }

