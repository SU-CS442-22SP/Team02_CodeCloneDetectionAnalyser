    public he3Decode(String in_underscorefile) {
        try {
            File out = new File(in_underscorefile + extension);
            File in = new File(in_underscorefile);
            int file_underscoresize = (int) in.length();
            FileInputStream in_underscorestream = new FileInputStream(in_underscorefile);
            out.createNewFile();
            FileOutputStream out_underscorestream = new FileOutputStream(out.getName());
            ByteArrayOutputStream os = new ByteArrayOutputStream(file_underscoresize);
            byte byte_underscorearr[] = new byte[8];
            int buff_underscoresize = byte_underscorearr.length;
            int _underscorefetched = 0;
            int _underscorechars_underscoreread = 0;
            System.out.println(appname + ".\n" + "decoding: " + in_underscorefile + "\n" + "decoding to: " + in_underscorefile + extension + "\n" + "\nreading: ");
            while (_underscorefetched < file_underscoresize) {
                _underscorechars_underscoreread = in_underscorestream.read(byte_underscorearr, 0, buff_underscoresize);
                if (_underscorechars_underscoreread == -1) break;
                os.write(byte_underscorearr, 0, _underscorechars_underscoreread);
                _underscorefetched += _underscorechars_underscoreread;
                System.out.print("*");
            }
            System.out.print("\ndecoding: ");
            out_underscorestream.write(_underscoredecode((ByteArrayOutputStream) os));
            System.out.print("complete\n\n");
        } catch (java.io.FileNotFoundException fnfEx) {
            System.err.println("Exception: " + fnfEx.getMessage());
        } catch (java.io.IOException ioEx) {
            System.err.println("Exception: " + ioEx.getMessage());
        }
    }

