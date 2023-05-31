    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        PrintWriter writer = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            writer = response.getWriter();
        } catch (IOException ex) {
            log(OctetStreamReader.class.getName() + "has thrown an exception: " + ex.getMessage());
        }
        String filename = request.getHeader("X-File-Name");
        try {
            is = request.getInputStream();
            fos = new FileOutputStream(new File(targetPath + filename));
            IOUtils.copy(is, fos);
            response.setStatus(HttpServletResponse.SC_underscoreOK);
            writer.print("{success: true}");
        } catch (FileNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_underscoreINTERNAL_underscoreSERVER_underscoreERROR);
            writer.print("{success: false}");
            log(OctetStreamReader.class.getName() + "has thrown an exception: " + ex.getMessage());
        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_underscoreINTERNAL_underscoreSERVER_underscoreERROR);
            writer.print("{success: false}");
            log(OctetStreamReader.class.getName() + "has thrown an exception: " + ex.getMessage());
        } finally {
            try {
                fos.close();
                is.close();
            } catch (IOException ignored) {
            }
        }
        writer.flush();
        writer.close();
    }

