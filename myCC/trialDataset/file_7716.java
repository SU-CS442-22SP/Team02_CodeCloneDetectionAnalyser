    public static void adminUpdate(int i_underscoreid, double cost, String image, String thumbnail) {
        Connection con = null;
        try {
            tmpAdmin++;
            String name = "$tmp_underscoreadmin" + tmpAdmin;
            con = getConnection();
            PreparedStatement related1 = con.prepareStatement("CREATE TEMPORARY TABLE " + name + " TYPE=HEAP SELECT o_underscoreid FROM orders ORDER BY o_underscoredate DESC LIMIT 10000");
            related1.executeUpdate();
            related1.close();
            PreparedStatement related2 = con.prepareStatement("SELECT ol2.ol_underscorei_underscoreid, SUM(ol2.ol_underscoreqty) AS sum_underscoreol FROM order_underscoreline ol, order_underscoreline ol2, " + name + " t " + "WHERE ol.ol_underscoreo_underscoreid = t.o_underscoreid AND ol.ol_underscorei_underscoreid = ? AND ol2.ol_underscoreo_underscoreid = t.o_underscoreid AND ol2.ol_underscorei_underscoreid <> ? " + "GROUP BY ol2.ol_underscorei_underscoreid ORDER BY sum_underscoreol DESC LIMIT 0,5");
            related2.setInt(1, i_underscoreid);
            related2.setInt(2, i_underscoreid);
            ResultSet rs = related2.executeQuery();
            int[] related_underscoreitems = new int[5];
            int counter = 0;
            int last = 0;
            while (rs.next()) {
                last = rs.getInt(1);
                related_underscoreitems[counter] = last;
                counter++;
            }
            for (int i = counter; i < 5; i++) {
                last++;
                related_underscoreitems[i] = last;
            }
            rs.close();
            related2.close();
            PreparedStatement related3 = con.prepareStatement("DROP TABLE " + name);
            related3.executeUpdate();
            related3.close();
            PreparedStatement statement = con.prepareStatement("UPDATE item SET i_underscorecost = ?, i_underscoreimage = ?, i_underscorethumbnail = ?, i_underscorepub_underscoredate = CURRENT_underscoreDATE(), " + " i_underscorerelated1 = ?, i_underscorerelated2 = ?, i_underscorerelated3 = ?, i_underscorerelated4 = ?, i_underscorerelated5 = ? WHERE i_underscoreid = ?");
            statement.setDouble(1, cost);
            statement.setString(2, image);
            statement.setString(3, thumbnail);
            statement.setInt(4, related_underscoreitems[0]);
            statement.setInt(5, related_underscoreitems[1]);
            statement.setInt(6, related_underscoreitems[2]);
            statement.setInt(7, related_underscoreitems[3]);
            statement.setInt(8, related_underscoreitems[4]);
            statement.setInt(9, i_underscoreid);
            statement.executeUpdate();
            con.commit();
            statement.close();
            returnConnection(con);
        } catch (java.lang.Exception ex) {
            try {
                con.rollback();
                ex.printStackTrace();
            } catch (Exception se) {
                System.err.println("Transaction rollback failed.");
            }
        }
    }

