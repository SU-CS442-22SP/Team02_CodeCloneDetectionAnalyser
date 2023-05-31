    public static void _underscorehe3Decode(String in_underscorefile) {
        try {
            File out = new File(in_underscorefile + dec_underscoreextension);
            File in = new File(in_underscorefile);
            int file_underscoresize = (int) in.length();
            FileInputStream in_underscorestream = new FileInputStream(in_underscorefile);
            out.createNewFile();
            FileOutputStream out_underscorestream = new FileOutputStream(out.getName());
            InputStreamReader inputReader = new InputStreamReader(in_underscorestream, "ISO8859_underscore1");
            OutputStreamWriter outputWriter = new OutputStreamWriter(out_underscorestream, "ISO8859_underscore1");
            ByteArrayOutputStream os = new ByteArrayOutputStream(file_underscoresize);
            byte byte_underscorearr[] = new byte[8];
            char char_underscorearr[] = new char[8];
            int buff_underscoresize = char_underscorearr.length;
            int _underscorefetched = 0;
            int _underscorechars_underscoreread = 0;
            System.out.println(appname + ".\n" + dec_underscoremode + ": " + in_underscorefile + "\n" + dec_underscoremode + " to: " + in_underscorefile + dec_underscoreextension + "\n" + "\nreading: ");
            while (_underscorefetched < file_underscoresize) {
                _underscorechars_underscoreread = inputReader.read(char_underscorearr, 0, buff_underscoresize);
                if (_underscorechars_underscoreread == -1) break;
                for (int i = 0; i < _underscorechars_underscoreread; i++) byte_underscorearr[i] = (byte) char_underscorearr[i];
                os.write(byte_underscorearr, 0, _underscorechars_underscoreread);
                _underscorefetched += _underscorechars_underscoreread;
                System.out.print("*");
            }
            System.out.print("\n" + dec_underscoremode + ": ");
            outputWriter.write(new String(_underscoredecode((ByteArrayOutputStream) os), "ISO-8859-1"));
            System.out.print("complete\n\n");
        } catch (java.io.FileNotFoundException fnfEx) {
            System.err.println("Exception: " + fnfEx.getMessage());
        } catch (java.io.IOException ioEx) {
            System.err.println("Exception: " + ioEx.getMessage());
        }
    }

