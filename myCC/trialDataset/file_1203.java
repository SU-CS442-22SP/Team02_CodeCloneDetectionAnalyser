    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
        Writer out = null;
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            AuthSession auth_underscore = (AuthSession) renderRequest.getUserPrincipal();
            if (auth_underscore == null || !auth_underscore.isUserInRole("webmill.upload_underscoreimage")) {
                throw new PortletSecurityException("You have not enough right");
            }
            if (log.isDebugEnabled()) log.debug("Start commit new image from file");
            dbDyn = DatabaseAdapter.getInstance();
            String index_underscorepage = PortletService.url("mill.image.index", renderRequest, renderResponse);
            if (log.isDebugEnabled()) log.debug("right to commit image - " + auth_underscore.isUserInRole("webmill.upload_underscoreimage"));
            PortletSession sess = renderRequest.getPortletSession(true);
            if ((sess.getAttribute("MILL.IMAGE.ID_underscoreMAIN") == null) || (sess.getAttribute("MILL.IMAGE.DESC_underscoreIMAGE") == null)) {
                out.write("Not all parametrs initialized");
                return;
            }
            Long id_underscoremain = (Long) sess.getAttribute("MILL.IMAGE.ID_underscoreMAIN");
            String desc = ((String) sess.getAttribute("MILL.IMAGE.DESC_underscoreIMAGE"));
            if (log.isDebugEnabled()) log.debug("image description " + desc);
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_underscoreimage_underscorenumber_underscorefile");
            seq.setTableName("MAIN_underscoreFORUM_underscoreTHREADS");
            seq.setColumnName("ID_underscoreTHREAD");
            Long currID = dbDyn.getSequenceNextValue(seq);
            String storage_underscore = portletConfig.getPortletContext().getRealPath("/") + File.separatorChar + "image";
            String fileName = storage_underscore + File.separator + StringTools.appendString("" + currID, '0', 7, true) + "-";
            if (log.isDebugEnabled()) log.debug("image fileName " + fileName);
            String newFileName = "";
            String supportExtension[] = { ".jpg", ".jpeg", ".gif", ".png" };
            try {
                if (true) throw new UploadFileException("Todo need fix");
            } catch (UploadFileException e) {
                log.error("Error save image to disk", e);
                out.write("<html><head></head<body>" + "Error while processing this page:<br>" + ExceptionTools.getStackTrace(e, 20, "<br>") + "<br>" + "<p><a href=\"" + index_underscorepage + "\">continue</a></p>" + "</body></html>");
                return;
            }
            if (log.isDebugEnabled()) log.debug("newFileName " + newFileName);
            UserInfo userInfo = auth_underscore.getUserInfo();
            CustomSequenceType seqImageDir = new CustomSequenceType();
            seqImageDir.setSequenceName("seq_underscoreWM_underscoreimage_underscoredir");
            seqImageDir.setTableName("WM_underscoreIMAGE_underscoreDIR");
            seqImageDir.setColumnName("ID_underscoreIMAGE_underscoreDIR");
            Long seqValue = dbDyn.getSequenceNextValue(seqImageDir);
            ps = dbDyn.prepareStatement("insert into WM_underscoreIMAGE_underscoreDIR " + "( ID_underscoreIMAGE_underscoreDIR, ID_underscoreFIRM, is_underscoregroup, id, id_underscoremain, name_underscorefile, description )" + "(?, ?, 0, ?, ?, ?, ?");
            RsetTools.setLong(ps, 1, seqValue);
            RsetTools.setLong(ps, 2, userInfo.getCompanyId());
            RsetTools.setLong(ps, 3, currID);
            RsetTools.setLong(ps, 4, id_underscoremain);
            ps.setString(5, "/image/" + newFileName);
            ps.setString(6, desc);
            ps.executeUpdate();
            dbDyn.commit();
            if (log.isDebugEnabled()) log.debug("redirect to indexPage - " + index_underscorepage);
            out.write("Image successful uploaded");
            return;
        } catch (Exception e) {
            try {
                dbDyn.rollback();
            } catch (SQLException e1) {
            }
            final String es = "Error upload image";
            log.error(es, e);
            throw new PortletException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

