    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getJarFile", args = {  })
    public void test_underscoregetJarFile() throws MalformedURLException, IOException {
        URL url = null;
        url = createContent("lf.jar", "missing");
        JarURLConnection connection = null;
        connection = (JarURLConnection) url.openConnection();
        try {
            connection.connect();
            fail("Did not throw exception on connect");
        } catch (IOException e) {
        }
        try {
            connection.getJarFile();
            fail("Did not throw exception after connect");
        } catch (IOException e) {
        }
        URL invURL = createContent("InvalidJar.jar", "Test.class");
        JarURLConnection juConn = (JarURLConnection) invURL.openConnection();
        try {
            juConn.getJarFile();
            fail("IOException was not thrown.");
        } catch (java.io.IOException io) {
        }
        File resources = Support_underscoreResources.createTempFolder();
        Support_underscoreResources.copyFile(resources, null, "hyts_underscoreatt.jar");
        File file = new File(resources.toString() + "/hyts_underscoreatt.jar");
        URL fUrl1 = new URL("jar:file:" + file.getPath() + "!/");
        JarURLConnection con1 = (JarURLConnection) fUrl1.openConnection();
        ZipFile jf1 = con1.getJarFile();
        JarURLConnection con2 = (JarURLConnection) fUrl1.openConnection();
        ZipFile jf2 = con2.getJarFile();
        assertTrue("file: JarFiles not the same", jf1 == jf2);
        jf1.close();
        assertTrue("File should exist", file.exists());
        fUrl1 = createContent("lf.jar", "");
        con1 = (JarURLConnection) fUrl1.openConnection();
        jf1 = con1.getJarFile();
        con2 = (JarURLConnection) fUrl1.openConnection();
        jf2 = con2.getJarFile();
        assertTrue("http: JarFiles not the same", jf1 == jf2);
        jf1.close();
    }

