    private byte[] odszyfrujKlucz(byte[] kluczSesyjny, int rozmiarKlucza) {
        byte[] odszyfrowanyKlucz = null;
        byte[] kluczTymczasowy = null;
        try {
            MessageDigest skrot = MessageDigest.getInstance("SHA-1");
            skrot.update(haslo.getBytes());
            byte[] skrotHasla = skrot.digest();
            Object kluczDoKlucza = MARS_underscoreAlgorithm.makeKey(skrotHasla);
            byte[] tekst = null;
            kluczTymczasowy = new byte[rozmiarKlucza];
            int liczbaBlokow = rozmiarKlucza / ROZMIAR_underscoreBLOKU;
            for (int i = 0; i < liczbaBlokow; i++) {
                tekst = MARS_underscoreAlgorithm.blockDecrypt(kluczSesyjny, i * ROZMIAR_underscoreBLOKU, kluczDoKlucza);
                System.arraycopy(tekst, 0, kluczTymczasowy, i * ROZMIAR_underscoreBLOKU, tekst.length);
            }
            odszyfrowanyKlucz = new byte[dlugoscKlucza];
            System.arraycopy(kluczTymczasowy, 0, odszyfrowanyKlucz, 0, dlugoscKlucza);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SzyfrowaniePliku.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return odszyfrowanyKlucz;
    }

