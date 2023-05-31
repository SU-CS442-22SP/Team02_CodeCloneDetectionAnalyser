    public static void main(String[] argz) {
        int X, Y, Z;
        X = 256;
        Y = 256;
        Z = 256;
        try {
            String work_underscorefolder = "C:\\Documents and Settings\\Entheogen\\My Documents\\school\\jung\\vol_underscoredata\\CT_underscoreHEAD3";
            FileOutputStream out_underscorestream = new FileOutputStream(new File(work_underscorefolder + "\\converted.dat"));
            FileChannel out = out_underscorestream.getChannel();
            String f_underscorename = "head256.raw";
            File file = new File(work_underscorefolder + "\\" + f_underscorename);
            FileChannel in = new FileInputStream(file).getChannel();
            ByteBuffer buffa = BufferUtil.newByteBuffer((int) file.length());
            in.read(buffa);
            in.close();
            int N = 256;
            FloatBuffer output_underscoredata = BufferUtil.newFloatBuffer(N * N * N);
            float min = Float.MAX_underscoreVALUE;
            for (int i = 0, j = 0; i < buffa.capacity(); i++, j++) {
                byte c = buffa.get(i);
                min = Math.min(min, (float) (c));
                output_underscoredata.put((float) (c));
            }
            for (int i = 0; i < Y - X; ++i) {
                for (int j = 0; j < Y; ++j) {
                    for (int k = 0; k < Z; ++k) {
                        output_underscoredata.put(min);
                    }
                }
            }
            output_underscoredata.rewind();
            System.out.println("size of output_underscoredata = " + Integer.toString(output_underscoredata.capacity()));
            out.write(BufferUtil.copyFloatBufferAsByteBuffer(output_underscoredata));
            ByteBuffer buffa2 = BufferUtil.newByteBuffer(2);
            buffa2.put((byte) '.');
            out.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

