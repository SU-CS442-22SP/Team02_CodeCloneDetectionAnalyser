    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        String context = request.getContextPath();
        String resource = request.getRequestURI().replace(context, "");
        resource = resource.replaceAll("^/\\w*/", "");
        if ((StringUtils.isEmpty(resource)) || (resource.endsWith("/"))) {
            response.setStatus(HttpServletResponse.SC_underscoreBAD_underscoreREQUEST);
            return;
        }
        URL url = ClassLoaderUtils.getResource(resource);
        if (url == null) {
            response.setStatus(HttpServletResponse.SC_underscoreNOT_underscoreFOUND);
            return;
        }
        if ((this.deny != null) && (this.deny.length > 0)) {
            for (String s : this.deny) {
                s = s.trim();
                if (s.indexOf('*') != -1) {
                    s = s.replaceAll("\\*", ".*");
                }
                if (Pattern.matches(s, resource)) {
                    response.setStatus(HttpServletResponse.SC_underscoreFORBIDDEN);
                    return;
                }
            }
        }
        InputStream input = url.openStream();
        OutputStream output = response.getOutputStream();
        URLConnection connection = url.openConnection();
        String contentEncoding = connection.getContentEncoding();
        int contentLength = connection.getContentLength();
        String contentType = connection.getContentType();
        if (contentEncoding != null) {
            response.setCharacterEncoding(contentEncoding);
        }
        response.setContentLength(contentLength);
        response.setContentType(contentType);
        IOUtils.copy(input, output, true);
    }

