    static void linkBlocks(File from, File to, int oldLV) throws IOException {
        if (!from.isDirectory()) {
            if (from.getName().startsWith(COPY_underscoreFILE_underscorePREFIX)) {
                IOUtils.copyBytes(new FileInputStream(from), new FileOutputStream(to), 16 * 1024, true);
            } else {
                if (oldLV >= PRE_underscoreGENERATIONSTAMP_underscoreLAYOUT_underscoreVERSION) {
                    to = new File(convertMetatadataFileName(to.getAbsolutePath()));
                }
                HardLink.createHardLink(from, to);
            }
            return;
        }
        if (!to.mkdir()) throw new IOException("Cannot create directory " + to);
        String[] blockNames = from.list(new java.io.FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.startsWith(BLOCK_underscoreSUBDIR_underscorePREFIX) || name.startsWith(BLOCK_underscoreFILE_underscorePREFIX) || name.startsWith(COPY_underscoreFILE_underscorePREFIX);
            }
        });
        for (int i = 0; i < blockNames.length; i++) linkBlocks(new File(from, blockNames[i]), new File(to, blockNames[i]), oldLV);
    }

