        void execute(Connection conn, Component parent, String context, final ProgressMonitor progressMonitor, ProgressWrapper progressWrapper) throws Exception {
            int noOfComponents = m_underscorecomponents.length;
            Statement statement = null;
            StringBuffer pmNoteBuf = new StringBuffer(m_underscoreupdate ? "Updating " : "Creating ");
            pmNoteBuf.append(m_underscoreitemNameAbbrev);
            pmNoteBuf.append(" ");
            pmNoteBuf.append(m_underscoreitemNameValue);
            final String pmNote = pmNoteBuf.toString();
            progressMonitor.setNote(pmNote);
            try {
                conn.setAutoCommit(false);
                int id = -1;
                if (m_underscoreupdate) {
                    statement = conn.createStatement();
                    String sql = getUpdateSql(noOfComponents, m_underscoreid);
                    statement.executeUpdate(sql);
                    id = m_underscoreid;
                    if (m_underscoreindexesChanged) deleteComponents(conn, id);
                } else {
                    PreparedStatement pStmt = getInsertPrepStmt(conn, noOfComponents);
                    pStmt.executeUpdate();
                    Integer res = DbCommon.getAutoGenId(parent, context, pStmt);
                    if (res == null) return;
                    id = res.intValue();
                }
                if (!m_underscoreupdate || m_underscoreindexesChanged) {
                    PreparedStatement insertCompPrepStmt = conn.prepareStatement(getInsertComponentPrepStmtSql());
                    for (int i = 0; i < noOfComponents; i++) {
                        createComponent(progressMonitor, m_underscorecomponents, pmNote, id, i, insertCompPrepStmt);
                    }
                }
                conn.commit();
                m_underscoreitemTable.getPrimaryId().setVal(m_underscoreitem, id);
                m_underscoreitemCache.updateCache(m_underscoreitem, id);
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

