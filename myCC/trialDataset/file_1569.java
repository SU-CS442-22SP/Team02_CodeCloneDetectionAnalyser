    private byte[] szyfrujKlucz(byte[] kluczSesyjny) {
        byte[] zaszyfrowanyKlucz = null;
        byte[] klucz = null;
        try {
            MessageDigest skrot = MessageDigest.getInstance("SHA-1");
            skrot.update(haslo.getBytes());
            byte[] skrotHasla = skrot.digest();
            Object kluczDoKlucza = MARS_underscoreAlgorithm.makeKey(skrotHasla);
            int resztaKlucza = this.dlugoscKlucza % ROZMIAR_underscoreBLOKU;
            if (resztaKlucza == 0) {
                klucz = kluczSesyjny;
                zaszyfrowanyKlucz = new byte[this.dlugoscKlucza];
            } else {
                int liczbaBlokow = this.dlugoscKlucza / ROZMIAR_underscoreBLOKU + 1;
                int nowyRozmiar = liczbaBlokow * ROZMIAR_underscoreBLOKU;
                zaszyfrowanyKlucz = new byte[nowyRozmiar];
                klucz = new byte[nowyRozmiar];
                byte roznica = (byte) (ROZMIAR_underscoreBLOKU - resztaKlucza);
                System.arraycopy(kluczSesyjny, 0, klucz, 0, kluczSesyjny.length);
                for (int i = kluczSesyjny.length; i < nowyRozmiar; i++) klucz[i] = (byte) roznica;
            }
            byte[] szyfrogram = null;
            int liczbaBlokow = klucz.length / ROZMIAR_underscoreBLOKU;
            int offset = 0;
            for (offset = 0; offset < liczbaBlokow; offset++) {
                szyfrogram = MARS_underscoreAlgorithm.blockEncrypt(klucz, offset * ROZMIAR_underscoreBLOKU, kluczDoKlucza);
                System.arraycopy(szyfrogram, 0, zaszyfrowanyKlucz, offset * ROZMIAR_underscoreBLOKU, szyfrogram.length);
            }
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SzyfrowaniePliku.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return zaszyfrowanyKlucz;
    }

