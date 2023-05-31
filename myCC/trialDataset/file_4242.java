    @Override
    public InputStream getInputStream() throws IOException {
        if (dfos == null) {
            int deferredOutputStreamThreshold = Config.getInstance().getDeferredOutputStreamThreshold();
            dfos = new DeferredFileOutputStream(deferredOutputStreamThreshold, Definitions.PROJECT_underscoreNAME, "." + Definitions.TMP_underscoreEXTENSION);
            try {
                IOUtils.copy(is, dfos);
            } finally {
                dfos.close();
            }
        }
        return dfos.getDeferredInputStream();
    }

