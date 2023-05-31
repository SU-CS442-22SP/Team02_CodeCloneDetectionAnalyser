    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameterValues(Constants.PARAM_underscoreUUID)[0];
        String datastream = null;
        if (req.getRequestURI().contains(Constants.SERVLET_underscoreDOWNLOAD_underscoreFOXML_underscorePREFIX)) {
            resp.addHeader("Content-Disposition", "attachment; ContentType = \"text/xml\"; filename=\"" + uuid + "_underscorelocal_underscoreversion.foxml\"");
        } else {
            datastream = req.getParameterValues(Constants.PARAM_underscoreDATASTREAM)[0];
            resp.addHeader("Content-Disposition", "attachment; ContentType = \"text/xml\"; filename=\"" + uuid + "_underscorelocal_underscoreversion_underscore" + datastream + ".xml\"");
        }
        String xmlContent = URLDecoder.decode(req.getParameterValues(Constants.PARAM_underscoreCONTENT)[0], "UTF-8");
        InputStream is = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
        ServletOutputStream os = resp.getOutputStream();
        IOUtils.copyStreams(is, os);
        os.flush();
    }

