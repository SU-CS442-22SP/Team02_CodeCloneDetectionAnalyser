    private boolean copyFile(File _underscorefile1, File _underscorefile2) {
        FileInputStream fis;
        FileOutputStream fos;
        try {
            fis = new FileInputStream(_underscorefile1);
            fos = new FileOutputStream(_underscorefile2);
            FileChannel canalFuente = fis.getChannel();
            canalFuente.transferTo(0, canalFuente.size(), fos.getChannel());
            fis.close();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return false;
    }

