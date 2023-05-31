    public void criarQuestaoDiscursiva(QuestaoDiscursiva q) throws SQLException {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO discursiva (id_underscorequestao,gabarito) VALUES (?,?)";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, q.getIdQuestao());
            stmt.setString(2, q.getGabarito());
            stmt.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        }
    }

