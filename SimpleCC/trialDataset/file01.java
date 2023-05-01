class Foo {
    @Override
    public void insert(Connection conn) throws SQLException {
        PreparedStatement objectInsert = null;
        String sqlString = null;
        int newID = 0;
        try {
            conn.setAutoCommit(false);
            sqlString = "SELECT NEXTVAL(OBJ_SEQ) AS NEXTVAL";
            objectInsert = conn.prepareStatement(sqlString);
            ResultSet r = objectInsert.executeQuery(sqlString);
            newID = r.getInt("NEXTVAL");
            sqlString = "INSERT INTO OBJECTS" + "(" + "OBJ_ID," + "OBJ_NAME," + "OBTY_CDE" + ")" + "VALUES" + "(" + "?," + "?," + "?" + ")" + "";
            objectInsert = conn.prepareStatement(sqlString);
            objectInsert.setInt(1, newID);
            objectInsert.setString(2, getRoomKey());
            objectInsert.setString(3, "ROOM");
            objectInsert.executeUpdate();
            sqlString = "INSERT INTO ROOMS" + "(" + "";
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } finally {
            if (objectInsert != null) {
                objectInsert.close();
            }
            conn.setAutoCommit(true);
        }
    }
    public void elimina(Pedido pe) throws errorSQL, errorConexionBD {
        System.out.println("GestorPedido.elimina()");
        int id = pe.getId();
        String sql;
        Statement stmt = null;
        try {
            gd.begin();
            sql = "DELETE FROM pedido WHERE id=" + id;
            System.out.println("Ejecutando: " + sql);
            stmt = gd.getConexion().createStatement();
            stmt.executeUpdate(sql);
            System.out.println("executeUpdate");
            gd.commit();
            System.out.println("commit");
            stmt.close();
        } catch (SQLException e) {
            gd.rollback();
            throw new errorSQL(e.toString());
        } catch (errorConexionBD e) {
            System.err.println("Error en GestorPedido.elimina(): " + e);
        } catch (errorSQL e) {
            System.err.println("Error en GestorPedido.elimina(): " + e);
        }
    }
    public static void saveFileData(File file, File destination, java.io.File newDataFile) throws Exception {
        String fileName = file.getFileName();
        String assetsPath = FileFactory.getRealAssetsRootPath();
        new java.io.File(assetsPath).mkdir();
        java.io.File workingFile = getAssetIOFile(file);
        DotResourceCache vc = CacheLocator.getVeloctyResourceCache();
        vc.remove(ResourceManager.RESOURCE_TEMPLATE + workingFile.getPath());
        if (destination != null && destination.getInode() > 0) {
            FileInputStream is = new FileInputStream(workingFile);
            FileChannel channelFrom = is.getChannel();
            java.io.File newVersionFile = getAssetIOFile(destination);
            FileChannel channelTo = new FileOutputStream(newVersionFile).getChannel();
            channelFrom.transferTo(0, channelFrom.size(), channelTo);
            channelTo.force(false);
            channelTo.close();
            channelFrom.close();
        }
        if (newDataFile != null) {
            FileChannel writeCurrentChannel = new FileOutputStream(workingFile).getChannel();
            writeCurrentChannel.truncate(0);
            FileChannel fromChannel = new FileInputStream(newDataFile).getChannel();
            fromChannel.transferTo(0, fromChannel.size(), writeCurrentChannel);
            writeCurrentChannel.force(false);
            writeCurrentChannel.close();
            fromChannel.close();
            if (UtilMethods.isImage(fileName)) {
                BufferedImage img = javax.imageio.ImageIO.read(workingFile);
                int height = img.getHeight();
                file.setHeight(height);
                int width = img.getWidth();
                file.setWidth(width);
            }
            String folderPath = workingFile.getParentFile().getAbsolutePath();
            Identifier identifier = IdentifierCache.getIdentifierFromIdentifierCache(file);
            java.io.File directory = new java.io.File(folderPath);
            java.io.File[] files = directory.listFiles((new FileFactory()).new ThumbnailsFileNamesFilter(identifier));
            for (java.io.File iofile : files) {
                try {
                    iofile.delete();
                } catch (SecurityException e) {
                    Logger.error(FileFactory.class, "EditFileAction._saveWorkingFileData(): " + iofile.getName() + " cannot be erased. Please check the file permissions.");
                } catch (Exception e) {
                    Logger.error(FileFactory.class, "EditFileAction._saveWorkingFileData(): " + e.getMessage());
                }
            }
        }
    }
    public static void upper() throws Exception {
        File input = new File("dateiname");
        PostMethod post = new PostMethod("url");
        post.setRequestBody(new FileInputStream(input));
        if (input.length() < Integer.MAX_VALUE) post.setRequestContentLength((int) input.length()); else post.setRequestContentLength(EntityEnclosingMethod.CONTENT_LENGTH_CHUNKED);
        post.setRequestHeader("Content-type", "text/xml; charset=ISO-8859�1");
        HttpClient httpclient = new HttpClient();
        httpclient.executeMethod(post);
        post.releaseConnection();
        URL url = new URL("https://www.amazon.de/");
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        rd.close();
    }
    protected boolean testPort(String protocol, String server, int port, String file) {
        System.out.println("testPort[" + protocol + "," + server + ", " + port + ", " + file + "]");
        URL url = null;
        try {
            url = new URL(protocol, server, port, file);
        } catch (MalformedURLException ex) {
            log.severe("No URL for Protocol=" + protocol + ", Server=" + server + ": " + ex.getMessage());
            return false;
        }
        try {
            URLConnection c = url.openConnection();
            Object o = c.getContent();
            if (o == null) log.warning("In use=" + url); else log.warning("In Use=" + url);
        } catch (Exception ex) {
            log.fine("Not used=" + url);
            return false;
        }
        return true;
    }
    public void writeOutput(String directory) throws IOException {
        File f = new File(directory);
        int i = 0;
        if (f.isDirectory()) {
            for (AppInventorScreen screen : screens.values()) {
                File screenFile = new File(getScreenFilePath(f.getAbsolutePath(), screen));
                screenFile.getParentFile().mkdirs();
                screenFile.createNewFile();
                FileWriter out = new FileWriter(screenFile);
                String initial = files.get(i).toString();
                Map<String, String> types = screen.getTypes();
                String[] lines = initial.split("\n");
                for (String key : types.keySet()) {
                    if (!key.trim().equals(screen.getName().trim())) {
                        String value = types.get(key);
                        boolean varFound = false;
                        boolean importFound = false;
                        for (String line : lines) {
                            if (line.matches("^\\s*(public|private)\\s+" + value + "\\s+" + key + "\\s*=.*;$")) varFound = true;
                            if (line.matches("^\\s*(public|private)\\s+" + value + "\\s+" + key + "\\s*;$")) varFound = true;
                            if (line.matches("^\\s*import\\s+.*" + value + "\\s*;$")) importFound = true;
                        }
                        if (!varFound) initial = initial.replaceFirst("(?s)(?<=\\{\n)", "\tprivate " + value + " " + key + ";\n");
                        if (!importFound) initial = initial.replaceFirst("(?=import)", "import com.google.devtools.simple.runtime.components.android." + value + ";\n");
                    }
                }
                out.write(initial);
                out.close();
                i++;
            }
            File manifestFile = new File(getManifestFilePath(f.getAbsolutePath(), manifest));
            manifestFile.getParentFile().mkdirs();
            manifestFile.createNewFile();
            FileWriter out = new FileWriter(manifestFile);
            out.write(manifest.toString());
            out.close();
            File projectFile = new File(getProjectFilePath(f.getAbsolutePath(), project));
            projectFile.getParentFile().mkdirs();
            projectFile.createNewFile();
            out = new FileWriter(projectFile);
            out.write(project.toString());
            out.close();
            String[] copyResourceFilenames = { "proguard.cfg", "project.properties", "libSimpleAndroidRuntime.jar", "\\.classpath", "res/drawable/icon.png", "\\.settings/org.eclipse.jdt.core.prefs" };
            for (String copyResourceFilename : copyResourceFilenames) {
                InputStream is = getClass().getResourceAsStream("/resources/" + copyResourceFilename.replace("\\.", ""));
                File outputFile = new File(f.getAbsoluteFile() + File.separator + copyResourceFilename.replace("\\.", "."));
                outputFile.getParentFile().mkdirs();
                OutputStream os = new FileOutputStream(outputFile);
                byte[] buf = new byte[1024];
                int readBytes;
                if (is == null) System.out.println("/resources/" + copyResourceFilename.replace("\\.", ""));
                if (os == null) System.out.println(f.getAbsolutePath() + File.separator + copyResourceFilename.replace("\\.", "."));
                while ((readBytes = is.read(buf)) > 0) {
                    os.write(buf, 0, readBytes);
                }
            }
            for (String assetName : assets) {
                InputStream is = new FileInputStream(new File(assetsDir.getAbsolutePath() + File.separator + assetName));
                File outputFile = new File(f.getAbsoluteFile() + File.separator + assetName);
                outputFile.getParentFile().mkdirs();
                OutputStream os = new FileOutputStream(outputFile);
                byte[] buf = new byte[1024];
                int readBytes;
                while ((readBytes = is.read(buf)) > 0) {
                    os.write(buf, 0, readBytes);
                }
            }
            File assetsOutput = new File(getAssetsFilePath(f.getAbsolutePath()));
            new File(assetsDir.getAbsoluteFile() + File.separator + "assets").renameTo(assetsOutput);
        }
    }
    public void invoke() throws IOException {
        String[] command = new String[files.length + options.length + 2];
        command[0] = chmod;
        System.arraycopy(options, 0, command, 1, options.length);
        command[1 + options.length] = perms;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            command[2 + options.length + i] = file.getAbsolutePath();
        }
        Process p = Runtime.getRuntime().exec(command);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (p.exitValue() != 0) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(p.getErrorStream(), writer);
            throw new IOException("Unable to chmod files: " + writer.toString());
        }
    }
    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            String username = "test";
            String password = "test";
            int providerId = 1;
            if (request.getParameter("providerId") != null) providerId = Integer.parseInt(request.getParameter("providerId"));
            String thisPageContextAddress = "http://localhost:8080" + request.getContextPath();
            String thisPageServingAddress = thisPageContextAddress + "/index.jsp";
            String token = "";
            String token_timeout = (String) request.getParameter("token_timeout");
            String referer = request.getHeader("Referer");
            if (token_timeout != null && token_timeout.equals("true")) {
                System.out.println("token timeout for referer" + referer);
                if (referer != null) {
                    if (request.getSession().getServletContext().getAttribute("token_timeout_processing_lock") == null) {
                        request.getSession().getServletContext().setAttribute("token_timeout_processing_lock", true);
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
                        request.getSession().getServletContext().removeAttribute("token_timeout_processing_lock");
                    } else out.println("token_timeout_processing_lock");
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
                out.write("\n");
                out.write("<html>\n");
                out.write("  <head>\n");
                out.write("    <title>AJAX test </title>\n");
                out.write("    <link rel=\"stylesheet\" href=\"css/default.css\" type=\"text/css\" />\n");
                out.write("\n");
                out.write("    <script type=\"text/javascript\" src=\"../OpenLayers-2.8/OpenLayers.js\"></script>\n");
                out.write("    <script type=\"text/javascript\">\n");
                out.write("\n");
                out.write("        var map, layer;\n");
                out.write("\n");
                out.write("        var token = \"");
                out.print(request.getSession().getServletContext().getAttribute("token"));
                out.write("\";\n");
                out.write("\n");
                out.write("\n");
                out.write("        function init(){\n");
                out.write("\n");
                out.write("            OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;\n");
                out.write("\n");
                out.write("            var options = {\n");
                out.write("                maxExtent: new OpenLayers.Bounds(0, 0, 3000000, 9000000),\n");
                out.write("                tileSize :new OpenLayers.Size(250, 250),\n");
                out.write("                units: 'm',\n");
                out.write("                projection: 'EPSG:3006',\n");
                out.write("                resolutions : [1.3,2.6,4,6.6,13.2,26.5,66.1,132.3,264.6,793.8,1322.9,2645.8,13229.2,26458.3]\n");
                out.write("            }\n");
                out.write("\n");
                out.write("            map = new OpenLayers.Map('swedenMap', options);\n");
                out.write("\n");
                out.write("            layer = new OpenLayers.Layer.TMS(\"TMS\", \"http://localhost:8080/WebGISTileServer/TMSServletProxy/\",\n");
                out.write("                                            { layername: token + '/7', type: 'png' });\n");
                out.write("\n");
                out.write("            map.addLayer(layer);\n");
                out.write("\n");
                out.write("            map.addControl( new OpenLayers.Control.PanZoom() );\n");
                out.write("            map.addControl( new OpenLayers.Control.PanZoomBar() );\n");
                out.write("            map.addControl( new OpenLayers.Control.MouseDefaults());\n");
                out.write("            map.addControl( new OpenLayers.Control.MousePosition());\n");
                out.write("\n");
                out.write("            map.setCenter(new OpenLayers.LonLat(555555, 6846027), 2);\n");
                out.write("        }\n");
                out.write("    </script>\n");
                out.write("  </head>\n");
                out.write("  <body onload=\"init()\">\n");
                out.write("\n");
                out.write("        <div id=\"container\">\n");
                out.write("\n");
                out.write("            <div id=\"header\">\n");
                out.write("                <h1 id=\"logo\">\n");
                out.write("                    <span>ASP</span> MapServices\n");
                out.write("                    <small>Web mapping.  <span>EASY</span></small>\n");
                out.write("                </h1>\n");
                out.write("\n");
                out.write("                <ul id=\"menu\">\n");
                out.write("                    <li><a href=\"default.html\">Home</a></li>\n");
                out.write("                    <li><a href=\"demo_world.jsp\">Demonstration</a></li>\n");
                out.write("                    <li style=\"border-right: none;\"><a href=\"contact.html\">Contact</a></li>\n");
                out.write("                </ul>\n");
                out.write("            </div>\n");
                out.write("\n");
                out.write("            <div id=\"body\">\n");
                out.write("                <ul id=\"maps-menu\">\n");
                out.write("                    <li><a href=\"demo_world.jsp\">World</a></li>\n");
                out.write("                    <li><a href=\"demo_sweden_rt90.jsp\">Sweden RT90</a></li>\n");
                out.write("                    <li><a href=\"demo_sweden_sweref99.jsp\">Sweden SWEREF99</a></li>\n");
                out.write("                </ul>\n");
                out.write("\n");
                out.write("                <div id=\"swedenMap\" style=\"height:600px\"></div>\n");
                out.write("            </div>\n");
                out.write("        </div>\n");
                out.write("    </body>\n");
                out.write("\n");
                out.write("\n");
                out.write("  </head>\n");
                out.write("\n");
                out.write("</html>");
            }
            out.write('\n');
            out.write('\n');
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) out.clearBuffer();
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
    public Configuration(URL url) {
        InputStream in = null;
        try {
            load(in = url.openStream());
        } catch (Exception e) {
            throw new RuntimeException("Could not load configuration from " + url, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
    public void convert(File src, File dest) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(src));
        DcmParser p = pfact.newDcmParser(in);
        Dataset ds = fact.newDataset();
        p.setDcmHandler(ds.getDcmHandler());
        try {
            FileFormat format = p.detectFileFormat();
            if (format != FileFormat.ACRNEMA_STREAM) {
                System.out.println("\n" + src + ": not an ACRNEMA stream!");
                return;
            }
            p.parseDcmFile(format, Tags.PixelData);
            if (ds.contains(Tags.StudyInstanceUID) || ds.contains(Tags.SeriesInstanceUID) || ds.contains(Tags.SOPInstanceUID) || ds.contains(Tags.SOPClassUID)) {
                System.out.println("\n" + src + ": contains UIDs!" + " => probable already DICOM - do not convert");
                return;
            }
            boolean hasPixelData = p.getReadTag() == Tags.PixelData;
            boolean inflate = hasPixelData && ds.getInt(Tags.BitsAllocated, 0) == 12;
            int pxlen = p.getReadLength();
            if (hasPixelData) {
                if (inflate) {
                    ds.putUS(Tags.BitsAllocated, 16);
                    pxlen = pxlen * 4 / 3;
                }
                if (pxlen != (ds.getInt(Tags.BitsAllocated, 0) >>> 3) * ds.getInt(Tags.Rows, 0) * ds.getInt(Tags.Columns, 0) * ds.getInt(Tags.NumberOfFrames, 1) * ds.getInt(Tags.NumberOfSamples, 1)) {
                    System.out.println("\n" + src + ": mismatch pixel data length!" + " => do not convert");
                    return;
                }
            }
            ds.putUI(Tags.StudyInstanceUID, uid(studyUID));
            ds.putUI(Tags.SeriesInstanceUID, uid(seriesUID));
            ds.putUI(Tags.SOPInstanceUID, uid(instUID));
            ds.putUI(Tags.SOPClassUID, classUID);
            if (!ds.contains(Tags.NumberOfSamples)) {
                ds.putUS(Tags.NumberOfSamples, 1);
            }
            if (!ds.contains(Tags.PhotometricInterpretation)) {
                ds.putCS(Tags.PhotometricInterpretation, "MONOCHROME2");
            }
            if (fmi) {
                ds.setFileMetaInfo(fact.newFileMetaInfo(ds, UIDs.ImplicitVRLittleEndian));
            }
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
            try {
            } finally {
                ds.writeFile(out, encodeParam());
                if (hasPixelData) {
                    if (!skipGroupLen) {
                        out.write(PXDATA_GROUPLEN);
                        int grlen = pxlen + 8;
                        out.write((byte) grlen);
                        out.write((byte) (grlen >> 8));
                        out.write((byte) (grlen >> 16));
                        out.write((byte) (grlen >> 24));
                    }
                    out.write(PXDATA_TAG);
                    out.write((byte) pxlen);
                    out.write((byte) (pxlen >> 8));
                    out.write((byte) (pxlen >> 16));
                    out.write((byte) (pxlen >> 24));
                }
                if (inflate) {
                    int b2, b3;
                    for (; pxlen > 0; pxlen -= 3) {
                        out.write(in.read());
                        b2 = in.read();
                        b3 = in.read();
                        out.write(b2 & 0x0f);
                        out.write(b2 >> 4 | ((b3 & 0x0f) << 4));
                        out.write(b3 >> 4);
                    }
                } else {
                    for (; pxlen > 0; --pxlen) {
                        out.write(in.read());
                    }
                }
                out.close();
            }
            System.out.print('.');
        } finally {
            in.close();
        }
    }
    public static String getRefCatastral(String pURL) {
        String result = new String();
        String iniPC1 = "<pc1>";
        String iniPC2 = "<pc2>";
        String finPC1 = "</pc1>";
        String finPC2 = "</pc2>";
        String iniCuerr = "<cuerr>";
        String finCuerr = "</cuerr>";
        String iniDesErr = "<des>";
        String finDesErr = "</des>";
        boolean error = false;
        int ini, fin;
        try {
            URL url = new URL(pURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.contains(iniCuerr)) {
                    ini = str.indexOf(iniCuerr) + iniCuerr.length();
                    fin = str.indexOf(finCuerr);
                    if (Integer.parseInt(str.substring(ini, fin)) > 0) error = true;
                }
                if (error) {
                    if (str.contains(iniDesErr)) {
                        ini = str.indexOf(iniDesErr) + iniDesErr.length();
                        fin = str.indexOf(finDesErr);
                        throw (new Exception(str.substring(ini, fin)));
                    }
                } else {
                    if (str.contains(iniPC1)) {
                        ini = str.indexOf(iniPC1) + iniPC1.length();
                        fin = str.indexOf(finPC1);
                        result = str.substring(ini, fin);
                    }
                    if (str.contains(iniPC2)) {
                        ini = str.indexOf(iniPC2) + iniPC2.length();
                        fin = str.indexOf(finPC2);
                        result = result.concat(str.substring(ini, fin));
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return result;
    }
    public static Coordinate getCoordenadas(String RCoURL) {
        Coordinate coord = new Coordinate();
        String pURL;
        String iniPC1 = "<pc1>";
        String iniPC2 = "<pc2>";
        String finPC1 = "</pc1>";
        String finPC2 = "</pc2>";
        String iniX = "<xcen>";
        String iniY = "<ycen>";
        String finX = "</xcen>";
        String finY = "</ycen>";
        String iniCuerr = "<cuerr>";
        String finCuerr = "</cuerr>";
        String iniDesErr = "<des>";
        String finDesErr = "</des>";
        boolean error = false;
        int ini, fin;
        if (RCoURL.contains("/") || RCoURL.contains("\\") || RCoURL.contains(".")) pURL = RCoURL; else {
            if (RCoURL.length() > 14) pURL = baseURL[1].replace("<RC>", RCoURL.substring(0, 14)); else pURL = baseURL[1].replace("<RC>", RCoURL);
        }
        try {
            URL url = new URL(pURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.contains(iniCuerr)) {
                    ini = str.indexOf(iniCuerr) + iniCuerr.length();
                    fin = str.indexOf(finCuerr);
                    if (Integer.parseInt(str.substring(ini, fin)) > 0) error = true;
                }
                if (error) {
                    if (str.contains(iniDesErr)) {
                        ini = str.indexOf(iniDesErr) + iniDesErr.length();
                        fin = str.indexOf(finDesErr);
                        throw (new Exception(str.substring(ini, fin)));
                    }
                } else {
                    if (str.contains(iniPC1)) {
                        ini = str.indexOf(iniPC1) + iniPC1.length();
                        fin = str.indexOf(finPC1);
                        coord.setDescription(str.substring(ini, fin));
                    }
                    if (str.contains(iniPC2)) {
                        ini = str.indexOf(iniPC2) + iniPC2.length();
                        fin = str.indexOf(finPC2);
                        coord.setDescription(coord.getDescription().concat(str.substring(ini, fin)));
                    }
                    if (str.contains(iniX)) {
                        ini = str.indexOf(iniX) + iniX.length();
                        fin = str.indexOf(finX);
                        coord.setLongitude(Double.parseDouble(str.substring(ini, fin)));
                    }
                    if (str.contains(iniY)) {
                        ini = str.indexOf(iniY) + iniY.length();
                        fin = str.indexOf(finY);
                        coord.setLatitude(Double.parseDouble(str.substring(ini, fin)));
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return coord;
    }
    public static String getURLContent(String urlStr) throws MalformedURLException, IOException {
        URL url = new URL(urlStr);
        log.info("url: " + url);
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer buf = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            buf.append(inputLine);
        }
        in.close();
        return buf.toString();
    }
    public void elimina(Cliente cli) throws errorSQL, errorConexionBD {
        System.out.println("GestorCliente.elimina()");
        int id = cli.getId();
        String sql;
        Statement stmt = null;
        try {
            gd.begin();
            sql = "DELETE FROM cliente WHERE cod_cliente =" + id;
            System.out.println("Ejecutando: " + sql);
            stmt = gd.getConexion().createStatement();
            stmt.executeUpdate(sql);
            sql = "DELETE FROM usuario WHERE cod_usuario =" + id;
            System.out.println("Ejecutando: " + sql);
            stmt = gd.getConexion().createStatement();
            stmt.executeUpdate(sql);
            System.out.println("executeUpdate");
            sql = "DELETE FROM persona WHERE id =" + id;
            System.out.println("Ejecutando: " + sql);
            stmt = gd.getConexion().createStatement();
            stmt.executeUpdate(sql);
            gd.commit();
            System.out.println("commit");
            stmt.close();
        } catch (SQLException e) {
            gd.rollback();
            throw new errorSQL(e.toString());
        } catch (errorConexionBD e) {
            System.err.println("Error en GestorCliente.elimina(): " + e);
        } catch (errorSQL e) {
            System.err.println("Error en GestorCliente.elimina(): " + e);
        }
    }
    public String httpToStringStupid(String url) throws IllegalStateException, IOException, HttpException, InterruptedException, URISyntaxException {
        String pageDump = null;
        getParams().setParameter(ClientPNames.COOKIE_POLICY, org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY);
        getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, getPreferenceService().getSearchSocketTimeout());
        HttpGet httpget = new HttpGet(url);
        httpget.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, getPreferenceService().getSearchSocketTimeout());
        HttpResponse response = execute(httpget);
        HttpEntity entity = response.getEntity();
        pageDump = IOUtils.toString(entity.getContent(), "UTF-8");
        return pageDump;
    }
    public int extract() throws Exception {
        int count = 0;
        if (VERBOSE) System.out.println("IAAE:Extractr.extract: getting ready to extract " + getArtDir().toString());
        ITCFileFilter iff = new ITCFileFilter();
        RecursiveFileIterator rfi = new RecursiveFileIterator(getArtDir(), iff);
        FileTypeDeterminer ftd = new FileTypeDeterminer();
        File artFile = null;
        File targetFile = null;
        broadcastStart();
        while (rfi.hasMoreElements()) {
            artFile = (File) rfi.nextElement();
            targetFile = getTargetFile(artFile);
            if (VERBOSE) System.out.println("IAAE:Extractr.extract: working ont " + artFile.toString());
            BufferedInputStream in = null;
            BufferedOutputStream out = null;
            try {
                in = new BufferedInputStream((new FileInputStream(artFile)));
                out = new BufferedOutputStream((new FileOutputStream(targetFile)));
                byte[] buffer = new byte[10240];
                int read = 0;
                int total = 0;
                read = in.read(buffer);
                while (read != -1) {
                    if ((total <= 491) && (read > 491)) {
                        out.write(buffer, 492, (read - 492));
                    } else if ((total <= 491) && (read <= 491)) {
                    } else {
                        out.write(buffer, 0, read);
                    }
                    total = total + read;
                    read = in.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                broadcastFail();
            } finally {
                in.close();
                out.close();
            }
            broadcastSuccess();
            count++;
        }
        broadcastDone();
        return count;
    }
    private String hash(String text) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(text.getBytes());
        BigInteger hash = new BigInteger(1, md5.digest());
        return hash.toString(16);
    }
    public static String createPseudoUUID() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(new UID().toString().getBytes());
            try {
                String localHost = InetAddress.getLocalHost().toString();
                messageDigest.update(localHost.getBytes());
            } catch (UnknownHostException e) {
                throw new OXFException(e);
            }
            byte[] digestBytes = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            sb.append(toHexString(NumberUtils.readIntBigEndian(digestBytes, 0)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 4)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 6)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 8)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 10)));
            sb.append(toHexString(NumberUtils.readIntBigEndian(digestBytes, 12)));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new OXFException(e);
        }
    }
    public static void main(final String... args) {
        int returnCode = 0;
        if (args.length == 0) {
            System.err.println("Usage: JWGet url...");
            returnCode++;
        }
        final byte[] buf = new byte[8192];
        for (final String arg : args) {
            try {
                final URL url = new URL(arg);
                OutputStream out = null;
                InputStream in = null;
                try {
                    final URLConnection con = url.openConnection();
                    in = con.getInputStream();
                    final String location = con.getHeaderField("Content-Location");
                    final String outputFilename = new File((location != null ? new URL(url, location) : url).getFile()).getName();
                    System.err.println(outputFilename);
                    out = new FileOutputStream(outputFilename);
                    for (int bytesRead; (bytesRead = in.read(buf)) != -1; out.write(buf, 0, bytesRead)) ;
                } catch (final IOException e) {
                    System.err.println(e);
                    returnCode++;
                } finally {
                    try {
                        in.close();
                    } catch (final Exception ignore) {
                    }
                    try {
                        out.close();
                    } catch (final Exception ignore) {
                    }
                }
            } catch (final MalformedURLException e) {
                System.err.println(e);
                returnCode++;
            }
        }
        System.exit(returnCode);
    }
    public static String hash(String text) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"), 0, text.length());
            byte[] md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ISource writeTo(ISource output) throws ResourceException {
        try {
            Document doc = getParent().getDocument();
            Nodes places = doc.query(getPosition().getXpath());
            if (places.size() == 0) {
                places = doc.query("//html");
            }
            if (places.size() > 0 && places.get(0) instanceof Element) {
                Element target = (Element) places.get(0);
                List<URL> urls = getResourceURLs();
                if (getType() == EType.TEXT) {
                    Element tag = getHeaderTag();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    UtilIO.writeAllTo(urls, out);
                    String content = out.toString();
                    out.close();
                    tag.appendChild(content);
                    if (getPosition().getPlace() == EPlace.START) {
                        target.insertChild(tag, 0);
                    } else {
                        target.appendChild(tag);
                    }
                } else {
                    for (URL url : urls) {
                        String file = url.toString();
                        String name = file.substring(file.lastIndexOf("/") + 1) + "_res_" + (serialNumber++);
                        Element tag = getHeaderTag(output, name);
                        File resFile = getFile(output, name);
                        if (!resFile.getParentFile().exists()) {
                            if (!resFile.getParentFile().mkdirs()) {
                                throw new ResourceException("Could not create resource directory '" + resFile.getParent() + "'.");
                            }
                        }
                        UtilIO.writeToClose(url.openStream(), new FileOutputStream(resFile));
                        if (getPosition().getPlace() == EPlace.START) {
                            target.insertChild(tag, 0);
                        } else {
                            target.appendChild(tag);
                        }
                    }
                }
            } else {
                throw new ResourceException("Head element not found.");
            }
        } catch (IOException e) {
            throw new ResourceException(e);
        } catch (SourceException e) {
            throw new ResourceException(e);
        }
        return output;
    }
    public static Image readImage(URL url, ImageMimeType type, int page) throws IOException {
        if (type.javaNativeSupport()) {
            return ImageIO.read(url.openStream());
        } else if ((type.equals(ImageMimeType.DJVU)) || (type.equals(ImageMimeType.VNDDJVU)) || (type.equals(ImageMimeType.XDJVU))) {
            com.lizardtech.djvu.Document doc = new com.lizardtech.djvu.Document(url);
            doc.setAsync(false);
            DjVuPage[] p = new DjVuPage[1];
            int size = doc.size();
            if ((page != 0) && (page >= size)) {
                page = 0;
            }
            p[0] = doc.getPage(page, 1, true);
            p[0].setAsync(false);
            DjVuImage djvuImage = new DjVuImage(p, true);
            Rectangle pageBounds = djvuImage.getPageBounds(0);
            Image[] images = djvuImage.getImage(new JPanel(), new Rectangle(pageBounds.width, pageBounds.height));
            if (images.length == 1) {
                Image img = images[0];
                return img;
            } else return null;
        } else if (type.equals(ImageMimeType.PDF)) {
            PDDocument document = null;
            try {
                document = PDDocument.load(url.openStream());
                int resolution = 96;
                List<?> pages = document.getDocumentCatalog().getAllPages();
                PDPage pdPage = (PDPage) pages.get(page);
                BufferedImage image = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, resolution);
                return image;
            } finally {
                if (document != null) {
                    document.close();
                }
            }
        } else throw new IllegalArgumentException("unsupported mimetype '" + type.getValue() + "'");
    }
    public static void writeFullImageToStream(Image scaledImage, String javaFormat, OutputStream os) throws IOException {
        BufferedImage bufImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_BYTE_BINARY);
        Graphics gr = bufImage.getGraphics();
        gr.drawImage(scaledImage, 0, 0, null);
        gr.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufImage, javaFormat, bos);
        IOUtils.copyStreams(new ByteArrayInputStream(bos.toByteArray()), os);
    }
    private static void grab(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        boolean f = false;
        while ((inputLine = in.readLine()) != null) {
            inputLine = inputLine.trim();
            if (inputLine.startsWith("<tbody>")) {
                f = true;
                continue;
            }
            if (inputLine.startsWith("</table>")) {
                f = false;
                continue;
            }
            if (f) {
                sb.append(inputLine);
                sb.append("\n");
            }
        }
        process(sb.toString());
    }
    public static void main(String[] args) throws Exception {
        String uri = args[0];
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path inputPath = new Path(uri);
        CompressionCodecFactory factory = new CompressionCodecFactory(conf);
        CompressionCodec codec = factory.getCodec(inputPath);
        if (codec == null) {
            System.err.println("No codec found for " + uri);
            System.exit(1);
        }
        String outputUri = CompressionCodecFactory.removeSuffix(uri, codec.getDefaultExtension());
        InputStream in = null;
        OutputStream out = null;
        try {
            in = codec.createInputStream(fs.open(inputPath));
            out = fs.create(new Path(outputUri));
            IOUtils.copyBytes(in, out, conf);
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
    }
    public MapInfo loadLocalMapData(String fileName) {
        MapInfo info = mapCacheLocal.get(fileName);
        if (info != null && info.getContent() == null) {
            try {
                BufferedReader bufferedreader;
                URL fetchUrl = new URL(localMapContextUrl, fileName);
                URLConnection urlconnection = fetchUrl.openConnection();
                if (urlconnection.getContentEncoding() != null) {
                    bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), urlconnection.getContentEncoding()));
                } else {
                    bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "utf-8"));
                }
                String line;
                StringBuilder mapContent = new StringBuilder();
                while ((line = bufferedreader.readLine()) != null) {
                    mapContent.append(line);
                    mapContent.append("\n");
                }
                info.setContent(mapContent.toString());
                GameMapImplementation gameMap = GameMapImplementation.createFromMapInfo(info);
            } catch (IOException _ex) {
                System.err.println("HexTD::readFile:: Can't read from " + fileName);
            }
        } else {
            System.err.println("HexTD::readFile:: file not in cache: " + fileName);
        }
        return info;
    }
    private InputStream getInputStream(final String pUrlStr) throws IOException {
        URL url;
        int responseCode;
        String encoding;
        url = new URL(pUrlStr);
        myActiveConnection = (HttpURLConnection) url.openConnection();
        myActiveConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        responseCode = myActiveConnection.getResponseCode();
        if (responseCode != RESPONSECODE_OK) {
            String message;
            String apiErrorMessage;
            apiErrorMessage = myActiveConnection.getHeaderField("Error");
            if (apiErrorMessage != null) {
                message = "Received API HTTP response code " + responseCode + " with message \"" + apiErrorMessage + "\" for URL \"" + pUrlStr + "\".";
            } else {
                message = "Received API HTTP response code " + responseCode + " for URL \"" + pUrlStr + "\".";
            }
            throw new OsmosisRuntimeException(message);
        }
        myActiveConnection.setConnectTimeout(TIMEOUT);
        encoding = myActiveConnection.getContentEncoding();
        responseStream = myActiveConnection.getInputStream();
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            responseStream = new GZIPInputStream(responseStream);
        } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
            responseStream = new InflaterInputStream(responseStream, new Inflater(true));
        }
        return responseStream;
    }
    protected void download(URL url, File destination, long beginRange, long endRange, long totalFileSize, boolean appendToFile) throws DownloadException {
        System.out.println(" DOWNLOAD REQUEST RECEIVED " + url.toString() + " \n\tbeginRange : " + beginRange + " - EndRange " + endRange + " \n\t to -> " + destination.getAbsolutePath());
        try {
            if (destination.exists() && !appendToFile) {
                destination.delete();
            }
            if (!destination.exists()) destination.createNewFile();
            GetMethod get = new GetMethod(url.toString());
            HttpClient httpClient = new HttpClient();
            Header rangeHeader = new Header();
            rangeHeader.setName("Range");
            rangeHeader.setValue("bytes=" + beginRange + "-" + endRange);
            get.setRequestHeader(rangeHeader);
            httpClient.executeMethod(get);
            int statusCode = get.getStatusCode();
            if (statusCode >= 400 && statusCode < 500) throw new DownloadException("The file does not exist in this location : message from server ->  " + statusCode + " " + get.getStatusText());
            InputStream input = get.getResponseBodyAsStream();
            OutputStream output = new FileOutputStream(destination, appendToFile);
            try {
                int length = IOUtils.copy(input, output);
                System.out.println(" Length : " + length);
            } finally {
                input.close();
                output.flush();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to figure out the length of the file from the URL : " + e.getMessage());
            throw new DownloadException("Unable to figure out the length of the file from the URL : " + e.getMessage());
        }
    }
    protected boolean check(String username, String password, String realm, String nonce, String nc, String cnonce, String qop, String uri, String response, HttpServletRequest request) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(username.getBytes());
            md.update((byte) ':');
            md.update(realm.getBytes());
            md.update((byte) ':');
            md.update(password.getBytes());
            byte[] ha1 = md.digest();
            md.reset();
            md.update(request.getMethod().getBytes());
            md.update((byte) ':');
            md.update(uri.getBytes());
            byte[] ha2 = md.digest();
            md.update(TypeUtil.toString(ha1, 16).getBytes());
            md.update((byte) ':');
            md.update(nonce.getBytes());
            md.update((byte) ':');
            md.update(nc.getBytes());
            md.update((byte) ':');
            md.update(cnonce.getBytes());
            md.update((byte) ':');
            md.update(qop.getBytes());
            md.update((byte) ':');
            md.update(TypeUtil.toString(ha2, 16).getBytes());
            byte[] digest = md.digest();
            return response.equals(encode(digest));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean copyFile(final File src, final File dst) {
        boolean result = false;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        synchronized (FileUtil.DATA_LOCK) {
            try {
                inChannel = new FileInputStream(src).getChannel();
                outChannel = new FileOutputStream(dst).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                result = true;
            } catch (IOException e) {
            } finally {
                if (inChannel != null && inChannel.isOpen()) {
                    try {
                        inChannel.close();
                    } catch (IOException e) {
                    }
                }
                if (outChannel != null && outChannel.isOpen()) {
                    try {
                        outChannel.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return result;
    }
    public static Image getPluginImage(Object plugin, String name) {
        try {
            try {
                URL url = getPluginImageURL(plugin, name);
                if (m_URLImageMap.containsKey(url)) return m_URLImageMap.get(url);
                InputStream is = url.openStream();
                Image image;
                try {
                    image = getImage(is);
                    m_URLImageMap.put(url, image);
                } finally {
                    is.close();
                }
                return image;
            } catch (Throwable e) {
            }
        } catch (Throwable e) {
        }
        return null;
    }
    private void loadDDL() throws IOException {
        try {
            conn.createStatement().executeQuery("SELECT * FROM overrides").close();
        } catch (SQLException e) {
            Statement stmt = null;
            if (!e.getMessage().matches(ERR_MISSING_TABLE)) {
                LOG.trace(SQL_ERROR, e);
                LOG.fatal(e);
                throw new IOException("Error on initial data store read", e);
            }
            String[] qry = { "CREATE TABLE overrides (id INT NOT NULL, title VARCHAR(255) NOT NULL, subtitle VARCHAR(255) NOT NULL, PRIMARY KEY(id))", "CREATE TABLE settings (var VARCHAR(32) NOT NULL, val VARCHAR(255) NOT NULL, PRIMARY KEY(var))", "INSERT INTO settings (var, val) VALUES ('schema', '1')" };
            try {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                for (String q : qry) stmt.executeUpdate(q);
                conn.commit();
            } catch (SQLException e2) {
                try {
                    conn.rollback();
                } catch (SQLException e3) {
                    LOG.trace(SQL_ERROR, e3);
                    LOG.error(e3);
                }
                LOG.trace(SQL_ERROR, e2);
                throw new IOException("Error initializing data store", e2);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e4) {
                        LOG.trace(SQL_ERROR, e4);
                        LOG.error(e4);
                        throw new IOException("Unable to cleanup data store resources", e4);
                    }
                }
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e3) {
                    LOG.trace(SQL_ERROR, e3);
                    LOG.error(e3);
                    throw new IOException("Unable to reset data store auto commit", e3);
                }
            }
        }
        return;
    }
    private void upgradeSchema() throws IOException {
        Statement stmt = null;
        try {
            int i = getSchema();
            if (i < SCHEMA_VERSION) {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                while (i < SCHEMA_VERSION) {
                    String qry;
                    switch(i) {
                        case 1:
                            qry = "CREATE TABLE log (id INTEGER PRIMARY KEY, context VARCHAR(16) NOT NULL, level VARCHAR(16) NOT NULL, time LONG INT NOT NULL, msg LONG VARCHAR NOT NULL, parent INT)";
                            stmt.executeUpdate(qry);
                            qry = "UPDATE settings SET val = '2' WHERE var = 'schema'";
                            stmt.executeUpdate(qry);
                            break;
                        case 2:
                            qry = "CREATE TABLE monitor (id INTEGER PRIMARY KEY NOT NULL, status INTEGER NOT NULL)";
                            stmt.executeUpdate(qry);
                            qry = "UPDATE settings SET val = '3' WHERE var = 'schema'";
                            stmt.executeUpdate(qry);
                            break;
                        case 3:
                            qry = "CREATE TABLE favs (id INTEGER PRIMARY KEY NOT NULL)";
                            stmt.executeUpdate(qry);
                            qry = "UPDATE settings SET val = '4' WHERE var = 'schema'";
                            stmt.executeUpdate(qry);
                            break;
                        case 4:
                            qry = "DROP TABLE log";
                            stmt.executeUpdate(qry);
                            qry = "UPDATE settings SET val = '5' WHERE var = 'schema'";
                            stmt.executeUpdate(qry);
                            break;
                        case 5:
                            qry = "UPDATE settings SET val = '120000' WHERE var = 'SleepTime'";
                            stmt.executeUpdate(qry);
                            qry = "UPDATE settings set val = '6' WHERE var = 'schema'";
                            stmt.executeUpdate(qry);
                            break;
                    }
                    i++;
                }
                conn.commit();
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e2) {
                LOG.trace(SQL_ERROR, e2);
                LOG.error(e2);
            }
            LOG.trace(SQL_ERROR, e);
            LOG.fatal(e);
            throw new IOException("Error upgrading data store", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOG.trace(SQL_ERROR, e);
                throw new IOException("Unable to cleanup SQL resources", e);
            }
        }
    }
    public void run() {
        result.setValid(false);
        try {
            final HttpResponse response = client.execute(method, context);
            result.setValid(ArrayUtils.contains(validCodes, response.getStatusLine().getStatusCode()));
            result.setResult(response.getStatusLine().getStatusCode());
        } catch (final ClientProtocolException e) {
            LOGGER.error(e);
            result.setValid(false);
        } catch (final IOException e) {
            LOGGER.error(e);
            result.setValid(false);
        }
    }
    public static void main(String argv[]) {
        System.out.println("Starting URL tests");
        System.out.println("Test 1: Simple URL test");
        try {
            URL url = new URL("http", "www.fsf.org", 80, "/");
            if (!url.getProtocol().equals("http") || !url.getHost().equals("www.fsf.org") || url.getPort() != 80 || !url.getFile().equals("/")) System.out.println("FAILED: Simple URL test");
            System.out.println("URL is: " + url.toString());
            URLConnection uc = url.openConnection();
            if (uc instanceof HttpURLConnection) System.out.println("Got the expected connection type");
            HttpURLConnection hc = (HttpURLConnection) uc;
            hc.connect();
            System.out.flush();
            System.out.println("Dumping response headers");
            for (int i = 0; ; i++) {
                String key = hc.getHeaderFieldKey(i);
                if (key == null) break;
                System.out.println(key + ": " + hc.getHeaderField(i));
            }
            System.out.flush();
            System.out.println("Dumping contents");
            BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
            for (String str = br.readLine(); str != null; str = br.readLine()) {
                System.out.println(str);
            }
            System.out.flush();
            hc.disconnect();
            System.out.println("Content Type: " + hc.getContentType());
            System.out.println("Content Encoding: " + hc.getContentEncoding());
            System.out.println("Content Length: " + hc.getContentLength());
            System.out.println("Date: " + hc.getDate());
            System.out.println("Expiration: " + hc.getExpiration());
            System.out.println("Last Modified: " + hc.getLastModified());
            System.out.println("PASSED: Simple URL test");
        } catch (IOException e) {
            System.out.println("FAILED: Simple URL test: " + e);
        }
        System.out.println("Test 2: URL parsing test");
        try {
            URL url = new URL("http://www.urbanophile.com/arenn/trans/trans.html#mis");
            if (!url.toString().equals("http://www.urbanophile.com/arenn/trans/trans.html#mis")) System.out.println("FAILED: Parse URL test: " + url.toString()); else {
                System.out.println("Parsed ok: " + url.toString());
                url = new URL("http://www.foo.com:8080/#");
                if (!url.toString().equals("http://www.foo.com:8080/#")) System.out.println("FAILED: Parse URL test: " + url.toString()); else {
                    System.out.println("Parsed ok: " + url.toString());
                    url = new URL("http://www.bar.com/test:file/");
                    if (!url.toString().equals("http://www.bar.com/test:file/")) System.out.println("FAILED: Parse URL test: " + url.toString()); else {
                        System.out.println("Parsed ok: " + url.toString());
                        url = new URL("http://www.gnu.org");
                        if (!url.toString().equals("http://www.gnu.org/")) System.out.println("FAILED: Parse URL test: " + url.toString()); else {
                            System.out.println("Parsed ok: " + url.toString());
                            url = new URL("HTTP://www.fsf.org/");
                            if (!url.toString().equals("http://www.fsf.org/")) System.out.println("FAILED: Parse URL test: " + url.toString()); else {
                                System.out.println("Parsed ok: " + url.toString());
                                System.out.println("PASSED: URL parse test");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("FAILED: URL parsing test: " + e);
        }
        System.out.println("Test 3: getContent test");
        try {
            URL url = new URL("http://localhost/~arenn/services.txt");
            Object obj = url.getContent();
            System.out.println("Object type is: " + obj.getClass().getName());
            if (obj instanceof InputStream) {
                System.out.println("Got InputStream, so dumping contents");
                BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) obj));
                for (String str = br.readLine(); str != null; str = br.readLine()) System.out.println(str);
                br.close();
            } else {
                System.out.println("FAILED: Object is not an InputStream");
            }
            System.out.println("PASSED: getContent test");
        } catch (IOException e) {
            System.out.println("FAILED: getContent test: " + e);
        }
        System.out.println("URL test complete");
    }
    private static void writeUrl(String filePath, String data, String charCoding, boolean urlIsFile) throws IOException {
        int chunkLength;
        OutputStream os = null;
        try {
            if (!urlIsFile) {
                URL urlObj = new URL(filePath);
                URLConnection uc = urlObj.openConnection();
                os = uc.getOutputStream();
                if (charCoding == null) {
                    String type = uc.getContentType();
                    if (type != null) {
                        charCoding = getCharCodingFromType(type);
                    }
                }
            } else {
                File f = new File(filePath);
                os = new FileOutputStream(f);
            }
            Writer w;
            if (charCoding == null) {
                w = new OutputStreamWriter(os);
            } else {
                w = new OutputStreamWriter(os, charCoding);
            }
            w.write(data);
            w.flush();
        } finally {
            if (os != null) os.close();
        }
    }
    public static void loadPackage1(String ycCode) {
        InputStream input = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new FakeTrustManager() };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            URL url = Retriever.getPackage1Url(String.valueOf(YouthClub.getMiniModel().getBasics().getTeamId()), ycCode);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
            uc.setHostnameVerifier(new FakeHostnameVerifier());
            uc.setConnectTimeout(CONNECTION_TIMEOUT);
            uc.setReadTimeout(CONNECTION_TIMEOUT);
            input = uc.getInputStream();
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = input.read()) != -1) {
                sb.append((char) c);
            }
            Document doc = YouthClub.getMiniModel().getXMLParser().parseString(sb.toString());
            String target = System.getProperty("user.home") + System.getProperty("file.separator") + "youthclub_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".xml";
            YouthClub.getMiniModel().getXMLParser().writeXML(doc, target);
            Debug.log("YC XML saved to " + target);
        } catch (Exception e) {
            Debug.logException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }
    public static String getMD5(String in) {
        if (in == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(in.getBytes());
            byte[] hash = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            Debug.logException(e);
        }
        return null;
    }
    public static void test(String args[]) {
        int trace;
        int bytes_read = 0;
        int last_contentLenght = 0;
        try {
            BufferedReader reader;
            URL url;
            url = new URL(args[0]);
            URLConnection istream = url.openConnection();
            last_contentLenght = istream.getContentLength();
            reader = new BufferedReader(new InputStreamReader(istream.getInputStream()));
            System.out.println(url.toString());
            String line;
            trace = t2pNewTrace();
            while ((line = reader.readLine()) != null) {
                bytes_read = bytes_read + line.length() + 1;
                t2pProcessLine(trace, line);
            }
            t2pHandleEventPairs(trace);
            t2pSort(trace, 0);
            t2pExportTrace(trace, new String("pngtest2.png"), 1000, 700, (float) 0, (float) 33);
            t2pExportTrace(trace, new String("pngtest3.png"), 1000, 700, (float) 2.3, (float) 2.44);
            System.out.println("Press any key to contiune read from stream !!!");
            System.out.println(t2pGetProcessName(trace, 0));
            System.in.read();
            istream = url.openConnection();
            if (last_contentLenght != istream.getContentLength()) {
                istream = url.openConnection();
                istream.setRequestProperty("Range", "bytes=" + Integer.toString(bytes_read) + "-");
                System.out.println(Integer.toString(istream.getContentLength()));
                reader = new BufferedReader(new InputStreamReader(istream.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    t2pProcessLine(trace, line);
                }
            } else System.out.println("File not changed !");
            t2pDeleteTrace(trace);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException !!!");
        } catch (IOException e) {
            System.out.println("File not found " + args[0]);
        }
        ;
    }
    private void saveURL(URL url, String filename) throws IOException {
        URLConnection connection = url.openConnection();
        connection.connect();
        InputStreamReader ReadIn = new InputStreamReader(connection.getInputStream());
        BufferedReader BufData = new BufferedReader(ReadIn);
        FileWriter FWriter = new FileWriter(filename);
        BufferedWriter BWriter = new BufferedWriter(FWriter);
        String urlData = null;
        while ((urlData = BufData.readLine()) != null) {
            BWriter.write(urlData);
            BWriter.newLine();
        }
        BWriter.close();
    }
    public synchronized void run() {
        logger.info("SEARCH STARTED");
        JSONObject json = null;
        logger.info("Opening urlConnection");
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            connection.addRequestProperty("Referer", HTTP_REFERER);
        } catch (IOException e) {
            logger.warn("PROBLEM CONTACTING GOOGLE");
            e.printStackTrace();
        }
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            logger.warn("PROBLEM RETREIVING DATA FROM GOOGLE");
            e.printStackTrace();
        }
        logger.info("Google RET: " + builder.toString());
        try {
            json = new JSONObject(builder.toString());
            json.append("query", q);
        } catch (JSONException e) {
            logger.warn("PROBLEM RETREIVING DATA FROM GOOGLE");
            e.printStackTrace();
        }
        sc.onSearchFinished(json);
    }
    private String doMessageDigestAndBase64Encoding(String sequence) throws SeguidException {
        if (sequence == null) {
            throw new NullPointerException("You must give a non null sequence");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            sequence = sequence.trim().toUpperCase();
            messageDigest.update(sequence.getBytes());
            byte[] digest = messageDigest.digest();
            String seguid = Base64.encodeBytes(digest);
            seguid = seguid.replace("=", "");
            if (log.isTraceEnabled()) {
                log.trace("SEGUID " + seguid);
            }
            return seguid;
        } catch (NoSuchAlgorithmException e) {
            throw new SeguidException("Exception thrown when calculating Seguid for " + sequence, e.getCause());
        }
    }
    public static void copyFile(String inputFile, String outputFile) throws IOException {
        FileInputStream fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(outputFile);
        for (int b = fis.read(); b != -1; b = fis.read()) fos.write(b);
        fos.close();
        fis.close();
    }
    public Atividade insertAtividade(Atividade atividade) throws SQLException {
        Connection conn = null;
        String insert = "insert into Atividade (idatividade, requerente_idrequerente, datacriacao, datatermino, valor, tipoatividade, descricao, fase_idfase, estado) " + "values " + "(nextval('seq_atividade'), " + atividade.getRequerente().getIdRequerente() + ", " + "'" + atividade.getDataCriacao() + "', '" + atividade.getDataTermino() + "', '" + atividade.getValor() + "', '" + atividade.getTipoAtividade().getIdTipoAtividade() + "', '" + atividade.getDescricao() + "', " + atividade.getFaseIdFase() + ", " + atividade.getEstado() + ")";
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            if (result == 1) {
                String sqlSelect = "select last_value from seq_atividade";
                ResultSet rs = stmt.executeQuery(sqlSelect);
                while (rs.next()) {
                    atividade.setIdAtividade(rs.getInt("last_value"));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
        return null;
    }
    public void candidatarAtividade(Atividade atividade) throws SQLException {
        Connection conn = null;
        String insert = "insert into Atividade_has_recurso_humano " + "(atividade_idatividade, usuario_idusuario, ativo) " + "values " + "(" + atividade.getIdAtividade() + ", " + "" + atividade.getRecursoHumano().getIdUsuario() + ", " + "'false')";
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    public void desistirCandidatura(Atividade atividade) throws SQLException {
        Connection conn = null;
        String insert = "delete from Atividade_has_recurso_humano where atividade_idatividade=" + atividade.getIdAtividade() + " and usuario_idusuario=" + atividade.getRecursoHumano().getIdUsuario();
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    public void aprovarCandidato(Atividade atividade) throws SQLException {
        Connection conn = null;
        String insert = "update Atividade_has_recurso_humano set ativo='true' " + "where atividade_idatividade=" + atividade.getIdAtividade() + " and " + " usuario_idusuario=" + atividade.getRecursoHumano().getIdUsuario();
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    public void testResponseTimeout() throws Exception {
        server.enqueue(new MockResponse().setBody("ABC").clearHeaders().addHeader("Content-Length: 4"));
        server.enqueue(new MockResponse().setBody("DEF"));
        server.play();
        URLConnection urlConnection = server.getUrl("/").openConnection();
        urlConnection.setReadTimeout(1000);
        InputStream in = urlConnection.getInputStream();
        assertEquals('A', in.read());
        assertEquals('B', in.read());
        assertEquals('C', in.read());
        try {
            in.read();
            fail();
        } catch (SocketTimeoutException expected) {
        }
        URLConnection urlConnection2 = server.getUrl("/").openConnection();
        InputStream in2 = urlConnection2.getInputStream();
        assertEquals('D', in2.read());
        assertEquals('E', in2.read());
        assertEquals('F', in2.read());
        assertEquals(-1, in2.read());
        assertEquals(0, server.takeRequest().getSequenceNumber());
        assertEquals(0, server.takeRequest().getSequenceNumber());
    }
    public void testPreparedStatement0009() throws Exception {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("create table #t0009 " + "  (i  integer  not null,      " + "   s  char(10) not null)      ");
        con.setAutoCommit(false);
        PreparedStatement pstmt = con.prepareStatement("insert into #t0009 values (?, ?)");
        int rowsToAdd = 8;
        final String theString = "abcdefghijklmnopqrstuvwxyz";
        int count = 0;
        for (int i = 1; i <= rowsToAdd; i++) {
            pstmt.setInt(1, i);
            pstmt.setString(2, theString.substring(0, i));
            count += pstmt.executeUpdate();
        }
        pstmt.close();
        assertEquals(count, rowsToAdd);
        con.rollback();
        ResultSet rs = stmt.executeQuery("select s, i from #t0009");
        assertNotNull(rs);
        count = 0;
        while (rs.next()) {
            count++;
            assertEquals(rs.getString(1).trim().length(), rs.getInt(2));
        }
        assertEquals(count, 0);
        con.commit();
        pstmt = con.prepareStatement("insert into #t0009 values (?, ?)");
        rowsToAdd = 6;
        count = 0;
        for (int i = 1; i <= rowsToAdd; i++) {
            pstmt.setInt(1, i);
            pstmt.setString(2, theString.substring(0, i));
            count += pstmt.executeUpdate();
        }
        assertEquals(count, rowsToAdd);
        con.commit();
        pstmt.close();
        rs = stmt.executeQuery("select s, i from #t0009");
        count = 0;
        while (rs.next()) {
            count++;
            assertEquals(rs.getString(1).trim().length(), rs.getInt(2));
        }
        assertEquals(count, rowsToAdd);
        con.commit();
        stmt.close();
        con.setAutoCommit(true);
    }
    public void testTransactions0010() throws Exception {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("create table #t0010 " + "  (i  integer  not null,      " + "   s  char(10) not null)      ");
        con.setAutoCommit(false);
        PreparedStatement pstmt = con.prepareStatement("insert into #t0010 values (?, ?)");
        int rowsToAdd = 8;
        final String theString = "abcdefghijklmnopqrstuvwxyz";
        int count = 0;
        for (int i = 1; i <= rowsToAdd; i++) {
            pstmt.setInt(1, i);
            pstmt.setString(2, theString.substring(0, i));
            count += pstmt.executeUpdate();
        }
        assertEquals(count, rowsToAdd);
        con.rollback();
        ResultSet rs = stmt.executeQuery("select s, i from #t0010");
        assertNotNull(rs);
        count = 0;
        while (rs.next()) {
            count++;
            assertEquals(rs.getString(1).trim().length(), rs.getInt(2));
        }
        assertEquals(count, 0);
        rowsToAdd = 6;
        for (int j = 1; j <= 2; j++) {
            count = 0;
            for (int i = 1; i <= rowsToAdd; i++) {
                pstmt.setInt(1, i + ((j - 1) * rowsToAdd));
                pstmt.setString(2, theString.substring(0, i));
                count += pstmt.executeUpdate();
            }
            assertEquals(count, rowsToAdd);
            con.commit();
        }
        rs = stmt.executeQuery("select s, i from #t0010");
        count = 0;
        while (rs.next()) {
            count++;
            int i = rs.getInt(2);
            if (i > rowsToAdd) {
                i -= rowsToAdd;
            }
            assertEquals(rs.getString(1).trim().length(), i);
        }
        assertEquals(count, (2 * rowsToAdd));
        stmt.close();
        pstmt.close();
        con.setAutoCommit(true);
    }
}