    public void testReaderWriterF2() throws Exception {
        String inFile = "test_underscoredata/mri.png";
        String outFile = "test_underscoreoutput/mri_underscore_underscoresmooth_underscoretestReaderWriter.mhd";
        itkImageFileReaderF2_underscorePointer reader = itkImageFileReaderF2.itkImageFileReaderF2_underscoreNew();
        itkImageFileWriterF2_underscorePointer writer = itkImageFileWriterF2.itkImageFileWriterF2_underscoreNew();
        reader.SetFileName(inFile);
        writer.SetFileName(outFile);
        writer.SetInput(reader.GetOutput());
        writer.Update();
    }

