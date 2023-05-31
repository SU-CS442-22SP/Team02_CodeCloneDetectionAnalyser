    public static void decoupe(String input_underscorefile_underscorepath) {
        final int BUFFER_underscoreSIZE = 2000000;
        try {
            FileInputStream fr = new FileInputStream(input_underscorefile_underscorepath);
            byte[] cbuf = new byte[BUFFER_underscoreSIZE];
            int n_underscoreread = 0;
            int i = 0;
            boolean bContinue = true;
            while (bContinue) {
                n_underscoreread = fr.read(cbuf, 0, BUFFER_underscoreSIZE);
                if (n_underscoreread == -1) break;
                FileOutputStream fo = new FileOutputStream("f_underscore" + ++i);
                fo.write(cbuf, 0, n_underscoreread);
                fo.close();
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

