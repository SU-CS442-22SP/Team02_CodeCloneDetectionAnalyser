    public void copyFile(String source_underscorefile_underscorepath, String destination_underscorefile_underscorepath) {
        FileWriter fw = null;
        FileReader fr = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        File source = null;
        try {
            fr = new FileReader(source_underscorefile_underscorepath);
            fw = new FileWriter(destination_underscorefile_underscorepath);
            br = new BufferedReader(fr);
            bw = new BufferedWriter(fw);
            source = new File(source_underscorefile_underscorepath);
            int fileLength = (int) source.length();
            char charBuff[] = new char[fileLength];
            while (br.read(charBuff, 0, fileLength) != -1) bw.write(charBuff, 0, fileLength);
        } catch (FileNotFoundException fnfe) {
            System.out.println(source_underscorefile_underscorepath + " does not exist!");
        } catch (IOException ioe) {
            System.out.println("Error reading/writing files!");
        } finally {
            try {
                if (br != null) br.close();
                if (bw != null) bw.close();
            } catch (IOException ioe) {
            }
        }
    }

