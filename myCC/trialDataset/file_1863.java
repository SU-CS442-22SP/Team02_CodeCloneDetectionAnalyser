    public void testImageDataField() {
        PropertySet propertySet = fetchPropertySet();
        try {
            ImageDataField dataField = propertySet.getDataField(PropertySetTestStruct.TESTBLOCK_underscoreIMAGE, ImageDataField.class);
            URL url = getClass().getResource("JFire_underscoreTest.gif");
            if (url == null) return;
            InputStream in = url.openStream();
            try {
                dataField.loadStream(in, "JFire_underscoreTest.gif", "image/gif");
            } finally {
                in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Setting image of ImageDataField failed", e);
        }
        try {
            propertySet.deflate();
            getPropertyManager().storePropertySet(propertySet, true, FETCH_underscoreGROUPS, FETCH_underscoreDEPTH);
        } catch (Exception e) {
            throw new RuntimeException("Storing PropertySet with ImageDataField failed", e);
        }
    }

