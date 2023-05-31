    public void create() throws IOException {
        FileChannel fc = new FileInputStream(sourceFile).getChannel();
        for (RangeArrayElement element : array) {
            FileChannel fc_underscore = fc.position(element.starting());
            File part = new File(destinationDirectory, "_underscore0x" + Long.toHexString(element.starting()) + ".partial");
            FileChannel partfc = new FileOutputStream(part).getChannel();
            partfc.transferFrom(fc_underscore, 0, element.getSize());
            partfc.force(true);
            partfc.close();
        }
        fc.close();
    }

