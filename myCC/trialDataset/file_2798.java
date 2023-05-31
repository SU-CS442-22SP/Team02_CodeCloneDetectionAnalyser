    public void testReaderWriterUC2() throws Exception {
        String inFile = "test_underscoredata/mri.png";
        String outFile = "test_underscoreoutput/mri_underscore_underscoresmooth_underscoretestReaderWriter.png";
        itkImageFileReaderUC2_underscorePointer reader = itkImageFileReaderUC2.itkImageFileReaderUC2_underscoreNew();
        itkImageFileWriterUC2_underscorePointer writer = itkImageFileWriterUC2.itkImageFileWriterUC2_underscoreNew();
        reader.SetFileName(inFile);
        writer.SetFileName(outFile);
        writer.SetInput(reader.GetOutput());
        writer.Update();
    }

