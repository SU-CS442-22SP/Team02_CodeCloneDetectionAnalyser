    public static int unzipFile(File file_underscoreinput, File dir_underscoreoutput) {
        ZipInputStream zip_underscorein_underscorestream;
        try {
            FileInputStream in = new FileInputStream(file_underscoreinput);
            BufferedInputStream source = new BufferedInputStream(in);
            zip_underscorein_underscorestream = new ZipInputStream(source);
        } catch (IOException e) {
            return STATUS_underscoreIN_underscoreFAIL;
        }
        byte[] input_underscorebuffer = new byte[BUF_underscoreSIZE];
        int len = 0;
        do {
            try {
                ZipEntry zip_underscoreentry = zip_underscorein_underscorestream.getNextEntry();
                if (zip_underscoreentry == null) break;
                File output_underscorefile = new File(dir_underscoreoutput, zip_underscoreentry.getName());
                FileOutputStream out = new FileOutputStream(output_underscorefile);
                BufferedOutputStream destination = new BufferedOutputStream(out, BUF_underscoreSIZE);
                while ((len = zip_underscorein_underscorestream.read(input_underscorebuffer, 0, BUF_underscoreSIZE)) != -1) destination.write(input_underscorebuffer, 0, len);
                destination.flush();
                out.close();
            } catch (IOException e) {
                return STATUS_underscoreGUNZIP_underscoreFAIL;
            }
        } while (true);
        try {
            zip_underscorein_underscorestream.close();
        } catch (IOException e) {
        }
        return STATUS_underscoreOK;
    }

