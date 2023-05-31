    private static FacesBean.Type _underscorecreateType() {
        try {
            ClassLoader cl = _underscoregetClassLoader();
            URL url = cl.getResource("META-INF/faces-bean-type.properties");
            if (url != null) {
                Properties properties = new Properties();
                InputStream is = url.openStream();
                try {
                    properties.load(is);
                    String className = (String) properties.get(UIXComponentBase.class.getName());
                    return (FacesBean.Type) cl.loadClass(className).newInstance();
                } finally {
                    is.close();
                }
            }
        } catch (Exception e) {
            _underscoreLOG.severe("CANNOT_underscoreLOAD_underscoreTYPE_underscorePROPERTIES", e);
        }
        return new FacesBean.Type();
    }

