    public synchronized void checkout() throws SQLException, InterruptedException {
        Connection con = this.session.open();
        con.setAutoCommit(false);
        String sql_underscorestmt = DB2SQLStatements.shopping_underscorecart_underscoregetAll(this.customer_underscoreid);
        Statement stmt = con.createStatement(ResultSet.TYPE_underscoreSCROLL_underscoreINSENSITIVE, ResultSet.CONCUR_underscoreREAD_underscoreONLY);
        ResultSet res = stmt.executeQuery(sql_underscorestmt);
        res.last();
        int rowcount = res.getRow();
        res.beforeFirst();
        ShoppingCartItem[] resArray = new ShoppingCartItem[rowcount];
        int i = 0;
        while (res.next()) {
            resArray[i] = new ShoppingCartItem();
            resArray[i].setCustomer_underscoreid(res.getInt("customer_underscoreid"));
            resArray[i].setDate_underscorestart(res.getDate("date_underscorestart"));
            resArray[i].setDate_underscorestop(res.getDate("date_underscorestop"));
            resArray[i].setRoom_underscoretype_underscoreid(res.getInt("room_underscoretype_underscoreid"));
            resArray[i].setNumtaken(res.getInt("numtaken"));
            resArray[i].setTotal_underscoreprice(res.getInt("total_underscoreprice"));
            i++;
        }
        this.wait(4000);
        try {
            for (int j = 0; j < rowcount; j++) {
                sql_underscorestmt = DB2SQLStatements.room_underscoredate_underscoreupdate(resArray[j]);
                stmt = con.createStatement();
                stmt.executeUpdate(sql_underscorestmt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
        }
        for (int j = 0; j < rowcount; j++) {
            System.out.println(j);
            sql_underscorestmt = DB2SQLStatements.booked_underscoreinsert(resArray[j], 2);
            stmt = con.createStatement();
            stmt.executeUpdate(sql_underscorestmt);
        }
        sql_underscorestmt = DB2SQLStatements.shopping_underscorecart_underscoredeleteAll(this.customer_underscoreid);
        stmt = con.createStatement();
        stmt.executeUpdate(sql_underscorestmt);
        con.commit();
        this.session.close(con);
    }

