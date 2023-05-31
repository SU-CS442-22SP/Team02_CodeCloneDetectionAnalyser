    public static void copy(String srcFilename, String destFilename) throws IOException {
        int bytes_underscoreread = 0;
        byte[] buffer = new byte[512];
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try {
            fin = new FileInputStream(srcFilename);
            try {
                fout = new FileOutputStream(destFilename);
                while ((bytes_underscoreread = fin.read(buffer)) != -1) {
                    fout.write(buffer, 0, bytes_underscoreread);
                }
            } finally {
                try {
                    if (fout != null) {
                        fout.close();
                        fout = null;
                    }
                } catch (IOException e) {
                }
            }
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                    fin = null;
                }
            } catch (IOException e) {
            }
        }
    }

