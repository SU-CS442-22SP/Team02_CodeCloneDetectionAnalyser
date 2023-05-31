    public static void concatFiles(final String as_underscorebase_underscorefile_underscorename) throws IOException, FileNotFoundException {
        new File(as_underscorebase_underscorefile_underscorename).createNewFile();
        final OutputStream lo_underscoreout = new FileOutputStream(as_underscorebase_underscorefile_underscorename, true);
        int ln_underscorepart = 1, ln_underscorereaded = -1;
        final byte[] lh_underscorebuffer = new byte[32768];
        File lo_underscorefile = new File(as_underscorebase_underscorefile_underscorename + "part1");
        while (lo_underscorefile.exists() && lo_underscorefile.isFile()) {
            final InputStream lo_underscoreinput = new FileInputStream(lo_underscorefile);
            while ((ln_underscorereaded = lo_underscoreinput.read(lh_underscorebuffer)) != -1) {
                lo_underscoreout.write(lh_underscorebuffer, 0, ln_underscorereaded);
            }
            ln_underscorepart++;
            lo_underscorefile = new File(as_underscorebase_underscorefile_underscorename + "part" + ln_underscorepart);
        }
        lo_underscoreout.flush();
        lo_underscoreout.close();
    }

