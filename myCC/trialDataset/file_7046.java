    public void aprovarCandidato(Atividade atividade) throws SQLException {
        Connection conn = null;
        String insert = "update Atividade_underscorehas_underscorerecurso_underscorehumano set ativo='true' " + "where atividade_underscoreidatividade=" + atividade.getIdAtividade() + " and " + " usuario_underscoreidusuario=" + atividade.getRecursoHumano().getIdUsuario();
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

