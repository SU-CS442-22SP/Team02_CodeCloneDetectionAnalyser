    public static void main(String[] args) {
        FileDialog fd = new FileDialog(new Frame(), "Find a .def file", FileDialog.LOAD);
        boolean proceed = true;
        String input = "";
        DataInputStream dis;
        OutputStream out;
        InputStream in;
        File out_underscorefile, in_underscorefile;
        byte[] buffer = new byte[2048];
        int bytes_underscoreread;
        while (proceed) {
            fd.show();
            if (fd.getFile() != null) {
                try {
                    in_underscorefile = new File(fd.getDirectory(), fd.getFile());
                    out_underscorefile = new File(fd.getDirectory(), fd.getFile() + ".out");
                    out = new FileOutputStream(out_underscorefile);
                    in = new MSZipInputStream(new FileInputStream(in_underscorefile));
                    while ((bytes_underscoreread = in.read(buffer)) != -1) out.write(buffer, 0, bytes_underscoreread);
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Do another (y/n)?");
            dis = new DataInputStream(System.in);
            try {
                input = dis.readLine();
            } catch (Exception blah) {
            }
            if (input.toLowerCase().startsWith("n")) proceed = false;
        }
    }

