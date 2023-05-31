    protected String getGraphPath(String name) throws ServletException {
        String hash;
        try {
            MessageDigest md = MessageDigest.getInstance(m_underscoremessagedigest_underscorealgorithm);
            md.update(name.getBytes());
            hash = bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new ServletException("NoSuchAlgorithmException while " + "attempting to hash file name: " + e);
        }
        File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
        return tempDir.getAbsolutePath() + File.separatorChar + hash;
    }

