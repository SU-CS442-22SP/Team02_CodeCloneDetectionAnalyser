    @Override
    protected svm_underscoremodel loadModel(InputStream inputStream) throws IOException {
        File tmpFile = File.createTempFile("tmp", ".mdl");
        FileOutputStream output = new FileOutputStream(tmpFile);
        try {
            IOUtils.copy(inputStream, output);
            return libsvm.svm.svm_underscoreload_underscoremodel(tmpFile.getPath());
        } finally {
            output.close();
            tmpFile.delete();
        }
    }

