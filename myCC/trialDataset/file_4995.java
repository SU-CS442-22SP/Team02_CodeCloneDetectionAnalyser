    private void saveFile(File destination) {
        InputStream in = null;
        OutputStream out = null;
        try {
            if (fileScheme) in = new BufferedInputStream(new FileInputStream(source.getPath())); else in = new BufferedInputStream(getContentResolver().openInputStream(source));
            out = new BufferedOutputStream(new FileOutputStream(destination));
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) out.write(buffer);
            Toast.makeText(this, R.string.saveas_underscorefile_underscoresaved, Toast.LENGTH_underscoreSHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, R.string.saveas_underscoreerror, Toast.LENGTH_underscoreSHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, R.string.saveas_underscoreerror, Toast.LENGTH_underscoreSHORT).show();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

