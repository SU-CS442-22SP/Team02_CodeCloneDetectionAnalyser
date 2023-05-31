    public Ontology open(String resource_underscorename) {
        Ontology ontology = null;
        try {
            URL url = null;
            if (resource_underscorename.startsWith("jar")) url = new URL(resource_underscorename); else {
                ClassLoader cl = this.getClass().getClassLoader();
                url = cl.getResource(resource_underscorename);
            }
            InputStream input_underscorestream;
            if (url != null) {
                JarURLConnection jc = (JarURLConnection) url.openConnection();
                input_underscorestream = jc.getInputStream();
            } else input_underscorestream = new FileInputStream(resource_underscorename);
            ObjectInputStream ois = new ObjectInputStream(input_underscorestream);
            ontology = (Ontology) ois.readObject();
            ois.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return ontology;
    }

