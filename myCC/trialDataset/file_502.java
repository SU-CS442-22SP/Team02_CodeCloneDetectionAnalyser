    private String copyImageFile(String urlString, String filePath) {
        FileOutputStream destination = null;
        File destination_underscorefile = null;
        String inLine;
        String dest_underscorename = "";
        byte[] buffer;
        int bytes_underscoreread;
        int last_underscoreoffset = 0;
        int offset = 0;
        InputStream imageFile = null;
        try {
            URL url = new URL(urlString);
            imageFile = url.openStream();
            dest_underscorename = url.getFile();
            offset = 0;
            last_underscoreoffset = 0;
            offset = dest_underscorename.indexOf('/', offset + 1);
            while (offset > -1) {
                last_underscoreoffset = offset + 1;
                offset = dest_underscorename.indexOf('/', offset + 1);
            }
            dest_underscorename = filePath + File.separator + dest_underscorename.substring(last_underscoreoffset);
            destination_underscorefile = new File(dest_underscorename);
            if (destination_underscorefile.exists()) {
                if (destination_underscorefile.isFile()) {
                    if (!destination_underscorefile.canWrite()) {
                        System.out.println("FileCopy: destination " + "file is unwriteable: " + dest_underscorename);
                    }
                    System.out.println("File " + dest_underscorename + " already exists. File will be overwritten.");
                } else {
                    System.out.println("FileCopy: destination " + "is not a file: " + dest_underscorename);
                }
            } else {
                File parentdir = parent(destination_underscorefile);
                if (!parentdir.exists()) {
                    System.out.println("FileCopy: destination " + "directory doesn't exist: " + dest_underscorename);
                }
                if (!parentdir.canWrite()) {
                    System.out.println("FileCopy: destination " + "directory is unwriteable: " + dest_underscorename);
                }
            }
            destination = new FileOutputStream(dest_underscorename);
            buffer = new byte[1024];
            while (true) {
                bytes_underscoreread = imageFile.read(buffer);
                if (bytes_underscoreread == -1) break;
                destination.write(buffer, 0, bytes_underscoreread);
            }
        } catch (MalformedURLException ex) {
            System.out.println("Bad URL " + urlString);
        } catch (IOException ex) {
            System.out.println(" IO error: " + ex.getMessage());
        } finally {
            if (imageFile != null) {
                try {
                    imageFile.close();
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
        return (dest_underscorename);
    }

