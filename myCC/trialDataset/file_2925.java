    public void overwriteFileTest() throws Exception {
        File filefrom = new File("/tmp/from.txt");
        File fileto = new File("/tmp/to.txt");
        InputStream from = null;
        OutputStream to = null;
        try {
            from = new FileInputStream(filefrom);
            to = new FileOutputStream(fileto);
            byte[] buffer = new byte[4096];
            int bytes_underscoreread;
            while ((bytes_underscoreread = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytes_underscoreread);
            }
        } finally {
            if (from != null) {
                from.close();
            }
            if (to != null) {
                to.close();
            }
        }
    }

