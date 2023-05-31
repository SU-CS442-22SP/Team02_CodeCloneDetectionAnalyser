    private void fileCopy(File filename) throws IOException {
        if (this.stdOut) {
            this.fileDump(filename);
            return;
        }
        File source_underscorefile = new File(spoolPath + "/" + filename);
        File destination_underscorefile = new File(copyPath + "/" + filename);
        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_underscoreread;
        try {
            if (!source_underscorefile.exists() || !source_underscorefile.isFile()) throw new FileCopyException("no such source file: " + source_underscorefile);
            if (!source_underscorefile.canRead()) throw new FileCopyException("source file is unreadable: " + source_underscorefile);
            if (destination_underscorefile.exists()) {
                if (destination_underscorefile.isFile()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    if (!destination_underscorefile.canWrite()) throw new FileCopyException("destination file is unwriteable: " + destination_underscorefile);
                    if (!this.overwrite) {
                        System.out.print("File " + destination_underscorefile + " already exists. Overwrite? (Y/N): ");
                        System.out.flush();
                        if (!in.readLine().toUpperCase().equals("Y")) throw new FileCopyException("copy cancelled.");
                    }
                } else throw new FileCopyException("destination is not a file: " + destination_underscorefile);
            } else {
                File parentdir = parent(destination_underscorefile);
                if (!parentdir.exists()) throw new FileCopyException("destination directory doesn't exist: " + destination_underscorefile);
                if (!parentdir.canWrite()) throw new FileCopyException("destination directory is unwriteable: " + destination_underscorefile);
            }
            source = new FileInputStream(source_underscorefile);
            destination = new FileOutputStream(destination_underscorefile);
            buffer = new byte[1024];
            while ((bytes_underscoreread = source.read(buffer)) != -1) {
                destination.write(buffer, 0, bytes_underscoreread);
            }
            System.out.println("File " + filename + " successfull copied to " + destination_underscorefile);
            if (this.keep == false && source_underscorefile.isFile()) {
                try {
                    source.close();
                } catch (Exception e) {
                }
                if (source_underscorefile.delete()) {
                    new File(this.spoolPath + "/info/" + filename + ".desc").delete();
                }
            }
        } finally {
            if (source != null) try {
                source.close();
            } catch (IOException e) {
            }
            if (destination != null) try {
                destination.flush();
            } catch (IOException e) {
            }
            if (destination != null) try {
                destination.close();
            } catch (IOException e) {
            }
        }
    }

