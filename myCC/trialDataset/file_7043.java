    public Atividade insertAtividade(Atividade atividade) throws SQLException {
        Connection conn = null;
        String insert = "insert into Atividade (idatividade, requerente_underscoreidrequerente, datacriacao, datatermino, valor, tipoatividade, descricao, fase_underscoreidfase, estado) " + "values " + "(nextval('seq_underscoreatividade'), " + atividade.getRequerente().getIdRequerente() + ", " + "'" + atividade.getDataCriacao() + "', '" + atividade.getDataTermino() + "', '" + atividade.getValor() + "', '" + atividade.getTipoAtividade().getIdTipoAtividade() + "', '" + atividade.getDescricao() + "', " + atividade.getFaseIdFase() + ", " + atividade.getEstado() + ")";
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            if (result == 1) {
                String sqlSelect = "select last_underscorevalue from seq_underscoreatividade";
                ResultSet rs = stmt.executeQuery(sqlSelect);
                while (rs.next()) {
                    atividade.setIdAtividade(rs.getInt("last_underscorevalue"));
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

