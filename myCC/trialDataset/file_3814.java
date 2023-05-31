    public static void copyFile(String original, String destination) throws Exception {
        File original_underscorefile = new File(original);
        File destination_underscorefile = new File(destination);
        if (!original_underscorefile.exists()) throw new Exception("File with path " + original + " does not exist.");
        if (destination_underscorefile.exists()) throw new Exception("File with path " + destination + " already exists.");
        FileReader in = new FileReader(original_underscorefile);
        FileWriter out = new FileWriter(destination_underscorefile);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }

