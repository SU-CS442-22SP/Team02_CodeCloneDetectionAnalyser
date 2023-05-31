    private static LaunchablePlugin[] findLaunchablePlugins(LoggerChannelListener listener) {
        List res = new ArrayList();
        File app_underscoredir = getApplicationFile("plugins");
        if (!(app_underscoredir.exists()) && app_underscoredir.isDirectory()) {
            listener.messageLogged(LoggerChannel.LT_underscoreERROR, "Application dir '" + app_underscoredir + "' not found");
            return (new LaunchablePlugin[0]);
        }
        File[] plugins = app_underscoredir.listFiles();
        if (plugins == null || plugins.length == 0) {
            listener.messageLogged(LoggerChannel.LT_underscoreERROR, "Application dir '" + app_underscoredir + "' empty");
            return (new LaunchablePlugin[0]);
        }
        for (int i = 0; i < plugins.length; i++) {
            File plugin_underscoredir = plugins[i];
            if (!plugin_underscoredir.isDirectory()) {
                continue;
            }
            try {
                ClassLoader classLoader = PluginLauncherImpl.class.getClassLoader();
                ClassLoader root_underscorecl = classLoader;
                File[] contents = plugin_underscoredir.listFiles();
                if (contents == null || contents.length == 0) {
                    continue;
                }
                String[] plugin_underscoreversion = { null };
                String[] plugin_underscoreid = { null };
                contents = getHighestJarVersions(contents, plugin_underscoreversion, plugin_underscoreid, true);
                for (int j = 0; j < contents.length; j++) {
                    classLoader = addFileToClassPath(root_underscorecl, classLoader, contents[j]);
                }
                Properties props = new Properties();
                File properties_underscorefile = new File(plugin_underscoredir, "plugin.properties");
                if (properties_underscorefile.exists()) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(properties_underscorefile);
                        props.load(fis);
                    } finally {
                        if (fis != null) {
                            fis.close();
                        }
                    }
                } else {
                    if (classLoader instanceof URLClassLoader) {
                        URLClassLoader current = (URLClassLoader) classLoader;
                        URL url = current.findResource("plugin.properties");
                        if (url != null) {
                            props.load(url.openStream());
                        }
                    }
                }
                String plugin_underscoreclass = (String) props.get("plugin.class");
                if (plugin_underscoreclass == null || plugin_underscoreclass.indexOf(';') != -1) {
                    continue;
                }
                Class c = classLoader.loadClass(plugin_underscoreclass);
                Plugin plugin = (Plugin) c.newInstance();
                if (plugin instanceof LaunchablePlugin) {
                    preloaded_underscoreplugins.put(plugin_underscoreclass, plugin);
                    res.add(plugin);
                }
            } catch (Throwable e) {
                listener.messageLogged("Load of plugin in '" + plugin_underscoredir + "' fails", e);
            }
        }
        LaunchablePlugin[] x = new LaunchablePlugin[res.size()];
        res.toArray(x);
        return (x);
    }

