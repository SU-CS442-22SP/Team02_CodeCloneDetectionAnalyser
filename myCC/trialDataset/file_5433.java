    public void _underscorejspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _underscorejspx_underscoreout = null;
        PageContext _underscorejspx_underscorepage_underscorecontext = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            pageContext = _underscorejspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _underscorejspx_underscorepage_underscorecontext = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _underscorejspx_underscoreout = out;
            _underscorejspx_underscoreresourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            String username = "test";
            String password = "test";
            int providerId = 1;
            if (request.getParameter("providerId") != null) providerId = Integer.parseInt(request.getParameter("providerId"));
            String thisPageContextAddress = "http://localhost:8080/" + request.getContextPath();
            String thisPageServingAddress = thisPageContextAddress + "/index.jsp";
            String token = "";
            String token_underscoretimeout = (String) request.getParameter("token_underscoretimeout");
            String referer = request.getHeader("Referer");
            if (token_underscoretimeout != null && token_underscoretimeout.equals("true")) {
                System.out.println("token timeout for referer" + referer);
                if (referer != null) {
                    if (request.getSession().getServletContext().getAttribute("token_underscoretimeout_underscoreprocessing_underscorelock") == null) {
                        request.getSession().getServletContext().setAttribute("token_underscoretimeout_underscoreprocessing_underscorelock", true);
                        byte[] buff = null;
                        BufferedInputStream bis = null;
                        URL url = new URL(thisPageContextAddress + "/ServerAdminServlet?action=login&username=" + username + "&password=" + password);
                        URLConnection urlc = url.openConnection();
                        int length = urlc.getContentLength();
                        InputStream in = urlc.getInputStream();
                        buff = new byte[length];
                        int bytesRead = 0;
                        while (bytesRead < length) {
                            bytesRead += in.read(buff, bytesRead, in.available());
                        }
                        token = new String(buff);
                        token = token.replaceAll("[\\r\\f]", "");
                        token = token.trim();
                        request.getSession().getServletContext().setAttribute("token", token);
                        out.println(token);
                        request.getSession().getServletContext().removeAttribute("token_underscoretimeout_underscoreprocessing_underscorelock");
                    } else out.println("token_underscoretimeout_underscoreprocessing_underscorelock");
                }
            } else {
                if (request.getSession().getServletContext().getAttribute("token") == null || request.getSession().getServletContext().getAttribute("token").equals("")) {
                    byte[] buff = null;
                    BufferedInputStream bis = null;
                    URL url = new URL(thisPageContextAddress + "/ServerAdminServlet?action=login&username=" + username + "&password=" + password);
                    URLConnection urlc = url.openConnection();
                    int length = urlc.getContentLength();
                    InputStream in = urlc.getInputStream();
                    buff = new byte[length];
                    int bytesRead = 0;
                    while (bytesRead < length) {
                        bytesRead += in.read(buff, bytesRead, in.available());
                    }
                    token = new String(buff);
                    token = token.replaceAll("[\\r\\f]", "");
                    token = token.trim();
                    request.getSession().getServletContext().setAttribute("token", token);
                }
                out.write("<html>\n");
                out.write("  <head>\n");
                out.write("    <title>AJAX test </title>\n");
                out.write("    <script type=\"text/javascript\" src=\"OpenLayers-2.8/OpenLayers.js\"></script>\n");
                out.write("    <script type=\"text/javascript\">\n");
                out.write("\n");
                out.write("        function init(){\n");
                out.write("\n");
                out.write("            var token = \"");
                out.print(request.getSession().getServletContext().getAttribute("token"));
                out.write("\";\n");
                out.write("\n");
                out.write("            var options = {\n");
                out.write("                            maxExtent: new OpenLayers.Bounds(-600, -300, 0, 400),\n");
                out.write("                            maxResolution: 4.77730, minRezolution: 78271.517, numZoomLevels: 15, units: 'm'\n");
                out.write("                          }\n");
                out.write("\n");
                out.write("\n");
                out.write("            var map = new OpenLayers.Map('map', options);\n");
                out.write("            var layer = new OpenLayers.Layer.TMS(\"TMS\", \"/WebGISTileServer/TMSServletProxy/\" + token + \"/7/\",\n");
                out.write("                                                { layername: 'TMS', type: 'png' });\n");
                out.write("            map.addLayer(layer);\n");
                out.write("            map.addControl(new OpenLayers.Control.MousePosition());\n");
                out.write("            map.setCenter(new OpenLayers.LonLat(-300, 120), 4);\n");
                out.write("        }\n");
                out.write("    </script>\n");
                out.write("  </head>\n");
                out.write("  <body onload=\"init()\">\n");
                out.write("      <H1>TeleAtlas map with TMS</H1>\n");
                out.write("      <div id=\"map\" style=\"width:100%; height:90%\"></div>\n");
                out.write("  </body>\n");
                out.write("</html>");
            }
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _underscorejspx_underscoreout;
                if (out != null && out.getBufferSize() != 0) out.clearBuffer();
                if (_underscorejspx_underscorepage_underscorecontext != null) _underscorejspx_underscorepage_underscorecontext.handlePageException(t);
            }
        } finally {
            _underscorejspxFactory.releasePageContext(_underscorejspx_underscorepage_underscorecontext);
        }
    }

