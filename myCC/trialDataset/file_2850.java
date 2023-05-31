    @Override
    public String transformSingleFile(X3DEditorSupport.X3dEditor xed) {
        Node[] node = xed.getActivatedNodes();
        X3DDataObject dob = (X3DDataObject) xed.getX3dEditorSupport().getDataObject();
        FileObject mySrc = dob.getPrimaryFile();
        File mySrcF = FileUtil.toFile(mySrc);
        File myOutF = new File(mySrcF.getParentFile(), mySrc.getName() + ".x3d.gz");
        TransformListener co = TransformListener.getInstance();
        co.message(NbBundle.getMessage(getClass(), "Gzip_underscorecompression_underscorestarting"));
        co.message(NbBundle.getMessage(getClass(), "Saving_underscoreas_underscore") + myOutF.getAbsolutePath());
        co.moveToFront();
        co.setNode(node[0]);
        try {
            FileInputStream fis = new FileInputStream(mySrcF);
            GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(myOutF));
            byte[] buf = new byte[4096];
            int ret;
            while ((ret = fis.read(buf)) > 0) gzos.write(buf, 0, ret);
            gzos.close();
        } catch (Exception ex) {
            co.message(NbBundle.getMessage(getClass(), "Exception:_underscore_underscore") + ex.getLocalizedMessage());
            return null;
        }
        co.message(NbBundle.getMessage(getClass(), "Gzip_underscorecompression_underscorecomplete"));
        return myOutF.getAbsolutePath();
    }

