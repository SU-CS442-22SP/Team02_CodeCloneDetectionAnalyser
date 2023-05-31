    private ModelDefinition buildModel(String name) {
        ModelDefinition model = null;
        URL url = ResourceLocator.locateBinaryModel(name);
        InputStream is = null;
        if (url == null) {
            url = ResourceLocator.locateTextModel(name);
            try {
                is = url.openStream();
                model = buildModelFromText(name, is);
                File file = ResourceLocator.replaceExtension(url, ResourceLocator.BINARY_underscoreMODEL_underscoreEXTENSION);
                BinaryExporter.getInstance().save(model, file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            try {
                is = url.openStream();
                model = (ModelDefinition) BinaryImporter.getInstance().load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return model;
    }

