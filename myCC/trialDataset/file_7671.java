    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String act = request.getParameter("act");
            if (null == act) {
            } else if ("down".equalsIgnoreCase(act)) {
                String vest = request.getParameter("vest");
                String id = request.getParameter("id");
                if (null == vest) {
                    t_underscoreattach_underscoreForm attach = null;
                    t_underscoreattach_underscoreQueryMap query = new t_underscoreattach_underscoreQueryMap();
                    attach = query.getByID(id);
                    if (null != attach) {
                        String filename = attach.getAttach_underscorename();
                        String fullname = attach.getAttach_underscorefullname();
                        response.addHeader("Content-Disposition", "attachment;filename=" + filename + "");
                        File file = new File(fullname);
                        if (file.exists()) {
                            java.io.FileInputStream in = new FileInputStream(file);
                            org.apache.commons.io.IOUtils.copy(in, response.getOutputStream());
                        }
                    }
                } else if ("review".equalsIgnoreCase(vest)) {
                    t_underscoreinfor_underscorereview_underscoreQueryMap reviewQuery = new t_underscoreinfor_underscorereview_underscoreQueryMap();
                    t_underscoreinfor_underscorereview_underscoreForm review = reviewQuery.getByID(id);
                    String seq = request.getParameter("seq");
                    String name = null, fullname = null;
                    if ("1".equals(seq)) {
                        name = review.getAttachname1();
                        fullname = review.getAttachfullname1();
                    } else if ("2".equals(seq)) {
                        name = review.getAttachname2();
                        fullname = review.getAttachfullname2();
                    } else if ("3".equals(seq)) {
                        name = review.getAttachname3();
                        fullname = review.getAttachfullname3();
                    }
                    String downTypeStr = DownType.getInst().getDownTypeByFileName(name);
                    logger.debug("filename=" + name + " downtype=" + downTypeStr);
                    response.setContentType(downTypeStr);
                    response.addHeader("Content-Disposition", "attachment;filename=" + name + "");
                    File file = new File(fullname);
                    if (file.exists()) {
                        java.io.FileInputStream in = new FileInputStream(file);
                        org.apache.commons.io.IOUtils.copy(in, response.getOutputStream());
                        in.close();
                    }
                }
            } else if ("upload".equalsIgnoreCase(act)) {
                String infoId = request.getParameter("inforId");
                logger.debug("infoId=" + infoId);
            }
        } catch (Exception e) {
        }
    }

