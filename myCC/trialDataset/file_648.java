    public static int gzipFile(File file_underscoreinput, String file_underscoreoutput) {
        File gzip_underscoreoutput = new File(file_underscoreoutput);
        GZIPOutputStream gzip_underscoreout_underscorestream;
        try {
            FileOutputStream out = new FileOutputStream(gzip_underscoreoutput);
            gzip_underscoreout_underscorestream = new GZIPOutputStream(new BufferedOutputStream(out));
        } catch (IOException e) {
            return STATUS_underscoreOUT_underscoreFAIL;
        }
        byte[] input_underscorebuffer = new byte[BUF_underscoreSIZE];
        int len = 0;
        try {
            FileInputStream in = new FileInputStream(file_underscoreinput);
            BufferedInputStream source = new BufferedInputStream(in, BUF_underscoreSIZE);
            while ((len = source.read(input_underscorebuffer, 0, BUF_underscoreSIZE)) != -1) gzip_underscoreout_underscorestream.write(input_underscorebuffer, 0, len);
            in.close();
        } catch (IOException e) {
            return STATUS_underscoreGZIP_underscoreFAIL;
        }
        try {
            gzip_underscoreout_underscorestream.close();
        } catch (IOException e) {
        }
        return STATUS_underscoreOK;
    }

