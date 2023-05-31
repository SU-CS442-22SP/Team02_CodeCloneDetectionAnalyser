    public static int gunzipFile(File file_underscoreinput, File dir_underscoreoutput) {
        GZIPInputStream gzip_underscorein_underscorestream;
        try {
            FileInputStream in = new FileInputStream(file_underscoreinput);
            BufferedInputStream source = new BufferedInputStream(in);
            gzip_underscorein_underscorestream = new GZIPInputStream(source);
        } catch (IOException e) {
            return STATUS_underscoreIN_underscoreFAIL;
        }
        String file_underscoreinput_underscorename = file_underscoreinput.getName();
        String file_underscoreoutput_underscorename = file_underscoreinput_underscorename.substring(0, file_underscoreinput_underscorename.length() - 3);
        File output_underscorefile = new File(dir_underscoreoutput, file_underscoreoutput_underscorename);
        byte[] input_underscorebuffer = new byte[BUF_underscoreSIZE];
        int len = 0;
        try {
            FileOutputStream out = new FileOutputStream(output_underscorefile);
            BufferedOutputStream destination = new BufferedOutputStream(out, BUF_underscoreSIZE);
            while ((len = gzip_underscorein_underscorestream.read(input_underscorebuffer, 0, BUF_underscoreSIZE)) != -1) destination.write(input_underscorebuffer, 0, len);
            destination.flush();
            out.close();
        } catch (IOException e) {
            return STATUS_underscoreGUNZIP_underscoreFAIL;
        }
        try {
            gzip_underscorein_underscorestream.close();
        } catch (IOException e) {
        }
        return STATUS_underscoreOK;
    }

