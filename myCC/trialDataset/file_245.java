    public static void copy(String from_underscorename, String to_underscorename) throws IOException {
        File from_underscorefile = new File(from_underscorename);
        File to_underscorefile = new File(to_underscorename);
        if (!from_underscorefile.exists()) abort("FileCopy: no such source file: " + from_underscorename);
        if (!from_underscorefile.isFile()) abort("FileCopy: can't copy directory: " + from_underscorename);
        if (!from_underscorefile.canRead()) abort("FileCopy: source file is unreadable: " + from_underscorename);
        if (to_underscorefile.isDirectory()) to_underscorefile = new File(to_underscorefile, from_underscorefile.getName());
        String parent = to_underscorefile.getParent();
        if (parent == null) parent = System.getProperty("user.dir");
        File dir = new File(parent);
        if (!dir.exists()) abort("FileCopy: destination directory doesn't exist: " + parent);
        if (dir.isFile()) abort("FileCopy: destination is not a directory: " + parent);
        if (!dir.canWrite()) abort("FileCopy: destination directory is unwriteable: " + parent);
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(from_underscorefile);
            to = new FileOutputStream(to_underscorefile);
            byte[] buffer = new byte[4096];
            int bytes_underscoreread;
            while ((bytes_underscoreread = from.read(buffer)) != -1) to.write(buffer, 0, bytes_underscoreread);
        } finally {
            if (from != null) try {
                from.close();
            } catch (IOException e) {
                ;
            }
            if (to != null) try {
                to.close();
            } catch (IOException e) {
                ;
            }
        }
    }

