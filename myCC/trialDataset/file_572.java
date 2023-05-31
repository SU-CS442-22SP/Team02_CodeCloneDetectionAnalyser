    public void resolvePlugins() {
        try {
            File cacheDir = XPontusConfigurationConstantsIF.XPONTUS_underscoreCACHE_underscoreDIR;
            File pluginsFile = new File(cacheDir, "plugins.xml");
            if (!pluginsFile.exists()) {
                URL pluginURL = new URL("http://xpontus.sourceforge.net/snapshot/plugins.xml");
                InputStream is = pluginURL.openStream();
                OutputStream os = FileUtils.openOutputStream(pluginsFile);
                IOUtils.copy(is, os);
                IOUtils.closeQuietly(os);
                IOUtils.closeQuietly(is);
            }
            resolvePlugins(pluginsFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

