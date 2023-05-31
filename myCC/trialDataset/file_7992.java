    public void save(UploadedFile file, Long student, Long activity) {
        File destiny = new File(fileFolder, student + "_underscore" + activity + "_underscore" + file.getFileName());
        try {
            IOUtils.copy(file.getFile(), new FileOutputStream(destiny));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao copiar o arquivo.", e);
        }
    }

