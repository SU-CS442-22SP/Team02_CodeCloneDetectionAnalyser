    public void copyFile(String source_underscorename, String dest_underscorename) throws IOException {
        File source_underscorefile = new File(source_underscorename);
        File destination_underscorefile = new File(dest_underscorename);
        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_underscoreread;
        try {
            if (!source_underscorefile.exists() || !source_underscorefile.isFile()) throw new FileCopyException(QZ.PHRASES.getPhrase("25") + " " + source_underscorename);
            if (!source_underscorefile.canRead()) throw new FileCopyException(QZ.PHRASES.getPhrase("26") + " " + QZ.PHRASES.getPhrase("27") + ": " + source_underscorename);
            if (destination_underscorefile.exists()) {
                if (destination_underscorefile.isFile()) {
                    DataInputStream in = new DataInputStream(System.in);
                    String response;
                    if (!destination_underscorefile.canWrite()) throw new FileCopyException(QZ.PHRASES.getPhrase("28") + " " + QZ.PHRASES.getPhrase("29") + ": " + dest_underscorename);
                    System.out.print(QZ.PHRASES.getPhrase("19") + dest_underscorename + QZ.PHRASES.getPhrase("30") + ": ");
                    System.out.flush();
                    response = in.readLine();
                    if (!response.equals("Y") && !response.equals("y")) throw new FileCopyException(QZ.PHRASES.getPhrase("31"));
                } else throw new FileCopyException(QZ.PHRASES.getPhrase("28") + " " + QZ.PHRASES.getPhrase("32") + ": " + dest_underscorename);
            } else {
                File parentdir = parent(destination_underscorefile);
                if (!parentdir.exists()) throw new FileCopyException(QZ.PHRASES.getPhrase("28") + " " + QZ.PHRASES.getPhrase("33") + ": " + dest_underscorename);
                if (!parentdir.canWrite()) throw new FileCopyException(QZ.PHRASES.getPhrase("28") + " " + QZ.PHRASES.getPhrase("34") + ": " + dest_underscorename);
            }
            source = new FileInputStream(source_underscorefile);
            destination = new FileOutputStream(destination_underscorefile);
            buffer = new byte[1024];
            while (true) {
                bytes_underscoreread = source.read(buffer);
                if (bytes_underscoreread == -1) break;
                destination.write(buffer, 0, bytes_underscoreread);
            }
        } finally {
            if (source != null) try {
                source.close();
            } catch (IOException e) {
                ;
            }
            if (destination != null) try {
                destination.close();
            } catch (IOException e) {
                ;
            }
        }
    }

