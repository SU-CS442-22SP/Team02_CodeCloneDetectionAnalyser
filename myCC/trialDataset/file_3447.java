    public static void copyFromOffset(long offset, File exe, File cab) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(exe));
        FileOutputStream out = new FileOutputStream(cab);
        byte[] buffer = new byte[4096];
        int bytes_underscoreread;
        in.skipBytes((int) offset);
        while ((bytes_underscoreread = in.read(buffer)) != -1) out.write(buffer, 0, bytes_underscoreread);
        in.close();
        out.close();
        in = null;
        out = null;
    }

