    public void copyFile(String source_underscorename, String dest_underscorename) throws IOException {
        File source_underscorefile = new File(source_underscorename);
        File destination_underscorefile = new File(dest_underscorename);
        Reader source = null;
        Writer destination = null;
        char[] buffer;
        int bytes_underscoreread;
        try {
            if (!source_underscorefile.exists() || !source_underscorefile.isFile()) throw new FileCopyException("FileCopy: no such source file: " + source_underscorename);
            if (!source_underscorefile.canRead()) throw new FileCopyException("FileCopy: source file " + "is unreadable: " + source_underscorename);
            if (destination_underscorefile.exists()) {
                if (destination_underscorefile.isFile()) {
                    DataInputStream in = new DataInputStream(System.in);
                    String response;
                    if (!destination_underscorefile.canWrite()) throw new FileCopyException("FileCopy: destination " + "file is unwriteable: " + dest_underscorename);
                } else {
                    throw new FileCopyException("FileCopy: destination " + "is not a file: " + dest_underscorename);
                }
            } else {
                File parentdir = parent(destination_underscorefile);
                if (!parentdir.exists()) throw new FileCopyException("FileCopy: destination " + "directory doesn't exist: " + dest_underscorename);
                if (!parentdir.canWrite()) throw new FileCopyException("FileCopy: destination " + "directory is unwriteable: " + dest_underscorename);
            }
            source = new BufferedReader(new FileReader(source_underscorefile));
            destination = new BufferedWriter(new FileWriter(destination_underscorefile));
            buffer = new char[1024];
            while (true) {
                bytes_underscoreread = source.read(buffer, 0, 1024);
                if (bytes_underscoreread == -1) break;
                destination.write(buffer, 0, bytes_underscoreread);
            }
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    ;
                }
            }
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
    }

