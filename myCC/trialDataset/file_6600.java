    public void copyToCurrentDir(File _underscorecopyFile, String _underscorefileName) throws IOException {
        File outputFile = new File(getCurrentPath() + File.separator + _underscorefileName);
        FileReader in;
        FileWriter out;
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        in = new FileReader(_underscorecopyFile);
        out = new FileWriter(outputFile);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
        reList();
    }

