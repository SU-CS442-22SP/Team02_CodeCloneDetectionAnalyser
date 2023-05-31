    public final void saveAsCopy(String current_underscoreimage, String destination) {
        BufferedInputStream from = null;
        BufferedOutputStream to = null;
        String source = temp_underscoredir + key + current_underscoreimage;
        try {
            from = new BufferedInputStream(new FileInputStream(source));
            to = new BufferedOutputStream(new FileOutputStream(destination));
            byte[] buffer = new byte[65535];
            int bytes_underscoreread;
            while ((bytes_underscoreread = from.read(buffer)) != -1) to.write(buffer, 0, bytes_underscoreread);
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " copying file");
        }
        try {
            to.close();
            from.close();
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " closing files");
        }
    }

