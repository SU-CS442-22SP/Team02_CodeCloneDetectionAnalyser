    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameterValues(Constants.PARAM_underscoreUUID)[0];
        String datastream = null;
        if (req.getRequestURI().contains(Constants.SERVLET_underscoreDOWNLOAD_underscoreFOXML_underscorePREFIX)) {
            resp.addHeader("Content-Disposition", "attachment; ContentType = \"text/xml\"; filename=\"" + uuid + "_underscoreserver_underscoreversion.foxml\"");
        } else {
            datastream = req.getParameterValues(Constants.PARAM_underscoreDATASTREAM)[0];
            resp.addHeader("Content-Disposition", "attachment; ContentType = \"text/xml\"; filename=\"" + uuid + "_underscoreserver_underscoreversion_underscore" + datastream + ".xml\"");
        }
        ServletOutputStream os = resp.getOutputStream();
        if (uuid != null && !"".equals(uuid)) {
            try {
                StringBuffer sb = new StringBuffer();
                if (req.getRequestURI().contains(Constants.SERVLET_underscoreDOWNLOAD_underscoreFOXML_underscorePREFIX)) {
                    sb.append(config.getFedoraHost()).append("/objects/").append(uuid).append("/objectXML");
                } else if (req.getRequestURI().contains(Constants.SERVLET_underscoreDOWNLOAD_underscoreDATASTREAMS_underscorePREFIX)) {
                    sb.append(config.getFedoraHost()).append("/objects/").append(uuid).append("/datastreams/").append(datastream).append("/content");
                }
                InputStream is = RESTHelper.get(sb.toString(), config.getFedoraLogin(), config.getFedoraPassword(), false);
                if (is == null) {
                    return;
                }
                try {
                    if (req.getRequestURI().contains(Constants.SERVLET_underscoreDOWNLOAD_underscoreDATASTREAMS_underscorePREFIX)) {
                        os.write(Constants.XML_underscoreHEADER_underscoreWITH_underscoreBACKSLASHES.getBytes());
                    }
                    IOUtils.copyStreams(is, os);
                } catch (IOException e) {
                    resp.setStatus(HttpURLConnection.HTTP_underscoreNOT_underscoreFOUND);
                    LOGGER.error("Problem with downloading foxml.", e);
                } finally {
                    os.flush();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            resp.setStatus(HttpURLConnection.HTTP_underscoreNOT_underscoreFOUND);
                            LOGGER.error("Problem with downloading foxml.", e);
                        } finally {
                            is = null;
                        }
                    }
                }
            } catch (IOException e) {
                resp.setStatus(HttpURLConnection.HTTP_underscoreNOT_underscoreFOUND);
                LOGGER.error("Problem with downloading foxml.", e);
            } finally {
                os.flush();
            }
        }
    }

