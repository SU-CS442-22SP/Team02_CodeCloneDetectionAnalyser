    @Test
    public void testMark() throws IllegalArgumentException, IOException {
        Assert.assertNotNull(this.m_underscoredetector);
        File f = new File("testdocuments/voiddocument/Voiderror.htm");
        Assert.assertTrue("Test file " + f.getAbsolutePath() + " does not exist. ", f.exists());
        URL url = f.toURL();
        this.m_underscoredetector.detectCodepage(url.openStream(), 200);
    }

