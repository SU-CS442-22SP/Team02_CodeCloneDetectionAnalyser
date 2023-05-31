    public static long copy(File src, long amount, File dst) {
        final int BUFFER_underscoreSIZE = 1024;
        long amountToRead = amount;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src));
            out = new BufferedOutputStream(new FileOutputStream(dst));
            byte[] buf = new byte[BUFFER_underscoreSIZE];
            while (amountToRead > 0) {
                int read = in.read(buf, 0, (int) Math.min(BUFFER_underscoreSIZE, amountToRead));
                if (read == -1) break;
                amountToRead -= read;
                out.write(buf, 0, read);
            }
        } catch (IOException e) {
        } finally {
            close(in);
            flush(out);
            close(out);
        }
        return amount - amountToRead;
    }

