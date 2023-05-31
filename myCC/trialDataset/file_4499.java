    @Override
    public void alterar(QuestaoMultiplaEscolha q) throws Exception {
        PreparedStatement stmt = null;
        String sql = "UPDATE questao SET id_underscoredisciplina=?, enunciado=?, grau_underscoredificuldade=? WHERE id_underscorequestao=?";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, q.getDisciplina().getIdDisciplina());
            stmt.setString(2, q.getEnunciado());
            stmt.setString(3, q.getDificuldade().name());
            stmt.setInt(4, q.getIdQuestao());
            stmt.executeUpdate();
            conexao.commit();
            alterarQuestaoMultiplaEscolha(q);
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        }
    }

