    @Test
    public void test30_underscorepasswordAging() throws Exception {
        Db db = DbConnection.defaultCieDbRW();
        try {
            db.begin();
            Config.setProperty(db, "com.entelience.esis.security.passwordAge", "5", 1);
            PreparedStatement pst = db.prepareStatement("UPDATE e_underscorepeople SET last_underscorepasswd_underscorechange = '2006-07-01' WHERE user_underscorename = ?");
            pst.setString(1, "esis");
            db.executeUpdate(pst);
            db.commit();
            p_underscorelogout();
            t30login1();
            assertTrue(isPasswordExpired());
            PeopleInfoLine me = getCurrentPeople();
            assertNotNull(me.getPasswordExpirationDate());
            assertTrue(me.getPasswordExpirationDate().before(DateHelper.now()));
            t30chgpasswd();
            assertFalse(isPasswordExpired());
            me = getCurrentPeople();
            assertNotNull(me.getPasswordExpirationDate());
            assertTrue(me.getPasswordExpirationDate().after(DateHelper.now()));
            p_underscorelogout();
            t30login2();
            assertFalse(isPasswordExpired());
            t30chgpasswd2();
            db.begin();
            Config.setProperty(db, "com.entelience.esis.security.passwordAge", "0", 1);
            db.commit();
        } catch (Exception e) {
            e.printStackTrace();
            db.rollback();
        } finally {
            db.safeClose();
        }
    }

