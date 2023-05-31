    private void _underscoreloadInternalViews() {
        _underscoreinternalViews = new HashMap<String, InternalView>();
        List<URL> list = new ArrayList<URL>();
        ClassLoader loader = _underscoregetClassLoader();
        try {
            Enumeration<URL> en = loader.getResources("META-INF/org.apache.myfaces.trinidad.render.InternalView.properties");
            while (en.hasMoreElements()) {
                list.add(en.nextElement());
            }
            en = loader.getResources("META-INF/org.apache.myfaces.trinidad.InternalView.properties");
            while (en.hasMoreElements()) {
                list.add(en.nextElement());
            }
            Collections.reverse(list);
        } catch (IOException ioe) {
            _underscoreLOG.severe(ioe);
        }
        for (URL url : list) {
            try {
                Properties properties = new Properties();
                _underscoreLOG.fine("Loading internal views from {0}", url);
                InputStream is = url.openStream();
                try {
                    properties.load(is);
                } finally {
                    is.close();
                }
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    String name = (String) entry.getKey();
                    String className = (String) entry.getValue();
                    Class<?> clazz = loader.loadClass(className);
                    InternalView view = (InternalView) clazz.newInstance();
                    _underscoreinternalViews.put(name, view);
                }
            } catch (IllegalAccessException iae) {
                _underscoreLOG.severe("CANNOT_underscoreLOAD_underscoreURL", url);
                _underscoreLOG.severe(iae);
            } catch (InstantiationException ie) {
                _underscoreLOG.severe("CANNOT_underscoreLOAD_underscoreURL", url);
                _underscoreLOG.severe(ie);
            } catch (ClassNotFoundException cnfe) {
                _underscoreLOG.severe("CANNOT_underscoreLOAD_underscoreURL", url);
                _underscoreLOG.severe(cnfe);
            } catch (IOException ioe) {
                _underscoreLOG.severe("CANNOT_underscoreLOAD_underscoreURL", url);
                _underscoreLOG.severe(ioe);
            }
        }
    }

