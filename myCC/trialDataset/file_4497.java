    public void criarTopicoQuestao(Questao q, Integer idTopico) throws SQLException {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO questao_underscoretopico (id_underscorequestao, id_underscoredisciplina, id_underscoretopico) VALUES (?,?,?)";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, q.getIdQuestao());
            stmt.setInt(2, q.getDisciplina().getIdDisciplina());
            stmt.setInt(3, idTopico);
            stmt.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        }
    }

