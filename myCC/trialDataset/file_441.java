    public static void copyFile(File file, File dest_underscorefile) throws FileNotFoundException, IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dest_underscorefile)));
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.close();
    }

