    public void doGet(HttpServletRequest request_underscore, HttpServletResponse response) throws IOException, ServletException {
        Writer out = null;
        DatabaseAdapter dbDyn = null;
        PreparedStatement st = null;
        try {
            RenderRequest renderRequest = null;
            RenderResponse renderResponse = null;
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_underscoreTYPE_underscoreUTF8);
            out = response.getWriter();
            AuthSession auth_underscore = (AuthSession) renderRequest.getUserPrincipal();
            if (auth_underscore == null) {
                throw new IllegalStateException("You have not enough right to execute this operation");
            }
            PortletSession session = renderRequest.getPortletSession();
            dbDyn = DatabaseAdapter.getInstance();
            String index_underscorepage = PortletService.url("mill.price.index", renderRequest, renderResponse);
            Long id_underscoreshop = null;
            if (renderRequest.getParameter(ShopPortlet.NAME_underscoreID_underscoreSHOP_underscorePARAM) != null) {
                id_underscoreshop = PortletService.getLong(renderRequest, ShopPortlet.NAME_underscoreID_underscoreSHOP_underscorePARAM);
            } else {
                Long id_underscore = (Long) session.getAttribute(ShopPortlet.ID_underscoreSHOP_underscoreSESSION);
                if (id_underscore == null) {
                    response.sendRedirect(index_underscorepage);
                    return;
                }
                id_underscoreshop = id_underscore;
            }
            session.removeAttribute(ShopPortlet.ID_underscoreSHOP_underscoreSESSION);
            session.setAttribute(ShopPortlet.ID_underscoreSHOP_underscoreSESSION, id_underscoreshop);
            if (auth_underscore.isUserInRole("webmill.edit_underscoreprice_underscorelist")) {
                Long id_underscoreitem = PortletService.getLong(renderRequest, "id_underscoreitem");
                if (id_underscoreitem == null) throw new IllegalArgumentException("id_underscoreitem not initialized");
                if (RequestTools.getString(renderRequest, "action").equals("update")) {
                    dbDyn.getConnection().setAutoCommit(false);
                    String sql_underscore = "delete from WM_underscorePRICE_underscoreITEM_underscoreDESCRIPTION a " + "where exists " + " ( select null from WM_underscorePRICE_underscoreLIST b " + "   where b.id_underscoreshop = ? and b.id_underscoreitem = ? and " + "         a.id_underscoreitem=b.id_underscoreitem ) ";
                    try {
                        st = dbDyn.prepareStatement(sql_underscore);
                        RsetTools.setLong(st, 1, id_underscoreshop);
                        RsetTools.setLong(st, 2, id_underscoreitem);
                        st.executeUpdate();
                    } catch (Exception e0001) {
                        dbDyn.rollback();
                        out.write("Error #1 - " + ExceptionTools.getStackTrace(e0001, 20, "<br>"));
                        return;
                    } finally {
                        DatabaseManager.close(st);
                        st = null;
                    }
                    sql_underscore = "insert into WM_underscorePRICE_underscoreITEM_underscoreDESCRIPTION " + "(ID_underscorePRICE_underscoreITEM_underscoreDESCRIPTION, ID_underscoreITEM, TEXT)" + "(select seq_underscoreWM_underscorePRICE_underscoreITEM_underscoreDESCRIPTION.nextval, ID_underscoreITEM, ? " + " from WM_underscorePRICE_underscoreLIST b where b.ID_underscoreSHOP = ? and b.ID_underscoreITEM = ? )";
                    try {
                        int idx = 0;
                        int offset = 0;
                        int j = 0;
                        byte[] b = StringTools.getBytesUTF(RequestTools.getString(renderRequest, "n"));
                        st = dbDyn.prepareStatement(sql_underscore);
                        while ((idx = StringTools.getStartUTF(b, 2000, offset)) != -1) {
                            st.setString(1, new String(b, offset, idx - offset, "utf-8"));
                            RsetTools.setLong(st, 2, id_underscoreshop);
                            RsetTools.setLong(st, 3, id_underscoreitem);
                            st.addBatch();
                            offset = idx;
                            if (j > 10) break;
                            j++;
                        }
                        int[] updateCounts = st.executeBatch();
                        if (log.isDebugEnabled()) log.debug("Number of updated records - " + updateCounts);
                        dbDyn.commit();
                    } catch (Exception e0) {
                        dbDyn.rollback();
                        out.write("Error #2 - " + ExceptionTools.getStackTrace(e0, 20, "<br>"));
                        return;
                    } finally {
                        dbDyn.getConnection().setAutoCommit(true);
                        if (st != null) {
                            DatabaseManager.close(st);
                            st = null;
                        }
                    }
                }
                if (RequestTools.getString(renderRequest, "action").equals("new_underscoreimage") && renderRequest.getParameter("id_underscoreimage") != null) {
                    Long id_underscoreimage = PortletService.getLong(renderRequest, "id_underscoreimage");
                    dbDyn.getConnection().setAutoCommit(false);
                    String sql_underscore = "delete from WM_underscoreIMAGE_underscorePRICE_underscoreITEMS a " + "where exists " + " ( select null from WM_underscorePRICE_underscoreLIST b " + "where b.id_underscoreshop = ? and b.id_underscoreitem = ? and " + "a.id_underscoreitem=b.id_underscoreitem ) ";
                    try {
                        st = dbDyn.prepareStatement(sql_underscore);
                        RsetTools.setLong(st, 1, id_underscoreshop);
                        RsetTools.setLong(st, 2, id_underscoreitem);
                        st.executeUpdate();
                    } catch (Exception e0001) {
                        dbDyn.rollback();
                        out.write("Error #3 - " + ExceptionTools.getStackTrace(e0001, 20, "<br>"));
                        return;
                    } finally {
                        DatabaseManager.close(st);
                        st = null;
                    }
                    sql_underscore = "insert into WM_underscoreIMAGE_underscorePRICE_underscoreITEMS " + "(id_underscoreIMAGE_underscorePRICE_underscoreITEMS, id_underscoreitem, ID_underscoreIMAGE_underscoreDIR)" + "(select seq_underscoreWM_underscoreIMAGE_underscorePRICE_underscoreITEMS.nextval, id_underscoreitem, ? " + " from WM_underscorePRICE_underscoreLIST b where b.id_underscoreshop = ? and b.id_underscoreitem = ? )";
                    try {
                        st = dbDyn.prepareStatement(sql_underscore);
                        RsetTools.setLong(st, 1, id_underscoreimage);
                        RsetTools.setLong(st, 2, id_underscoreshop);
                        RsetTools.setLong(st, 3, id_underscoreitem);
                        int updateCounts = st.executeUpdate();
                        if (log.isDebugEnabled()) log.debug("Number of updated records - " + updateCounts);
                        dbDyn.commit();
                    } catch (Exception e0) {
                        dbDyn.rollback();
                        log.error("Error insert image", e0);
                        out.write("Error #4 - " + ExceptionTools.getStackTrace(e0, 20, "<br>"));
                        return;
                    } finally {
                        dbDyn.getConnection().setAutoCommit(true);
                        DatabaseManager.close(st);
                        st = null;
                    }
                }
                if (true) throw new Exception("Need refactoring");
            }
        } catch (Exception e) {
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        } finally {
            DatabaseManager.close(dbDyn, st);
            st = null;
            dbDyn = null;
        }
    }

