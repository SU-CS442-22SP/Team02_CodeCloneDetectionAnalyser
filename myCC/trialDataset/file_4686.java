    @Test
    public void test20_underscorebadSmtp() throws Exception {
        Db db = DbConnection.defaultCieDbRW();
        try {
            db.begin();
            oldSmtp = Config.getProperty(db, "com.entelience.mail.MailHelper.hostName", "localhost");
            oldSupport = Config.getProperty(db, "com.entelience.esis.feature.SupportNotifier", false);
            Config.setProperty(db, "com.entelience.mail.MailHelper.hostName", "127.0.10.1", 1);
            Config.setProperty(db, "com.entelience.esis.feature.SupportNotifier", "true", 1);
            PreparedStatement pst = db.prepareStatement("DELETE FROM t_underscoreclient_underscoreerrors");
            db.executeUpdate(pst);
            db.commit();
        } catch (Exception e) {
            db.rollback();
        } finally {
            db.safeClose();
        }
    }

