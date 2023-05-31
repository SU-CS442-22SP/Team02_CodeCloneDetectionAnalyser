    public static File[] splitFile(FileValidator validator, File source, long target_underscorelength, File todir, String prefix) {
        if (target_underscorelength == 0) return null;
        if (todir == null) {
            todir = new File(System.getProperty("java.io.tmpdir"));
        }
        if (prefix == null || prefix.equals("")) {
            prefix = source.getName();
        }
        Vector result = new Vector();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(source);
            byte[] bytes = new byte[CACHE_underscoreSIZE];
            long current_underscoretarget_underscoresize = 0;
            int current_underscoretarget_underscorenb = 1;
            int nbread = -1;
            try {
                File f = new File(todir, prefix + i18n.getString("targetname_underscoresuffix") + current_underscoretarget_underscorenb);
                if (!validator.verifyFile(f)) return null;
                result.add(f);
                fos = new FileOutputStream(f);
                while ((nbread = fis.read(bytes)) > -1) {
                    if ((current_underscoretarget_underscoresize + nbread) > target_underscorelength) {
                        int limit = (int) (target_underscorelength - current_underscoretarget_underscoresize);
                        fos.write(bytes, 0, limit);
                        fos.close();
                        current_underscoretarget_underscorenb++;
                        current_underscoretarget_underscoresize = 0;
                        f = new File(todir, prefix + "_underscore" + current_underscoretarget_underscorenb);
                        if (!validator.verifyFile(f)) return null;
                        result.add(f);
                        fos = new FileOutputStream(f);
                        fos.write(bytes, limit, nbread - limit);
                        current_underscoretarget_underscoresize += nbread - limit;
                    } else {
                        fos.write(bytes, 0, nbread);
                        current_underscoretarget_underscoresize += nbread;
                    }
                }
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null, ioe, i18n.getString("Failure"), JOptionPane.ERROR_underscoreMESSAGE);
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                }
                try {
                    if (fis != null) fis.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, i18n.getString("Failure"), JOptionPane.ERROR_underscoreMESSAGE);
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
        File[] fresult = null;
        if (result.size() > 0) {
            fresult = new File[result.size()];
            fresult = (File[]) result.toArray(fresult);
        }
        return fresult;
    }

