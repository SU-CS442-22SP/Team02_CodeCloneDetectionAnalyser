    private static void addIngredient(int recipeId, String name, String amount, int measureId, int shopFlag) throws Exception {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("INSERT INTO ingredients (recipe_underscoreid, name, amount, measure_underscoreid, shop_underscoreflag) VALUES (?,?,?,?,?)");
            pst.setInt(1, recipeId);
            pst.setString(2, name);
            pst.setString(3, amount);
            pst.setInt(4, measureId);
            pst.setInt(5, shopFlag);
            pst.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Ainesosan lis�ys ep�onnistui. Poikkeus: " + e.getMessage());
        }
    }

