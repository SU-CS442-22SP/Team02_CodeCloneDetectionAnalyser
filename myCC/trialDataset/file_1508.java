    private void downloadFile(File file, String url) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_underscoreMOUNTED.equals(state)) {
            InputStream in = null;
            BufferedOutputStream out = null;
            try {
                in = new BufferedInputStream(new URL(url).openStream(), IO_underscoreBUFFER_underscoreSIZE);
                final FileOutputStream outStream = new FileOutputStream(file);
                out = new BufferedOutputStream(outStream, IO_underscoreBUFFER_underscoreSIZE);
                byte[] bytes = new byte[IO_underscoreBUFFER_underscoreSIZE];
                while (in.read(bytes) > 0) {
                    out.write(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

