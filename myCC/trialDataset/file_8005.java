    public static void copyFile(File fromFile, File toFile) throws IOException {
        FileReader from = new FileReader(fromFile);
        FileWriter to = new FileWriter(toFile);
        char[] buffer = new char[4096];
        int bytes_underscoreread;
        while ((bytes_underscoreread = from.read(buffer)) != -1) {
            to.write(buffer, 0, bytes_underscoreread);
        }
        to.flush();
        to.close();
        from.close();
    }

