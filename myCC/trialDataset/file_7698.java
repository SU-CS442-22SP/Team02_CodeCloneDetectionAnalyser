        void copy(String source_underscorename, String dest_underscorename) throws IOException {
            File source_underscorefile = new File(source_underscorename);
            File destination_underscorefile = new File(dest_underscorename);
            FileInputStream source = null;
            FileOutputStream destination = null;
            byte[] buffer;
            int bytes_underscoreread;
            try {
                if (!source_underscorefile.exists() || !source_underscorefile.isFile()) {
                    throw new FileCopyException("FileCopy: no such source file: " + source_underscorename);
                }
                if (!source_underscorefile.canRead()) {
                    throw new FileCopyException("FileCopy: source file " + "is unreadable: " + source_underscorename);
                }
                if (!destination_underscorefile.exists()) {
                    File parentdir = parent(destination_underscorefile);
                    if (!parentdir.exists()) {
                        throw new FileCopyException("FileCopy: destination " + "directory doesn't exist: " + dest_underscorename);
                    }
                    if (!parentdir.canWrite()) {
                        throw new FileCopyException("FileCopy: destination " + "directory is unwriteable: " + dest_underscorename);
                    }
                }
                source = new FileInputStream(source_underscorefile);
                destination = new FileOutputStream(destination_underscorefile);
                buffer = new byte[1024];
                while (true) {
                    bytes_underscoreread = source.read(buffer);
                    if (bytes_underscoreread == -1) {
                        break;
                    }
                    destination.write(buffer, 0, bytes_underscoreread);
                }
            } finally {
                if (source != null) {
                    try {
                        source.close();
                    } catch (IOException e) {
                    }
                }
                if (destination != null) {
                    try {
                        destination.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

