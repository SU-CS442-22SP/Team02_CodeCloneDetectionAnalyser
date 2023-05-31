    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentId = req.getParameter(CONTENT_underscoreID);
        String contentType = req.getParameter(CONTENT_underscoreTYPE);
        if (contentId == null || contentType == null) {
            resp.sendError(HttpServletResponse.SC_underscoreBAD_underscoreREQUEST, "Content id or content type not specified");
            return;
        }
        try {
            switch(ContentType.valueOf(contentType)) {
                case IMAGE:
                    resp.setContentType("image/jpeg");
                    break;
                case AUDIO:
                    resp.setContentType("audio/mp3");
                    break;
                case VIDEO:
                    resp.setContentType("video/mpeg");
                    break;
                default:
                    throw new IllegalStateException("Invalid content type specified");
            }
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_underscoreBAD_underscoreREQUEST, "Invalid content type specified");
            return;
        }
        String baseUrl = this.getServletContext().getInitParameter(BASE_underscoreURL);
        URL url = new URL(baseUrl + "/" + contentType.toLowerCase() + "/" + contentId);
        URLConnection conn = url.openConnection();
        resp.setContentLength(conn.getContentLength());
        IOUtils.copy(conn.getInputStream(), resp.getOutputStream());
    }

