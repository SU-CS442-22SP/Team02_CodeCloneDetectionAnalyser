    public void shouldBeAbleToDownloadAndUpload() throws IOException {
        OutputStream outputStream = fileSystem.createOutputStream(_underscore("hello"), OutputMode.OVERWRITE);
        outputStream.write(new byte[] { 1, 2, 3 });
        outputStream.close();
        InputStream inputStream = fileSystem.createInputStream(_underscore("hello"));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, buffer);
        inputStream.close();
        assertThat().array(buffer.toByteArray()).isEqualTo(new byte[] { 1, 2, 3 });
    }

