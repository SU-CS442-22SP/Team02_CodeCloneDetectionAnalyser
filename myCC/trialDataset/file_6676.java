    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
        Writer out = null;
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            out = renderResponse.getWriter();
            if (log.isDebugEnabled()) log.debug("Start commit new image");
            AuthSession auth_underscore = (AuthSession) renderRequest.getUserPrincipal();
            if (auth_underscore == null || !auth_underscore.isUserInRole("webmill.upload_underscoreimage")) {
                throw new PortletSecurityException("You have not enough right");
            }
            dbDyn = DatabaseAdapter.getInstance();
            if (log.isDebugEnabled()) log.debug("urlString - " + renderRequest.getParameter("url_underscoredownload"));
            String urlString = renderRequest.getParameter("url_underscoredownload").trim();
            if (urlString == null) throw new IllegalArgumentException("id_underscorefirm not initialized");
            if (log.isDebugEnabled()) log.debug("result url_underscoredownload " + urlString);
            String ext[] = { ".jpg", ".jpeg", ".gif", ".png" };
            int i;
            for (i = 0; i < ext.length; i++) {
                if ((ext[i] != null) && urlString.toLowerCase().endsWith(ext[i].toLowerCase())) break;
            }
            if (i == ext.length) throw new UploadFileException("Unsupported file extension. Error #20.03");
            if (log.isDebugEnabled()) log.debug("id_underscoremain - " + PortletService.getLong(renderRequest, "id_underscoremain"));
            Long id_underscoremain = PortletService.getLong(renderRequest, "id_underscoremain");
            if (id_underscoremain == null) throw new IllegalArgumentException("id_underscorefirm not initialized");
            String desc = RequestTools.getString(renderRequest, "d");
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_underscoreimage_underscorenumber_underscorefile");
            seq.setTableName("MAIN_underscoreFORUM_underscoreTHREADS");
            seq.setColumnName("ID_underscoreTHREAD");
            Long currID = dbDyn.getSequenceNextValue(seq);
            String storage_underscore = portletConfig.getPortletContext().getRealPath("/") + File.separatorChar + "image";
            String fileName = storage_underscore + File.separatorChar;
            if (log.isDebugEnabled()) log.debug("filename - " + fileName);
            URL url = new URL(urlString);
            File fileUrl = new File(url.getFile());
            if (log.isDebugEnabled()) log.debug("fileUrl - " + fileUrl);
            String newFileName = StringTools.appendString("" + currID, '0', 7, true) + "-" + fileUrl.getName();
            if (log.isDebugEnabled()) log.debug("newFileName " + newFileName);
            fileName += newFileName;
            if (log.isDebugEnabled()) log.debug("file to write " + fileName);
            InputStream is = url.openStream();
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            byte bytes[] = new byte[1000];
            int count = 0;
            while ((count = is.read(bytes)) != -1) {
                fos.write(bytes, 0, count);
            }
            fos.close();
            fos = null;
            is.close();
            is = null;
            url = null;
            out.write(DateUtils.getCurrentDate("dd-MMMM-yyyy HH:mm:ss:SS", renderRequest.getLocale()) + "<br>");
            ps = dbDyn.prepareStatement("insert into WM_underscoreIMAGE_underscoreDIR " + "( id_underscoreimage_underscoredir, ID_underscoreFIRM, is_underscoregroup, id, id_underscoremain, name_underscorefile, description )" + "(select seq_underscoreWM_underscoreIMAGE_underscoreDIR.nextval, ID_underscoreFIRM, 0, ?, ?, ?, ? " + " from WM_underscoreAUTH_underscoreUSER where user_underscorelogin = ? )");
            RsetTools.setLong(ps, 1, currID);
            RsetTools.setLong(ps, 2, id_underscoremain);
            ps.setString(3, "/image/" + newFileName);
            ps.setString(4, desc);
            ps.setString(5, auth_underscore.getUserLogin());
            ps.executeUpdate();
            dbDyn.commit();
            out.write("�������� ������ ������ ��� ������<br>" + "�������� ���� " + newFileName + "<br>" + DateUtils.getCurrentDate("dd-MMMM-yyyy HH:mm:ss:SS", renderRequest.getLocale()) + "<br>" + "<br>" + "<p><a href=\"" + PortletService.url("mill.image.index", renderRequest, renderResponse) + "\">��������� ������ ��������</a></p><br>" + "<p><a href=\"" + PortletService.url(ContainerConstants.CTX_underscoreTYPE_underscoreINDEX, renderRequest, renderResponse) + "\">�� ������� ��������</a></p>");
        } catch (Exception e) {
            try {
                dbDyn.rollback();
            } catch (Exception e1) {
            }
            final String es = "Error upload image from url";
            log.error(es, e);
            throw new PortletException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }

