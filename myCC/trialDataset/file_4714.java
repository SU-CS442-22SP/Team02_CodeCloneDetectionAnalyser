    private static Long getNextPkValueForEntityIncreaseBy(String ename, int count, int increaseBy) {
        if (increaseBy < 1) increaseBy = 1;
        String where = "where eoentity_underscorename = '" + ename + "'";
        ERXJDBCConnectionBroker broker = ERXJDBCConnectionBroker.connectionBrokerForEntityNamed(ename);
        Connection con = broker.getConnection();
        try {
            try {
                con.setAutoCommit(false);
                con.setReadOnly(false);
            } catch (SQLException e) {
                log.error(e, e);
            }
            for (int tries = 0; tries < count; tries++) {
                try {
                    ResultSet resultSet = con.createStatement().executeQuery("select pk_underscorevalue from pk_underscoretable " + where);
                    con.commit();
                    boolean hasNext = resultSet.next();
                    long pk = 1;
                    if (hasNext) {
                        pk = resultSet.getLong("pk_underscorevalue");
                        con.createStatement().executeUpdate("update pk_underscoretable set pk_underscorevalue = " + (pk + increaseBy) + " " + where);
                    } else {
                        pk = maxIdFromTable(ename);
                        con.createStatement().executeUpdate("insert into pk_underscoretable (eoentity_underscorename, pk_underscorevalue) values ('" + ename + "', " + (pk + increaseBy) + ")");
                    }
                    con.commit();
                    return new Long(pk);
                } catch (SQLException ex) {
                    String s = ex.getMessage().toLowerCase();
                    boolean creationError = (s.indexOf("error code 116") != -1);
                    creationError |= (s.indexOf("pk_underscoretable") != -1 && s.indexOf("does not exist") != -1);
                    creationError |= s.indexOf("ora-00942") != -1;
                    if (creationError) {
                        try {
                            con.rollback();
                            log.info("creating pk table");
                            con.createStatement().executeUpdate("create table pk_underscoretable (eoentity_underscorename varchar(100) not null, pk_underscorevalue integer)");
                            con.createStatement().executeUpdate("alter table pk_underscoretable add primary key (eoentity_underscorename)");
                            con.commit();
                        } catch (SQLException ee) {
                            throw new NSForwardException(ee, "could not create pk table");
                        }
                    } else {
                        throw new NSForwardException(ex, "Error fetching PK");
                    }
                }
            }
        } finally {
            broker.freeConnection(con);
        }
        throw new IllegalStateException("Couldn't get PK");
    }

