    public static int copy(File src, int amount, File dst) {
        final int BUFFER_underscoreSIZE = 1024;
        int amountToRead = amount;
        boolean ok = true;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src));
            out = new BufferedOutputStream(new FileOutputStream(dst));
            byte[] buf = new byte[BUFFER_underscoreSIZE];
            while (amountToRead > 0) {
                int read = in.read(buf, 0, Math.min(BUFFER_underscoreSIZE, amountToRead));
                if (read == -1) break;
                amountToRead -= read;
                out.write(buf, 0, read);
            }
        } catch (IOException e) {
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
            }
            if (out != null) {
                try {
                    out.flush();
                } catch (IOException e) {
                }
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return amount - amountToRead;
    }

