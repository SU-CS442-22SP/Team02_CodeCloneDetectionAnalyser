    public static void copyFile(String f_underscorein, String f_underscoreout, boolean remove) throws FileNotFoundException, IOException {
        if (remove) {
            PogoString readcode = new PogoString(PogoUtil.readFile(f_underscorein));
            readcode = PogoUtil.removeLogMessages(readcode);
            PogoUtil.writeFile(f_underscoreout, readcode.str);
        } else {
            FileInputStream fid = new FileInputStream(f_underscorein);
            FileOutputStream fidout = new FileOutputStream(f_underscoreout);
            int nb = fid.available();
            byte[] inStr = new byte[nb];
            if (fid.read(inStr) > 0) fidout.write(inStr);
            fid.close();
            fidout.close();
        }
    }

