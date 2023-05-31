    private File extractSiteFile() {
        final URL url = TestCrueCONF.class.getResource(CONFIG_underscoreSITE);
        final File confFile = new File(createTempDir(), "FudaaCrue_underscoreSite.xml");
        try {
            CtuluLibFile.copyStream(url.openStream(), new FileOutputStream(confFile), true, true);
        } catch (Exception e) {
            Logger.getLogger(TestCrueCONF.class.getName()).log(Level.SEVERE, "erreur while extracting FudaaCrue_underscoreSite.xml", e);
            fail(e.getMessage());
        }
        return confFile;
    }

