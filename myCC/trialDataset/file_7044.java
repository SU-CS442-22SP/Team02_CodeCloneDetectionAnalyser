    public void candidatarAtividade(Atividade atividade) throws SQLException {
        Connection conn = null;
        String insert = "insert into Atividade_underscorehas_underscorerecurso_underscorehumano " + "(atividade_underscoreidatividade, usuario_underscoreidusuario, ativo) " + "values " + "(" + atividade.getIdAtividade() + ", " + "" + atividade.getRecursoHumano().getIdUsuario() + ", " + "'false')";
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

