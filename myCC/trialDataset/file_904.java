    public void doActionxxx() {
        try {
            System.out.println("app: ggc");
            String server_underscorename = "http://192.168.4.3:8080/";
            server_underscorename = server_underscorename.trim();
            if (server_underscorename.length() == 0) {
                server_underscorename = "http://www.atech-software.com/";
            } else {
                if (!server_underscorename.startsWith("http://")) server_underscorename = "http://" + server_underscorename;
                if (!server_underscorename.endsWith("/")) server_underscorename = server_underscorename + "/";
            }
            URL url = new URL(server_underscorename + "ATechUpdateGetFile?" + "" + "file_underscoreid=1" + "&" + "version_underscorerequested=1");
            InputStream is = url.openStream();
            RandomAccessFile raf = new RandomAccessFile("/home/andy/test.jpg", "rw");
            ArrayList<Integer> list = new ArrayList<Integer>();
            float size = 671200;
            long current_underscoresize = 0;
            System.out.println("File size: " + is.available());
            byte[] array = new byte[1024];
            while (is.available() > 0) {
                if (is.available() < 1024) {
                    array = new byte[is.available()];
                }
                is.read(array);
                raf.write(array);
                current_underscoresize += array.length;
                System.out.println("Progress: " + ((current_underscoresize / size) * 100));
            }
            System.out.println("Size Arr: " + list.size());
            CheckSumUtility csu = new CheckSumUtility();
            System.out.println("Checksum: " + csu.getChecksumValue("/home/andy/test.jpg"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

