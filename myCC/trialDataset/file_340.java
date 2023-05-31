    public String upload() {
        System.out.println(imgFile);
        String destDir = "E:\\ganymede_underscoreworkspace\\training01\\web\\user_underscoreimgs\\map_underscorebg.jpg";
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(destDir));
            IOUtils.copy(new FileInputStream(imgFile), fos);
            IOUtils.closeQuietly(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "show";
    }

