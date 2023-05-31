        @Override
        void execute(Connection conn, Component parent, String context, ProgressMonitor progressBar, ProgressWrapper progressWrapper) throws Exception {
            Statement statement = null;
            try {
                conn.setAutoCommit(false);
                statement = conn.createStatement();
                String deleteSql = getDeleteSql(m_underscorecompositionId);
                statement.executeUpdate(deleteSql);
                conn.commit();
                s_underscorecompostionCache.delete(new Integer(m_underscorecompositionId));
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                throw ex;
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        }

