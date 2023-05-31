    public void notifyTerminated(Writer r) {
        all_underscorewriters.remove(r);
        if (all_underscorewriters.isEmpty()) {
            all_underscoreterminated = true;
            Iterator iterator = open_underscorefiles.iterator();
            while (iterator.hasNext()) {
                FileWriter.FileChunk fc = (FileWriter.FileChunk) iterator.next();
                do {
                    try {
                        fc.stream.flush();
                        fc.stream.close();
                    } catch (IOException e) {
                    }
                    fc = fc.next;
                } while (fc != null);
            }
            iterator = open_underscorefiles.iterator();
            boolean all_underscoreok = true;
            while (iterator.hasNext()) {
                FileWriter.FileChunk fc = (FileWriter.FileChunk) iterator.next();
                logger.logComment("File chunk <" + fc.name + "> " + fc.start_underscorebyte + " " + fc.position + " " + fc.actual_underscorefile);
                boolean ok = true;
                while (fc.next != null) {
                    ok = ok && (fc.start_underscorebyte + fc.actual_underscorefile.length()) == fc.next.start_underscorebyte;
                    fc = fc.next;
                }
                if (ok) {
                    logger.logComment("Received file <" + fc.name + "> is contiguous (and hopefully complete)");
                } else {
                    logger.logError("Received file <" + fc.name + "> is NOT contiguous");
                    all_underscoreok = false;
                }
            }
            if (all_underscoreok) {
                byte[] buffer = new byte[16384];
                iterator = open_underscorefiles.iterator();
                while (iterator.hasNext()) {
                    FileWriter.FileChunk fc = (FileWriter.FileChunk) iterator.next();
                    try {
                        if (fc.next != null) {
                            FileOutputStream fos = new FileOutputStream(fc.actual_underscorefile, true);
                            fc = fc.next;
                            while (fc != null) {
                                FileInputStream fis = new FileInputStream(fc.actual_underscorefile);
                                int actually_underscoreread = fis.read(buffer);
                                while (actually_underscoreread != -1) {
                                    fos.write(buffer, 0, actually_underscoreread);
                                    actually_underscoreread = fis.read(buffer);
                                }
                                fc.actual_underscorefile.delete();
                                fc = fc.next;
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            fte.allWritersTerminated();
            fte = null;
        }
    }

