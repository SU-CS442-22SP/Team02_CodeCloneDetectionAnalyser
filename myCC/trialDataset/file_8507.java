    public static boolean copyFile(File source, File dest) throws IOException {
        int answer = JOptionPane.YES_underscoreOPTION;
        if (dest.exists()) {
            answer = JOptionPane.showConfirmDialog(null, "File " + dest.getAbsolutePath() + "\n already exists.  Overwrite?", "Warning", JOptionPane.YES_underscoreNO_underscoreOPTION);
        }
        if (answer == JOptionPane.NO_underscoreOPTION) return false;
        dest.createNewFile();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

