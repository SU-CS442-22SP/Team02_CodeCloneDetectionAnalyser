    public static void copy(File from_underscorefile, File to_underscorefile) throws IOException {
        if (!from_underscorefile.exists()) {
            throw new IOException("FileCopy: no such source file: " + from_underscorefile.getPath());
        }
        if (!from_underscorefile.isFile()) {
            throw new IOException("FileCopy: can't copy directory: " + from_underscorefile.getPath());
        }
        if (!from_underscorefile.canRead()) {
            throw new IOException("FileCopy: source file is unreadable: " + from_underscorefile.getPath());
        }
        if (to_underscorefile.isDirectory()) {
            to_underscorefile = new File(to_underscorefile, from_underscorefile.getName());
        }
        if (to_underscorefile.exists()) {
            if (!to_underscorefile.canWrite()) {
                throw new IOException("FileCopy: destination file is unwriteable: " + to_underscorefile.getPath());
            }
            int choice = JOptionPane.showConfirmDialog(null, "Overwrite existing file " + to_underscorefile.getPath(), "File Exists", JOptionPane.YES_underscoreNO_underscoreOPTION, JOptionPane.QUESTION_underscoreMESSAGE);
            if (choice != JOptionPane.YES_underscoreOPTION) {
                throw new IOException("FileCopy: existing file was not overwritten.");
            }
        } else {
            String parent = to_underscorefile.getParent();
            if (parent == null) {
                parent = Globals.getDefaultPath();
            }
            File dir = new File(parent);
            if (!dir.exists()) {
                throw new IOException("FileCopy: destination directory doesn't exist: " + parent);
            }
            if (dir.isFile()) {
                throw new IOException("FileCopy: destination is not a directory: " + parent);
            }
            if (!dir.canWrite()) {
                throw new IOException("FileCopy: destination directory is unwriteable: " + parent);
            }
        }
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(from_underscorefile);
            to = new FileOutputStream(to_underscorefile);
            byte[] buffer = new byte[4096];
            int bytes_underscoreread;
            while ((bytes_underscoreread = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytes_underscoreread);
            }
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException e) {
                }
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException e) {
                }
            }
        }
    }

