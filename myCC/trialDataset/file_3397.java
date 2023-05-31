    protected void copyAndDelete(final URL _underscoresrc, final long _underscoretemp) throws IOException {
        final File storage = getStorageFile(_underscoresrc, _underscoretemp);
        final File dest = new File(_underscoresrc.getFile());
        FileChannel in = null;
        FileChannel out = null;
        if (storage.equals(dest)) {
            return;
        }
        try {
            readWriteLock_underscore.lockWrite();
            if (dest.exists()) {
                dest.delete();
            }
            if (storage.exists() && !storage.renameTo(dest)) {
                in = new FileInputStream(storage).getChannel();
                out = new FileOutputStream(dest).getChannel();
                final long len = in.size();
                final long copied = out.transferFrom(in, 0, in.size());
                if (len != copied) {
                    throw new IOException("unable to complete write");
                }
            }
        } finally {
            readWriteLock_underscore.unlockWrite();
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException _underscoreevt) {
                FuLog.error(_underscoreevt);
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (final IOException _underscoreevt) {
                FuLog.error(_underscoreevt);
            }
            storage.delete();
        }
    }

