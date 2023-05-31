    @TestTargetNew(level = TestLevel.PARTIAL, notes = "Exceptions checking missed.", method = "setUseCaches", args = { boolean.class })
    public void test_underscoresetUseCaches() throws Exception {
        File resources = Support_underscoreResources.createTempFolder();
        Support_underscoreResources.copyFile(resources, null, "hyts_underscoreatt.jar");
        File file = new File(resources.toString() + "/hyts_underscoreatt.jar");
        URL url = new URL("jar:file:" + file.getPath() + "!/HasAttributes.txt");
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        connection.setUseCaches(false);
        InputStream in = connection.getInputStream();
        in = connection.getInputStream();
        JarFile jarFile1 = connection.getJarFile();
        JarEntry jarEntry1 = connection.getJarEntry();
        in.read();
        in.close();
        JarFile jarFile2 = connection.getJarFile();
        JarEntry jarEntry2 = connection.getJarEntry();
        assertSame(jarFile1, jarFile2);
        assertSame(jarEntry1, jarEntry2);
        try {
            connection.getInputStream();
            fail("should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

