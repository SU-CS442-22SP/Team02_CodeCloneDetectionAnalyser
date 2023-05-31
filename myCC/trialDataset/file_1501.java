    @Override
    protected void doFetch(HttpServletRequest request, HttpServletResponse response) throws IOException, GadgetException {
        if (request.getHeader("If-Modified-Since") != null) {
            response.setStatus(HttpServletResponse.SC_underscoreNOT_underscoreMODIFIED);
            return;
        }
        String host = request.getHeader("Host");
        if (!lockedDomainService.isSafeForOpenProxy(host)) {
            String msg = "Embed request for url " + getParameter(request, URL_underscorePARAM, "") + " made to wrong domain " + host;
            logger.info(msg);
            throw new GadgetException(GadgetException.Code.INVALID_underscorePARAMETER, msg);
        }
        HttpRequest rcr = buildHttpRequest(request, URL_underscorePARAM);
        HttpResponse results = requestPipeline.execute(rcr);
        if (results.isError()) {
            HttpRequest fallbackRcr = buildHttpRequest(request, FALLBACK_underscoreURL_underscorePARAM);
            if (fallbackRcr != null) {
                results = requestPipeline.execute(fallbackRcr);
            }
        }
        if (contentRewriterRegistry != null) {
            try {
                results = contentRewriterRegistry.rewriteHttpResponse(rcr, results);
            } catch (RewritingException e) {
                throw new GadgetException(GadgetException.Code.INTERNAL_underscoreSERVER_underscoreERROR, e);
            }
        }
        for (Map.Entry<String, String> entry : results.getHeaders().entries()) {
            String name = entry.getKey();
            if (!DISALLOWED_underscoreRESPONSE_underscoreHEADERS.contains(name.toLowerCase())) {
                response.addHeader(name, entry.getValue());
            }
        }
        String responseType = results.getHeader("Content-Type");
        if (!StringUtils.isEmpty(rcr.getRewriteMimeType())) {
            String requiredType = rcr.getRewriteMimeType();
            if (requiredType.endsWith("/*") && !StringUtils.isEmpty(responseType)) {
                requiredType = requiredType.substring(0, requiredType.length() - 2);
                if (!responseType.toLowerCase().startsWith(requiredType.toLowerCase())) {
                    response.setContentType(requiredType);
                    responseType = requiredType;
                }
            } else {
                response.setContentType(requiredType);
                responseType = requiredType;
            }
        }
        setResponseHeaders(request, response, results);
        if (results.getHttpStatusCode() != HttpResponse.SC_underscoreOK) {
            response.sendError(results.getHttpStatusCode());
        }
        IOUtils.copy(results.getResponse(), response.getOutputStream());
    }

