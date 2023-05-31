    public static boolean copyFile(File from, File to, byte[] buf) {
        if (buf == null) buf = new byte[BUFFER_underscoreSIZE];
        FileInputStream from_underscores = null;
        FileOutputStream to_underscores = null;
        try {
            from_underscores = new FileInputStream(from);
            to_underscores = new FileOutputStream(to);
            for (int bytesRead = from_underscores.read(buf); bytesRead != -1; bytesRead = from_underscores.read(buf)) to_underscores.write(buf, 0, bytesRead);
            from_underscores.close();
            from_underscores = null;
            to_underscores.getFD().sync();
            to_underscores.close();
            to_underscores = null;
        } catch (IOException ioe) {
            return false;
        } finally {
            if (from_underscores != null) {
                try {
                    from_underscores.close();
                } catch (IOException ioe) {
                }
            }
            if (to_underscores != null) {
                try {
                    to_underscores.close();
                } catch (IOException ioe) {
                }
            }
        }
        return true;
    }

