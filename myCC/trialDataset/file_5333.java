    public ZipViewdataReader(ZipInputStream stream) throws IOException {
        ZipEntry ze;
        while ((ze = stream.getNextEntry()) != null) {
            File temp = File.createTempFile("spool.", ".synu");
            temp.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(temp);
            byte[] buffer = new byte[1024 * 1024];
            int length;
            while ((length = stream.read(buffer)) != -1) fos.write(buffer, 0, length);
            fos.close();
            String name = ze.getName();
            String[] parts = name.split("[\\\\/]");
            this._underscorefile_underscorehash.put(parts[parts.length - 1], temp);
        }
        stream.close();
        for (String key : this._underscorefile_underscorehash.keySet()) if (key.endsWith("Viewdata")) {
            File f = this._underscorefile_underscorehash.get(key);
            FileReader fr = new FileReader(f);
            this._underscoreviewdata = new BufferedReader(fr);
            break;
        }
        if (this._underscoreviewdata == null) throw new FileNotFoundException("No Viewdata found in ZIP file.");
    }

