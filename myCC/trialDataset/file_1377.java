    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        ServletContext ctx = getServletContext();
        RequestDispatcher rd = ctx.getRequestDispatcher(SETUP_underscoreJSP);
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
            session.setAttribute(ERROR_underscoreTAG, "You need to have run the Sniffer before running " + "the Grinder. Go to <a href=\"/index.jsp\">the start page</a> " + " to run the Sniffer.");
            rd = ctx.getRequestDispatcher(ERROR_underscoreJSP);
        } else {
            session.setMaxInactiveInterval(-1);
            String pValue = request.getParameter(ACTION_underscoreTAG);
            if (pValue != null && pValue.equals(START_underscoreTAG)) {
                rd = ctx.getRequestDispatcher(WAIT_underscoreJSP);
                int p = 1;
                int t = 1;
                int c = 1;
                try {
                    p = Integer.parseInt(request.getParameter("procs"));
                    p = p > MAX_underscorePROCS ? MAX_underscorePROCS : p;
                } catch (NumberFormatException e) {
                }
                try {
                    t = Integer.parseInt(request.getParameter("threads"));
                    t = t > MAX_underscoreTHREADS ? MAX_underscoreTHREADS : t;
                } catch (NumberFormatException e) {
                }
                try {
                    c = Integer.parseInt(request.getParameter("cycles"));
                    c = c > MAX_underscoreCYCLES ? MAX_underscoreCYCLES : c;
                } catch (NumberFormatException e) {
                }
                try {
                    String dirname = (String) session.getAttribute(OUTPUT_underscoreTAG);
                    File workdir = new File(dirname);
                    (new File(dirname + File.separator + LOG_underscoreDIR)).mkdir();
                    FileInputStream gpin = new FileInputStream(GPROPS);
                    FileOutputStream gpout = new FileOutputStream(dirname + File.separator + GPROPS);
                    copyBytes(gpin, gpout);
                    gpin.close();
                    InitialContext ictx = new InitialContext();
                    Boolean isSecure = (Boolean) session.getAttribute(SECURE_underscoreTAG);
                    if (isSecure.booleanValue()) {
                        gpout.write(("grinder.plugin=" + "net.grinder.plugin.http.HttpsPlugin" + "\n").getBytes());
                        String certificate = (String) ictx.lookup(CERTIFICATE);
                        String password = (String) ictx.lookup(PASSWORD);
                        gpout.write(("grinder.plugin.parameter.clientCert=" + certificate + "\n").getBytes());
                        gpout.write(("grinder.plugin.parameter.clientCertPassword=" + password + "\n").getBytes());
                    } else {
                        gpout.write(("grinder.plugin=" + "net.grinder.plugin.http.HttpPlugin\n").getBytes());
                    }
                    gpout.write(("grinder.processes=" + p + "\n").getBytes());
                    gpout.write(("grinder.threads=" + t + "\n").getBytes());
                    gpout.write(("grinder.cycles=" + c + "\n").getBytes());
                    gpin = new FileInputStream(dirname + File.separator + SNIFFOUT);
                    copyBytes(gpin, gpout);
                    gpin.close();
                    gpout.close();
                    String classpath = (String) ictx.lookup(CLASSPATH);
                    String cmd[] = new String[JAVA_underscorePROCESS.length + 1 + GRINDER_underscorePROCESS.length];
                    int i = 0;
                    int n = JAVA_underscorePROCESS.length;
                    System.arraycopy(JAVA_underscorePROCESS, 0, cmd, i, n);
                    cmd[n] = classpath;
                    i = n + 1;
                    n = GRINDER_underscorePROCESS.length;
                    System.arraycopy(GRINDER_underscorePROCESS, 0, cmd, i, n);
                    for (int j = 0; j < cmd.length; ++j) {
                        System.out.print(cmd[j] + " ");
                    }
                    Process proc = Runtime.getRuntime().exec(cmd, null, workdir);
                    session.setAttribute(PROCESS_underscoreTAG, proc);
                } catch (NamingException e) {
                    e.printStackTrace();
                    session.setAttribute(ERROR_underscoreMSG_underscoreTAG, e.toString());
                    session.setMaxInactiveInterval(TIMEOUT);
                    rd = ctx.getRequestDispatcher(ERROR_underscoreJSP);
                } catch (Throwable e) {
                    e.printStackTrace(response.getWriter());
                    throw new IOException(e.getMessage());
                }
            } else if (pValue != null && pValue.equals(CHECK_underscoreTAG)) {
                boolean finished = true;
                try {
                    Process p = (Process) session.getAttribute(PROCESS_underscoreTAG);
                    int result = p.exitValue();
                } catch (IllegalThreadStateException e) {
                    finished = false;
                }
                if (finished) {
                    session.setMaxInactiveInterval(TIMEOUT);
                    rd = ctx.getRequestDispatcher(RESULTS_underscoreJSP);
                } else {
                    rd = ctx.getRequestDispatcher(WAIT_underscoreJSP);
                }
            }
            try {
                rd.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace(response.getWriter());
                throw new IOException(e.getMessage());
            }
        }
    }

