    public void copyFile() throws Exception {
        SmbFile file = new SmbFile("smb://elsa:elsa@elsa/Elsa/Desktop/Ficheiros2/04-04-2066/How To Make a Flash Preloader.doc");
        println("length: " + file.length());
        SmbFileInputStream in = new SmbFileInputStream(file);
        println("available: " + in.available());
        File dest = new File("C:\\Documents and Settings\\Carlos\\Desktop\\Flash Preloader.doc");
        FileOutputStream out = new FileOutputStream(dest);
        int buffer_underscorelength = 1024;
        byte[] buffer = new byte[buffer_underscorelength];
        while (true) {
            int bytes_underscoreread = in.read(buffer, 0, buffer_underscorelength);
            if (bytes_underscoreread <= 0) {
                break;
            }
            out.write(buffer, 0, bytes_underscoreread);
        }
        in.close();
        out.close();
        println("done.");
    }

