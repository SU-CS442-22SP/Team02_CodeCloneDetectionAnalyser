    private int writeTraceFile(final File destination_underscorefile, final String trace_underscorefile_underscorename, final String trace_underscorefile_underscorepath) {
        URL url = null;
        BufferedInputStream is = null;
        FileOutputStream fo = null;
        BufferedOutputStream os = null;
        int b = 0;
        if (destination_underscorefile == null) {
            return 0;
        }
        try {
            url = new URL("http://" + trace_underscorefile_underscorepath + "/" + trace_underscorefile_underscorename);
            is = new BufferedInputStream(url.openStream());
            fo = new FileOutputStream(destination_underscorefile);
            os = new BufferedOutputStream(fo);
            while ((b = is.read()) != -1) {
                os.write(b);
            }
            os.flush();
            is.close();
            os.close();
        } catch (Exception e) {
            System.err.println(url.toString());
            Utilities.unexpectedException(e, this, CONTACT);
            return 0;
        }
        return 1;
    }

