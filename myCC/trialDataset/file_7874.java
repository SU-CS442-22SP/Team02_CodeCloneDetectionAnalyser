    private void copyFile(File src_underscorefile, File dest_underscorefile) {
        InputStream src_underscorestream = null;
        OutputStream dest_underscorestream = null;
        try {
            int b;
            src_underscorestream = new BufferedInputStream(new FileInputStream(src_underscorefile));
            dest_underscorestream = new BufferedOutputStream(new FileOutputStream(dest_underscorefile));
            while ((b = src_underscorestream.read()) != -1) dest_underscorestream.write(b);
        } catch (Exception e) {
            XRepository.getLogger().warning(this, "Error on copying the plugin file!");
            XRepository.getLogger().warning(this, e);
        } finally {
            try {
                src_underscorestream.close();
                dest_underscorestream.close();
            } catch (Exception ex2) {
            }
        }
    }

