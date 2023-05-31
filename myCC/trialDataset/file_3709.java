    public void setTableBraille(String tableBraille, boolean sys) {
        fiConf.setProperty(OptNames.fi_underscorebraille_underscoretable, tableBraille);
        fiConf.setProperty(OptNames.fi_underscoreis_underscoresys_underscorebraille_underscoretable, Boolean.toString(sys));
        FileChannel in = null;
        FileChannel out = null;
        try {
            String fichTable;
            if (!(tableBraille.endsWith(".ent"))) {
                tableBraille = tableBraille + ".ent";
            }
            if (sys) {
                fichTable = ConfigNat.getInstallFolder() + "xsl/tablesBraille/" + tableBraille;
            } else {
                fichTable = ConfigNat.getUserBrailleTableFolder() + tableBraille;
            }
            in = new FileInputStream(fichTable).getChannel();
            out = new FileOutputStream(getUserBrailleTableFolder() + "Brltab.ent").getChannel();
            in.transferTo(0, in.size(), out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String fichTable;
            if (sys) {
                fichTable = ConfigNat.getInstallFolder() + "/xsl/tablesEmbosseuse/" + tableBraille;
            } else {
                fichTable = ConfigNat.getUserEmbossTableFolder() + "/" + tableBraille;
            }
            in = new FileInputStream(fichTable).getChannel();
            out = new FileOutputStream(ConfigNat.getUserTempFolder() + "Table_underscorepour_underscorechaines.ent").getChannel();
            in.transferTo(0, in.size(), out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

