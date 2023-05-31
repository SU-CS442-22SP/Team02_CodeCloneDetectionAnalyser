    public String doAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");
        }
        t_underscoreinformation_underscoreEditMap editMap = new t_underscoreinformation_underscoreEditMap();
        try {
            t_underscoreinformation_underscoreForm vo = null;
            vo = (t_underscoreinformation_underscoreForm) form;
            vo.setCompany(vo.getCounty());
            if ("����".equals(vo.getInfo_underscoretype())) {
                vo.setInfo_underscorelevel(null);
                vo.setAlert_underscorelevel(null);
            }
            String str_underscorepostFIX = "";
            int i_underscorep = 0;
            editMap.add(vo);
            try {
                logger.info("���͹�˾�鱨��");
                String[] mobiles = request.getParameterValues("mobiles");
                vo.setMobiles(mobiles);
                SMSService.inforAlert(vo);
            } catch (Exception e) {
                logger.error("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            }
            String filename = vo.getFile().getFileName();
            if (null != filename && !"".equals(filename)) {
                FormFile file = vo.getFile();
                String realpath = getServlet().getServletContext().getRealPath("/");
                realpath = realpath.replaceAll("\\\\", "/");
                String inforId = vo.getId();
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
        } catch (HibernateException e) {
            logger.error("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            ActionErrors errors = new ActionErrors();
            errors.add("org.apache.struts.action.GLOBAL_underscoreERROR", new ActionError("error.database.save", e.toString()));
            saveErrors(request, errors);
            e.printStackTrace();
            request.setAttribute("t_underscoreinformation_underscoreForm", form);
            if (logger.isDebugEnabled()) {
                logger.debug("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return "addpage";
        }
        if (logger.isDebugEnabled()) {
            logger.debug("doAdd(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
        }
        return "aftersave";
    }

