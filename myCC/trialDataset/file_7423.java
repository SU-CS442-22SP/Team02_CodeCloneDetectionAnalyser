    public static boolean Copy(String _underscore_underscorefrom, String _underscore_underscoreto) {
        try {
            int bytesum = 0;
            int byteread = -1;
            java.io.File oldfile = new java.io.File(_underscore_underscorefrom);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(_underscore_underscorefrom);
                FileOutputStream fs = new FileOutputStream(_underscore_underscoreto);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("processFile.copyFile()���Ƶ����ļ��������� " + e.getMessage());
            return false;
        }
        return true;
    }

