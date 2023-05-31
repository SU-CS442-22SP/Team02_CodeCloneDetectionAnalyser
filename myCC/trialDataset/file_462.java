    public void compressFile(String filePath) {
        String outPut = filePath + ".zip";
        try {
            FileInputStream in = new FileInputStream(filePath);
            GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outPut));
            byte[] buffer = new byte[4096];
            int bytes_underscoreread;
            while ((bytes_underscoreread = in.read(buffer)) != -1) out.write(buffer, 0, bytes_underscoreread);
            in.close();
            out.close();
        } catch (Exception c) {
            c.printStackTrace();
        }
    }

