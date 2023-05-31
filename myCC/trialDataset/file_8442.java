    public static void copyFile(String source_underscorename, String dest_underscorename) throws IOException {
        source_underscorename = Shell.getUtils().constructPath(source_underscorename);
        File source_underscorefile = new File(source_underscorename);
        dest_underscorename = Shell.getUtils().constructPath(dest_underscorename);
        File destination_underscorefile = new File(dest_underscorename);
        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_underscoreread;
        try {
            if (!source_underscorefile.exists() || !source_underscorefile.isFile()) throw new FileCopyException("cp: no such source file: " + source_underscorename);
            if (!source_underscorefile.canRead()) throw new FileCopyException("cp: source file " + "is unreadable: " + source_underscorename);
            if (destination_underscorefile.exists()) {
                if (destination_underscorefile.isFile()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    String response;
                    if (!destination_underscorefile.canWrite()) throw new FileCopyException("cp: destination " + "file is unwriteable: " + dest_underscorename);
                    System.out.print("cp: file " + dest_underscorename + " already exists. Overwrite it ? (Y/N): ");
                    System.out.flush();
                    response = in.readLine();
                    if (!response.equals("Y") && !response.equals("y")) throw new FileCopyException("cp: copy cancelled.");
                } else throw new FileCopyException("cp: destination " + "is not a file: " + dest_underscorename);
            } else {
                File parentdir = parent(destination_underscorefile);
                if (!parentdir.exists()) throw new FileCopyException("cp: destination " + "directory doesn't exist: " + dest_underscorename);
                if (!parentdir.canWrite()) throw new FileCopyException("cp: destination " + "directory is unwriteable: " + dest_underscorename);
            }
            source = new FileInputStream(source_underscorefile);
            destination = new FileOutputStream(destination_underscorefile);
            buffer = new byte[1024];
            int size = (new Long((source_underscorefile.length() / 1024) / 50)).intValue();
            int c = 1;
            int written = 0;
            System.out.print("cp: ");
            while (true) {
                if (written < 50) {
                    if ((c - 1) == size && size != 0) {
                        System.out.print("#");
                        c = 1;
                        written++;
                    } else if (size == 0) {
                        int j = 1;
                        if (c > 1) j = (50 / c) - 50; else j = 50 / c;
                        for (int i = 0; i < j; i++) System.out.print("#");
                        written += j;
                    }
                }
                bytes_underscoreread = source.read(buffer);
                if (bytes_underscoreread == -1) break;
                destination.write(buffer, 0, bytes_underscoreread);
                c++;
            }
            System.out.println();
        } finally {
            if (source != null) try {
                source.close();
            } catch (IOException e) {
            }
            if (destination != null) try {
                destination.close();
            } catch (IOException e) {
            }
        }
    }

