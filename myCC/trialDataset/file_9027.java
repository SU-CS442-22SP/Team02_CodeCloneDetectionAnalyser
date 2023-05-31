    private List loadPluginFromDir(File directory, boolean bSkipAlreadyLoaded, boolean loading_underscorefor_underscorestartup, boolean initialise) throws PluginException {
        List loaded_underscorepis = new ArrayList();
        ClassLoader plugin_underscoreclass_underscoreloader = root_underscoreclass_underscoreloader;
        if (!directory.isDirectory()) {
            return (loaded_underscorepis);
        }
        String pluginName = directory.getName();
        File[] pluginContents = directory.listFiles();
        if (pluginContents == null || pluginContents.length == 0) {
            return (loaded_underscorepis);
        }
        boolean looks_underscorelike_underscoreplugin = false;
        for (int i = 0; i < pluginContents.length; i++) {
            String name = pluginContents[i].getName().toLowerCase();
            if (name.endsWith(".jar") || name.equals("plugin.properties")) {
                looks_underscorelike_underscoreplugin = true;
                break;
            }
        }
        if (!looks_underscorelike_underscoreplugin) {
            if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, LogEvent.LT_underscoreWARNING, "Plugin directory '" + directory + "' has no plugin.properties " + "or .jar files, skipping"));
            return (loaded_underscorepis);
        }
        String[] plugin_underscoreversion = { null };
        String[] plugin_underscoreid = { null };
        pluginContents = PluginLauncherImpl.getHighestJarVersions(pluginContents, plugin_underscoreversion, plugin_underscoreid, true);
        for (int i = 0; i < pluginContents.length; i++) {
            File jar_underscorefile = pluginContents[i];
            if (pluginContents.length > 1) {
                String name = jar_underscorefile.getName();
                if (name.startsWith("i18nPlugin_underscore")) {
                    if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "renaming '" + name + "' to conform with versioning system"));
                    jar_underscorefile.renameTo(new File(jar_underscorefile.getParent(), "i18nAZ_underscore0.1.jar  "));
                    continue;
                }
            }
            plugin_underscoreclass_underscoreloader = PluginLauncherImpl.addFileToClassPath(root_underscoreclass_underscoreloader, plugin_underscoreclass_underscoreloader, jar_underscorefile);
        }
        String plugin_underscoreclass_underscorestring = null;
        try {
            Properties props = new Properties();
            File properties_underscorefile = new File(directory.toString() + File.separator + "plugin.properties");
            try {
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
                    if (plugin_underscoreclass_underscoreloader instanceof URLClassLoader) {
                        URLClassLoader current = (URLClassLoader) plugin_underscoreclass_underscoreloader;
                        URL url = current.findResource("plugin.properties");
                        if (url != null) {
                            URLConnection connection = url.openConnection();
                            InputStream is = connection.getInputStream();
                            props.load(is);
                        } else {
                            throw (new Exception("failed to load plugin.properties from jars"));
                        }
                    } else {
                        throw (new Exception("failed to load plugin.properties from dir or jars"));
                    }
                }
            } catch (Throwable e) {
                Debug.printStackTrace(e);
                String msg = "Can't read 'plugin.properties' for plugin '" + pluginName + "': file may be missing";
                Logger.log(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_underscoreERROR, msg));
                System.out.println(msg);
                throw (new PluginException(msg, e));
            }
            checkJDKVersion(pluginName, props, true);
            checkAzureusVersion(pluginName, props, true);
            plugin_underscoreclass_underscorestring = (String) props.get("plugin.class");
            if (plugin_underscoreclass_underscorestring == null) {
                plugin_underscoreclass_underscorestring = (String) props.get("plugin.classes");
                if (plugin_underscoreclass_underscorestring == null) {
                    plugin_underscoreclass_underscorestring = "";
                }
            }
            String plugin_underscorename_underscorestring = (String) props.get("plugin.name");
            if (plugin_underscorename_underscorestring == null) {
                plugin_underscorename_underscorestring = (String) props.get("plugin.names");
            }
            int pos1 = 0;
            int pos2 = 0;
            while (true) {
                int p1 = plugin_underscoreclass_underscorestring.indexOf(";", pos1);
                String plugin_underscoreclass;
                if (p1 == -1) {
                    plugin_underscoreclass = plugin_underscoreclass_underscorestring.substring(pos1).trim();
                } else {
                    plugin_underscoreclass = plugin_underscoreclass_underscorestring.substring(pos1, p1).trim();
                    pos1 = p1 + 1;
                }
                PluginInterfaceImpl existing_underscorepi = getPluginFromClass(plugin_underscoreclass);
                if (existing_underscorepi != null) {
                    if (bSkipAlreadyLoaded) {
                        break;
                    }
                    File this_underscoreparent = directory.getParentFile();
                    File existing_underscoreparent = null;
                    if (existing_underscorepi.getInitializerKey() instanceof File) {
                        existing_underscoreparent = ((File) existing_underscorepi.getInitializerKey()).getParentFile();
                    }
                    if (this_underscoreparent.equals(FileUtil.getApplicationFile("plugins")) && existing_underscoreparent != null && existing_underscoreparent.equals(FileUtil.getUserFile("plugins"))) {
                        if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "Plugin '" + plugin_underscorename_underscorestring + "/" + plugin_underscoreclass + ": shared version overridden by user-specific one"));
                        return (new ArrayList());
                    } else {
                        Logger.log(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_underscoreWARNING, "Error loading '" + plugin_underscorename_underscorestring + "', plugin class '" + plugin_underscoreclass + "' is already loaded"));
                    }
                } else {
                    String plugin_underscorename = null;
                    if (plugin_underscorename_underscorestring != null) {
                        int p2 = plugin_underscorename_underscorestring.indexOf(";", pos2);
                        if (p2 == -1) {
                            plugin_underscorename = plugin_underscorename_underscorestring.substring(pos2).trim();
                        } else {
                            plugin_underscorename = plugin_underscorename_underscorestring.substring(pos2, p2).trim();
                            pos2 = p2 + 1;
                        }
                    }
                    Properties new_underscoreprops = (Properties) props.clone();
                    for (int j = 0; j < default_underscoreversion_underscoredetails.length; j++) {
                        if (plugin_underscoreclass.equals(default_underscoreversion_underscoredetails[j][0])) {
                            if (new_underscoreprops.get("plugin.id") == null) {
                                new_underscoreprops.put("plugin.id", default_underscoreversion_underscoredetails[j][1]);
                            }
                            if (plugin_underscorename == null) {
                                plugin_underscorename = default_underscoreversion_underscoredetails[j][2];
                            }
                            if (new_underscoreprops.get("plugin.version") == null) {
                                if (plugin_underscoreversion[0] != null) {
                                    new_underscoreprops.put("plugin.version", plugin_underscoreversion[0]);
                                } else {
                                    new_underscoreprops.put("plugin.version", default_underscoreversion_underscoredetails[j][3]);
                                }
                            }
                        }
                    }
                    new_underscoreprops.put("plugin.class", plugin_underscoreclass);
                    if (plugin_underscorename != null) {
                        new_underscoreprops.put("plugin.name", plugin_underscorename);
                    }
                    Throwable load_underscorefailure = null;
                    String pid = plugin_underscoreid[0] == null ? directory.getName() : plugin_underscoreid[0];
                    List<File> verified_underscorefiles = null;
                    Plugin plugin = null;
                    if (vc_underscoredisabled_underscoreplugins.contains(pid)) {
                        log("Plugin '" + pid + "' has been administratively disabled");
                    } else {
                        if (pid.endsWith("_underscorev")) {
                            verified_underscorefiles = new ArrayList<File>();
                            log("Re-verifying " + pid);
                            for (int i = 0; i < pluginContents.length; i++) {
                                File jar_underscorefile = pluginContents[i];
                                if (jar_underscorefile.getName().endsWith(".jar")) {
                                    try {
                                        log("    verifying " + jar_underscorefile);
                                        AEVerifier.verifyData(jar_underscorefile);
                                        verified_underscorefiles.add(jar_underscorefile);
                                        log("    OK");
                                    } catch (Throwable e) {
                                        String msg = "Error loading plugin '" + pluginName + "' / '" + plugin_underscoreclass_underscorestring + "'";
                                        Logger.log(new LogAlert(LogAlert.UNREPEATABLE, msg, e));
                                        plugin = new FailedPlugin(plugin_underscorename, directory.getAbsolutePath());
                                    }
                                }
                            }
                        }
                        if (plugin == null) {
                            plugin = PluginLauncherImpl.getPreloadedPlugin(plugin_underscoreclass);
                            if (plugin == null) {
                                try {
                                    Class c = plugin_underscoreclass_underscoreloader.loadClass(plugin_underscoreclass);
                                    plugin = (Plugin) c.newInstance();
                                } catch (java.lang.UnsupportedClassVersionError e) {
                                    plugin = new FailedPlugin(plugin_underscorename, directory.getAbsolutePath());
                                    load_underscorefailure = new UnsupportedClassVersionError(e.getMessage());
                                } catch (Throwable e) {
                                    if (e instanceof ClassNotFoundException && props.getProperty("plugin.install_underscoreif_underscoremissing", "no").equalsIgnoreCase("yes")) {
                                    } else {
                                        load_underscorefailure = e;
                                    }
                                    plugin = new FailedPlugin(plugin_underscorename, directory.getAbsolutePath());
                                }
                            } else {
                                plugin_underscoreclass_underscoreloader = plugin.getClass().getClassLoader();
                            }
                        }
                        MessageText.integratePluginMessages((String) props.get("plugin.langfile"), plugin_underscoreclass_underscoreloader);
                        PluginInterfaceImpl plugin_underscoreinterface = new PluginInterfaceImpl(plugin, this, directory, plugin_underscoreclass_underscoreloader, verified_underscorefiles, directory.getName(), new_underscoreprops, directory.getAbsolutePath(), pid, plugin_underscoreversion[0]);
                        boolean bEnabled = (loading_underscorefor_underscorestartup) ? plugin_underscoreinterface.getPluginState().isLoadedAtStartup() : initialise;
                        plugin_underscoreinterface.getPluginState().setDisabled(!bEnabled);
                        try {
                            Method load_underscoremethod = plugin.getClass().getMethod("load", new Class[] { PluginInterface.class });
                            load_underscoremethod.invoke(plugin, new Object[] { plugin_underscoreinterface });
                        } catch (NoSuchMethodException e) {
                        } catch (Throwable e) {
                            load_underscorefailure = e;
                        }
                        loaded_underscorepis.add(plugin_underscoreinterface);
                        if (load_underscorefailure != null) {
                            plugin_underscoreinterface.setAsFailed();
                            if (!pid.equals(UpdaterUpdateChecker.getPluginID())) {
                                String msg = "Error loading plugin '" + pluginName + "' / '" + plugin_underscoreclass_underscorestring + "'";
                                LogAlert la;
                                if (load_underscorefailure instanceof UnsupportedClassVersionError) {
                                    la = new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_underscoreERROR, msg + ". " + MessageText.getString("plugin.install.class_underscoreversion_underscoreerror"));
                                } else {
                                    la = new LogAlert(LogAlert.UNREPEATABLE, msg, load_underscorefailure);
                                }
                                Logger.log(la);
                                System.out.println(msg + ": " + load_underscorefailure);
                            }
                        }
                    }
                }
                if (p1 == -1) {
                    break;
                }
            }
            return (loaded_underscorepis);
        } catch (Throwable e) {
            if (e instanceof PluginException) {
                throw ((PluginException) e);
            }
            Debug.printStackTrace(e);
            String msg = "Error loading plugin '" + pluginName + "' / '" + plugin_underscoreclass_underscorestring + "'";
            Logger.log(new LogAlert(LogAlert.UNREPEATABLE, msg, e));
            System.out.println(msg + ": " + e);
            throw (new PluginException(msg, e));
        }
    }

