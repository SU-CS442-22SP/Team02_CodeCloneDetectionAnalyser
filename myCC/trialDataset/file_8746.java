    public static void fileCopy(String from_underscorename, String to_underscorename) throws IOException {
        File fromFile = new File(from_underscorename);
        File toFile = new File(to_underscorename);
        if (fromFile.equals(toFile)) abort("cannot copy on itself: " + from_underscorename);
        if (!fromFile.exists()) abort("no such currentSourcepartName file: " + from_underscorename);
        if (!fromFile.isFile()) abort("can't copy directory: " + from_underscorename);
        if (!fromFile.canRead()) abort("currentSourcepartName file is unreadable: " + from_underscorename);
        if (toFile.isDirectory()) toFile = new File(toFile, fromFile.getName());
        if (toFile.exists()) {
            if (!toFile.canWrite()) abort("destination file is unwriteable: " + to_underscorename);
        } else {
            String parent = toFile.getParent();
            if (parent == null) abort("destination directory doesn't exist: " + parent);
            File dir = new File(parent);
            if (!dir.exists()) abort("destination directory doesn't exist: " + parent);
            if (dir.isFile()) abort("destination is not a directory: " + parent);
            if (!dir.canWrite()) abort("destination directory is unwriteable: " + parent);
        }
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
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

