    public static boolean copyFileChannel(final File _underscorefileFrom, final File _underscorefileTo, final boolean _underscoreappend) {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = new FileInputStream(_underscorefileFrom).getChannel();
            dstChannel = new FileOutputStream(_underscorefileTo, _underscoreappend).getChannel();
            if (_underscoreappend) {
                dstChannel.transferFrom(srcChannel, dstChannel.size(), srcChannel.size());
            } else {
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            }
            srcChannel.close();
            dstChannel.close();
        } catch (final IOException e) {
            return false;
        } finally {
            try {
                if (srcChannel != null) {
                    srcChannel.close();
                }
            } catch (final IOException _underscoreevt) {
                FuLog.error(_underscoreevt);
            }
            try {
                if (dstChannel != null) {
                    dstChannel.close();
                }
            } catch (final IOException _underscoreevt) {
                FuLog.error(_underscoreevt);
            }
        }
        return true;
    }

