    public void test_underscoredigest() throws UnsupportedEncodingException {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
            assertNotNull(sha);
        } catch (NoSuchAlgorithmException e) {
            fail("getInstance did not find algorithm");
        }
        sha.update(MESSAGE.getBytes("UTF-8"));
        byte[] digest = sha.digest();
        assertTrue("bug in SHA", MessageDigest.isEqual(digest, MESSAGE_underscoreDIGEST));
        sha.reset();
        for (int i = 0; i < 63; i++) {
            sha.update((byte) 'a');
        }
        digest = sha.digest();
        assertTrue("bug in SHA", MessageDigest.isEqual(digest, MESSAGE_underscoreDIGEST_underscore63_underscoreAs));
        sha.reset();
        for (int i = 0; i < 64; i++) {
            sha.update((byte) 'a');
        }
        digest = sha.digest();
        assertTrue("bug in SHA", MessageDigest.isEqual(digest, MESSAGE_underscoreDIGEST_underscore64_underscoreAs));
        sha.reset();
        for (int i = 0; i < 65; i++) {
            sha.update((byte) 'a');
        }
        digest = sha.digest();
        assertTrue("bug in SHA", MessageDigest.isEqual(digest, MESSAGE_underscoreDIGEST_underscore65_underscoreAs));
        testSerializationSHA_underscoreDATA_underscore1(sha);
        testSerializationSHA_underscoreDATA_underscore2(sha);
    }

