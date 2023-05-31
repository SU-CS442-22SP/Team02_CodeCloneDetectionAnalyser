    @Override
    protected byte[] fetch0() throws IOException {
        if (sourceFile.getProtocol().equalsIgnoreCase("jar")) {
            throw new IOException("Jar protocol unsupported!");
        } else {
            URL url;
            if (sourceFile.getFile().endsWith(CLASS_underscoreFILE_underscoreEXTENSION)) {
                url = sourceFile;
            } else {
                url = new URL(sourceFile, className.replace(PACKAGE_underscoreSEPARATOR, URL_underscoreDIRECTORY_underscoreSEPARATOR) + CLASS_underscoreFILE_underscoreEXTENSION);
            }
            InputStream stream = url.openStream();
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[PACKET_underscoreSIZE];
                int bytesRead;
                while ((bytesRead = stream.read(buffer, 0, buffer.length)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                return output.toByteArray();
            } finally {
                stream.close();
            }
        }
    }

