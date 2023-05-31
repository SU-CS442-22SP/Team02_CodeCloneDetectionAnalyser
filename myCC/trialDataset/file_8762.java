    public static void copy(File _underscorefrom, File _underscoreto) throws IOException {
        if (_underscorefrom == null || !_underscorefrom.exists()) return;
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new FileOutputStream(_underscoreto);
            in = new FileInputStream(_underscorefrom);
            byte[] buf = new byte[2048];
            int read = in.read(buf);
            while (read > 0) {
                out.write(buf, 0, read);
                read = in.read(buf);
            }
        } catch (IOException _underscoree) {
            throw _underscoree;
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

