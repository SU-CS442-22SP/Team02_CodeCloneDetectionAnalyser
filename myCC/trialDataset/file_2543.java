    public String doAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadFileForm vo = (UploadFileForm) form;
        String review = request.getParameter("review");
        String realpath = getServlet().getServletContext().getRealPath("/");
        realpath = realpath.replaceAll("\\\\", "/");
        String inforId = request.getParameter("inforId");
        request.setAttribute("id", inforId);
        String str_underscorepostFIX = "";
        int i_underscorep = 0;
        if (null == review) {
            FormFile file = vo.getFile();
            if (file != null) {
                String rootFilePath = getServlet().getServletContext().getRealPath(request.getContextPath());
                rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.strPath).toString();
                String strAppend = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
                if (file.getFileSize() != 0) {
                    file.getInputStream();
                    String name = file.getFileName();
                    i_underscorep = file.getFileName().lastIndexOf(".");
                    str_underscorepostFIX = file.getFileName().substring(i_underscorep, file.getFileName().length());
                    String fullPath = realpath + "attach/" + strAppend + str_underscorepostFIX;
                    t_underscoreattach attach = new t_underscoreattach();
                    attach.setAttach_underscorefullname(fullPath);
                    attach.setAttach_underscorename(name);
                    attach.setInfor_underscoreid(Integer.parseInt(inforId));
                    attach.setInsert_underscoreday(new Date());
                    attach.setUpdate_underscoreday(new Date());
                    t_underscoreattach_underscoreEditMap attachEdit = new t_underscoreattach_underscoreEditMap();
                    attachEdit.add(attach);
                    File sysfile = new File(fullPath);
                    if (!sysfile.exists()) {
                        sysfile.createNewFile();
                    }
                    java.io.OutputStream out = new FileOutputStream(sysfile);
                    org.apache.commons.io.IOUtils.copy(file.getInputStream(), out);
                    out.close();
                }
            }
            request.setAttribute("operating-status", "�����ɹ�!  ��ӭ����ʹ�á�");
            return "editsave";
        } else {
            String rootFilePath = getServlet().getServletContext().getRealPath(request.getContextPath());
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.strPath).toString();
            FormFile file = vo.getFile();
            FormFile file2 = vo.getFile2();
            FormFile file3 = vo.getFile3();
            t_underscoreinfor_underscorereview newreview = new t_underscoreinfor_underscorereview();
            String content = request.getParameter("content");
            newreview.setContent(content);
            if (null != inforId) newreview.setInfor_underscoreid(Integer.parseInt(inforId));
            newreview.setInsert_underscoreday(new Date());
            UserDetails user = LoginUtils.getLoginUser(request);
            newreview.setCreate_underscorename(user.getUsercode());
            if (null != file.getFileName() && !"".equals(file.getFileName())) {
                newreview.setAttachname1(file.getFileName());
                String strAppend1 = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
                i_underscorep = file.getFileName().lastIndexOf(".");
                str_underscorepostFIX = file.getFileName().substring(i_underscorep, file.getFileName().length());
                newreview.setAttachfullname1(realpath + "attach/" + strAppend1 + str_underscorepostFIX);
                saveFile(file.getInputStream(), realpath + "attach/" + strAppend1 + str_underscorepostFIX);
            }
            if (null != file2.getFileName() && !"".equals(file2.getFileName())) {
                newreview.setAttachname2(file2.getFileName());
                String strAppend2 = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
                i_underscorep = file2.getFileName().lastIndexOf(".");
                str_underscorepostFIX = file2.getFileName().substring(i_underscorep, file2.getFileName().length());
                newreview.setAttachfullname2(realpath + "attach/" + strAppend2 + str_underscorepostFIX);
                saveFile(file2.getInputStream(), realpath + "attach/" + strAppend2 + str_underscorepostFIX);
            }
            if (null != file3.getFileName() && !"".equals(file3.getFileName())) {
                newreview.setAttachname3(file3.getFileName());
                String strAppend3 = (new StringBuilder(String.valueOf(UUIDGenerator.nextHex()))).append(UploadFileOne.getFileType(file)).toString();
                i_underscorep = file3.getFileName().lastIndexOf(".");
                str_underscorepostFIX = file3.getFileName().substring(i_underscorep, file3.getFileName().length());
                newreview.setAttachfullname3(realpath + "attach/" + strAppend3 + str_underscorepostFIX);
                saveFile(file3.getInputStream(), realpath + "attach/" + strAppend3 + str_underscorepostFIX);
            }
            t_underscoreinfor_underscorereview_underscoreEditMap reviewEdit = new t_underscoreinfor_underscorereview_underscoreEditMap();
            reviewEdit.add(newreview);
            request.setAttribute("review", "1");
            return "aftersave";
        }
    }

