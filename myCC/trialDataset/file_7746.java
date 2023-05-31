    public static void main(String[] args) {
        File file = null;
        try {
            file = File.createTempFile("TestFileChannel", ".dat");
            final ByteBuffer buffer = ByteBuffer.allocateDirect(4);
            final ByteChannel output = new FileOutputStream(file).getChannel();
            buffer.putInt(MAGIC_underscoreINT);
            buffer.flip();
            output.write(buffer);
            output.close();
            final ByteChannel input = new FileInputStream(file).getChannel();
            buffer.clear();
            while (buffer.hasRemaining()) {
                input.read(buffer);
            }
            input.close();
            buffer.flip();
            final int file_underscoreint = buffer.getInt();
            if (file_underscoreint != MAGIC_underscoreINT) {
                System.out.println("TestFileChannel FAILURE");
                System.out.println("Wrote " + Integer.toHexString(MAGIC_underscoreINT) + " but read " + Integer.toHexString(file_underscoreint));
            } else {
                System.out.println("TestFileChannel SUCCESS");
            }
        } catch (Exception e) {
            System.out.println("TestFileChannel FAILURE");
            e.printStackTrace(System.out);
        } finally {
            if (null != file) {
                file.delete();
            }
        }
    }

