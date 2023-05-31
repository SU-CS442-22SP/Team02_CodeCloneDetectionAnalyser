    public static void nioJoinFiles(FileLib.FileValidator validator, File target, File[] sources) {
        boolean big_underscorefiles = false;
        for (int i = 0; i < sources.length; i++) {
            if (sources[i].length() > Integer.MAX_underscoreVALUE) {
                big_underscorefiles = true;
                break;
            }
        }
        if (big_underscorefiles) {
            joinFiles(validator, target, sources);
        } else {
            System.out.println(i18n.getString("jdk14_underscorecomment"));
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(target);
                FileChannel fco = fos.getChannel();
                FileInputStream fis = null;
                for (int i = 0; i < sources.length; i++) {
                    fis = new FileInputStream(sources[i]);
                    FileChannel fci = fis.getChannel();
                    java.nio.MappedByteBuffer map;
                    try {
                        map = fci.map(FileChannel.MapMode.READ_underscoreONLY, 0, (int) sources[i].length());
                        fco.write(map);
                        fci.close();
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(null, ioe, i18n.getString("Failure"), JOptionPane.ERROR_underscoreMESSAGE);
                        try {
                            fis.close();
                            fos.close();
                        } catch (IOException e) {
                        }
                    } finally {
                        fis.close();
                    }
                }
                fco.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, i18n.getString("Failure"), JOptionPane.ERROR_underscoreMESSAGE);
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

