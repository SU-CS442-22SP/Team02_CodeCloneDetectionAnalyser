    public static int zipFile(File file_underscoreinput, File dir_underscoreoutput) {
        File zip_underscoreoutput = new File(dir_underscoreoutput, file_underscoreinput.getName() + ".zip");
        ZipOutputStream zip_underscoreout_underscorestream;
        try {
            FileOutputStream out = new FileOutputStream(zip_underscoreoutput);
            zip_underscoreout_underscorestream = new ZipOutputStream(new BufferedOutputStream(out));
        } catch (IOException e) {
            return STATUS_underscoreOUT_underscoreFAIL;
        }
        byte[] input_underscorebuffer = new byte[BUF_underscoreSIZE];
        int len = 0;
        try {
            ZipEntry zip_underscoreentry = new ZipEntry(file_underscoreinput.getName());
            zip_underscoreout_underscorestream.putNextEntry(zip_underscoreentry);
            FileInputStream in = new FileInputStream(file_underscoreinput);
            BufferedInputStream source = new BufferedInputStream(in, BUF_underscoreSIZE);
            while ((len = source.read(input_underscorebuffer, 0, BUF_underscoreSIZE)) != -1) zip_underscoreout_underscorestream.write(input_underscorebuffer, 0, len);
            in.close();
        } catch (IOException e) {
            return STATUS_underscoreZIP_underscoreFAIL;
        }
        try {
            zip_underscoreout_underscorestream.close();
        } catch (IOException e) {
        }
        return STATUS_underscoreOK;
    }

