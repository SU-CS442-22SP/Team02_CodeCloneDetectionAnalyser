    @Test
    public void test00_underscorereinitData() throws Exception {
        Logs.logMethodName();
        init();
        Db db = DbConnection.defaultCieDbRW();
        try {
            db.begin();
            PreparedStatement pst = db.prepareStatement("TRUNCATE e_underscoremodule;");
            pst.executeUpdate();
            pst = db.prepareStatement("TRUNCATE e_underscoreapplication_underscoreversion;");
            pst.executeUpdate();
            ModuleHelper.synchronizeDbWithModuleList(db);
            ModuleHelper.declareNewVersion(db);
            ModuleHelper.updateModuleVersions(db);
            esisId = com.entelience.directory.PeopleFactory.lookupUserName(db, "esis");
            assertNotNull(esisId);
            guestId = com.entelience.directory.PeopleFactory.lookupUserName(db, "guest");
            assertNotNull(guestId);
            extenId = com.entelience.directory.PeopleFactory.lookupUserName(db, "exten");
            assertNotNull(extenId);
            db.commit();
        } catch (Exception e) {
            db.rollback();
            throw e;
        }
    }

