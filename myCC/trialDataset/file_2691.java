    @Test
    public void testTrainingBackprop() throws IOException {
        File temp = File.createTempFile("fannj_underscore", ".tmp");
        temp.deleteOnExit();
        IOUtils.copy(this.getClass().getResourceAsStream("xor.data"), new FileOutputStream(temp));
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_underscoreSIGMOID_underscoreSYMMETRIC));
        layers.add(Layer.create(2, ActivationFunction.FANN_underscoreSIGMOID_underscoreSYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_underscoreSIGMOID_underscoreSYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_underscoreTRAIN_underscoreINCREMENTAL);
        float desiredError = .001f;
        float mse = trainer.train(temp.getPath(), 500000, 1000, desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }

