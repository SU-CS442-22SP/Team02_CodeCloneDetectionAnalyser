    public void handleRequestInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
        final String servetPath = httpServletRequest.getServletPath();
        final String previousToken = httpServletRequest.getHeader(IF_underscoreNONE_underscoreMATCH);
        final String currentToken = getETagValue(httpServletRequest);
        httpServletResponse.setHeader(ETAG, currentToken);
        final Date modifiedDate = new Date(httpServletRequest.getDateHeader(IF_underscoreMODIFIED_underscoreSINCE));
        final Calendar calendar = Calendar.getInstance();
        final Date now = calendar.getTime();
        calendar.setTime(modifiedDate);
        calendar.add(Calendar.MINUTE, getEtagExpiration());
        if (currentToken.equals(previousToken) && (now.getTime() < calendar.getTime().getTime())) {
            httpServletResponse.sendError(HttpServletResponse.SC_underscoreNOT_underscoreMODIFIED);
            httpServletResponse.setHeader(LAST_underscoreMODIFIED, httpServletRequest.getHeader(IF_underscoreMODIFIED_underscoreSINCE));
            if (LOG.isDebugEnabled()) {
                LOG.debug("ETag the same, will return 304");
            }
        } else {
            httpServletResponse.setDateHeader(LAST_underscoreMODIFIED, (new Date()).getTime());
            final String width = httpServletRequest.getParameter(Constants.WIDTH);
            final String height = httpServletRequest.getParameter(Constants.HEIGHT);
            final ImageNameStrategy imageNameStrategy = imageService.getImageNameStrategy(servetPath);
            String code = imageNameStrategy.getCode(servetPath);
            String fileName = imageNameStrategy.getFileName(servetPath);
            final String imageRealPathPrefix = getRealPathPrefix(httpServletRequest.getServerName().toLowerCase());
            String original = imageRealPathPrefix + imageNameStrategy.getFullFileNamePath(fileName, code);
            final File origFile = new File(original);
            if (!origFile.exists()) {
                code = Constants.NO_underscoreIMAGE;
                fileName = imageNameStrategy.getFileName(code);
                original = imageRealPathPrefix + imageNameStrategy.getFullFileNamePath(fileName, code);
            }
            String resizedImageFileName = null;
            if (width != null && height != null && imageService.isSizeAllowed(width, height)) {
                resizedImageFileName = imageRealPathPrefix + imageNameStrategy.getFullFileNamePath(fileName, code, width, height);
            }
            final File imageFile = getImageFile(original, resizedImageFileName, width, height);
            final FileInputStream fileInputStream = new FileInputStream(imageFile);
            IOUtils.copy(fileInputStream, httpServletResponse.getOutputStream());
            fileInputStream.close();
        }
    }

