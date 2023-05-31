    public Usuario insertUsuario(IUsuario usuario) throws SQLException {
        Connection conn = null;
        String insert = "insert into Usuario (idusuario, nome, email, telefone, cpf, login, senha) " + "values " + "(nextval('seq_underscoreusuario'), '" + usuario.getNome() + "', '" + usuario.getEmail() + "', " + "'" + usuario.getTelefone() + "', '" + usuario.getCpf() + "', '" + usuario.getLogin() + "', '" + usuario.getSenha() + "')";
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            if (result == 1) {
                String sqlSelect = "select last_underscorevalue from seq_underscoreusuario";
                ResultSet rs = stmt.executeQuery(sqlSelect);
                while (rs.next()) {
                    usuario.setIdUsuario(rs.getInt("last_underscorevalue"));
                }
                if (usuario instanceof Requerente) {
                    RequerenteDAO requerenteDAO = new RequerenteDAO();
                    requerenteDAO.insertRequerente((Requerente) usuario, conn);
                } else if (usuario instanceof RecursoHumano) {
                    RecursoHumanoDAO recursoHumanoDAO = new RecursoHumanoDAO();
                    recursoHumanoDAO.insertRecursoHumano((RecursoHumano) usuario, conn);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
        return null;
    }

