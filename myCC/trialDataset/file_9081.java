    public void run() throws Exception {
        logger.debug("#run enter");
        logger.debug("#run lineId : " + lineId);
        logger.debug("#run quantityNew : " + quantityNew);
        logger.debug("#run priceNew : " + priceNew);
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            connection.setAutoCommit(false);
            Integer itemId = null;
            Integer quantity = null;
            ps = connection.prepareStatement(SQL_underscoreSELECT_underscoreORDER_underscoreLINE);
            ps.setInt(1, lineId);
            rs = ps.executeQuery();
            while (rs.next()) {
                itemId = rs.getInt("ITEM_underscoreID");
                quantity = rs.getInt("QUANTITY");
            }
            rs.close();
            ps.close();
            ps = connection.prepareStatement(SQL_underscoreUPDATE_underscoreITEM_underscoreBALANCE);
            ps.setInt(1, quantityNew - quantity);
            ps.setInt(2, itemId);
            ps.executeUpdate();
            ps = connection.prepareStatement(SQL_underscoreUPDATE_underscoreORDER_underscoreLINE);
            ps.setDouble(1, priceNew);
            ps.setInt(2, quantityNew);
            ps.setInt(3, lineId);
            ps.executeUpdate();
            ps.close();
            ps.close();
            connection.commit();
        } catch (SQLException ex) {
            logger.error("SQLException", ex);
            connection.rollback();
            throw new Exception("Не удалось обновить позицию в заказе. Ошибка : " + ex.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
        logger.debug("#run exit");
    }

