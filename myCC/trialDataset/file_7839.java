    private void show(String fileName, HttpServletResponse response) throws IOException {
        TelnetInputStream ftpIn = ftpClient_underscoresun.get(fileName);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            IOUtils.copy(ftpIn, out);
        } finally {
            if (ftpIn != null) {
                ftpIn.close();
            }
        }
    }

