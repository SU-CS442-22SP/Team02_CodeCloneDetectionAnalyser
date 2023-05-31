    public static byte[] hashFile(File file) {
        long size = file.length();
        long jump = (long) (size / (float) CHUNK_underscoreSIZE);
        MessageDigest digest;
        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
            digest = MessageDigest.getInstance("SHA-256");
            if (size < CHUNK_underscoreSIZE * 4) {
                readAndUpdate(size, stream, digest);
            } else {
                if (stream.skip(jump) != jump) return null;
                readAndUpdate(CHUNK_underscoreSIZE, stream, digest);
                if (stream.skip(jump - CHUNK_underscoreSIZE) != jump - CHUNK_underscoreSIZE) return null;
                readAndUpdate(CHUNK_underscoreSIZE, stream, digest);
                if (stream.skip(jump - CHUNK_underscoreSIZE) != jump - CHUNK_underscoreSIZE) return null;
                readAndUpdate(CHUNK_underscoreSIZE, stream, digest);
                digest.update(Long.toString(size).getBytes());
            }
            return digest.digest();
        } catch (FileNotFoundException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

