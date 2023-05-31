    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Cache-Control", "max-age=" + Constants.HTTP_underscoreCACHE_underscoreSECONDS);
        String uuid = req.getRequestURI().substring(req.getRequestURI().indexOf(Constants.SERVLET_underscoreFULL_underscorePREFIX) + Constants.SERVLET_underscoreFULL_underscorePREFIX.length() + 1);
        boolean notScale = ClientUtils.toBoolean(req.getParameter(Constants.URL_underscorePARAM_underscoreNOT_underscoreSCALE));
        ServletOutputStream os = resp.getOutputStream();
        if (uuid != null && !"".equals(uuid)) {
            try {
                String mimetype = fedoraAccess.getMimeTypeForStream(uuid, FedoraUtils.IMG_underscoreFULL_underscoreSTREAM);
                if (mimetype == null) {
                    mimetype = "image/jpeg";
                }
                ImageMimeType loadFromMimeType = ImageMimeType.loadFromMimeType(mimetype);
                if (loadFromMimeType == ImageMimeType.JPEG || loadFromMimeType == ImageMimeType.PNG) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(config.getFedoraHost()).append("/objects/").append(uuid).append("/datastreams/IMG_underscoreFULL/content");
                    InputStream is = RESTHelper.get(sb.toString(), config.getFedoraLogin(), config.getFedoraPassword(), false);
                    if (is == null) {
                        return;
                    }
                    try {
                        IOUtils.copyStreams(is, os);
                    } catch (IOException e) {
                        resp.setStatus(HttpURLConnection.HTTP_underscoreNOT_underscoreFOUND);
                        LOGGER.error("Unable to open full image.", e);
                    } finally {
                        os.flush();
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                resp.setStatus(HttpURLConnection.HTTP_underscoreNOT_underscoreFOUND);
                                LOGGER.error("Unable to close stream.", e);
                            } finally {
                                is = null;
                            }
                        }
                    }
                } else {
                    Image rawImg = KrameriusImageSupport.readImage(uuid, FedoraUtils.IMG_underscoreFULL_underscoreSTREAM, this.fedoraAccess, 0, loadFromMimeType);
                    BufferedImage scaled = null;
                    if (!notScale) {
                        scaled = KrameriusImageSupport.getSmallerImage(rawImg, 1250, 1000);
                    } else {
                        scaled = KrameriusImageSupport.getSmallerImage(rawImg, 2500, 2000);
                    }
                    KrameriusImageSupport.writeImageToStream(scaled, "JPG", os);
                    resp.setContentType(ImageMimeType.JPEG.getValue());
                    resp.setStatus(HttpURLConnection.HTTP_underscoreOK);
                }
            } catch (IOException e) {
                resp.setStatus(HttpURLConnection.HTTP_underscoreNOT_underscoreFOUND);
                LOGGER.error("Unable to open full image.", e);
            } catch (XPathExpressionException e) {
                resp.setStatus(HttpURLConnection.HTTP_underscoreNOT_underscoreFOUND);
                LOGGER.error("Unable to create XPath expression.", e);
            } finally {
                os.flush();
            }
        }
    }

