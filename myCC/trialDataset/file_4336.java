    public static void main(String[] args) {
        String inFile = "test_underscoredata/blobs.png";
        String outFile = "ReadWriteTest.png";
        itkImageFileReaderUC2_underscorePointer reader = itkImageFileReaderUC2.itkImageFileReaderUC2_underscoreNew();
        itkImageFileWriterUC2_underscorePointer writer = itkImageFileWriterUC2.itkImageFileWriterUC2_underscoreNew();
        reader.SetFileName(inFile);
        writer.SetFileName(outFile);
        writer.SetInput(reader.GetOutput());
        writer.Update();
    }

