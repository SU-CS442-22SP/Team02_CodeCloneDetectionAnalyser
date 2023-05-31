    @Override
    @RemoteMethod
    public boolean decrypt(int idAnexo) {
        try {
            Anexo anexo = anexoService.selectById(idAnexo);
            aes.init(Cipher.DECRYPT_underscoreMODE, aeskeySpec);
            CipherInputStream cis = new CipherInputStream(new FileInputStream(config.baseDir + "/arquivos_underscoreupload_underscoredireto/encrypt/" + anexo.getAnexoCaminho()), aes);
            FileOutputStream fos = new FileOutputStream(config.baseDir + "/arquivos_underscoreupload_underscoredireto/decrypt/" + anexo.getAnexoCaminho());
            IOUtils.copy(cis, fos);
            cis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

