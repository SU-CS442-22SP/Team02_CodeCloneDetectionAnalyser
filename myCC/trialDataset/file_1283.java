    public synchronized void readModels(Project p, URL url) throws IOException {
        _underscoreproj = p;
        Argo.log.info("=======================================");
        Argo.log.info("== READING MODEL " + url);
        try {
            XMIReader reader = new XMIReader();
            InputSource source = new InputSource(url.openStream());
            source.setSystemId(url.toString());
            _underscorecurModel = reader.parse(source);
            if (reader.getErrors()) {
                throw new IOException("XMI file " + url.toString() + " could not be parsed.");
            }
            _underscoreUUIDRefs = new HashMap(reader.getXMIUUIDToObjectMap());
        } catch (SAXException saxEx) {
            Exception ex = saxEx.getException();
            if (ex == null) {
                saxEx.printStackTrace();
            } else {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Argo.log.info("=======================================");
        try {
            _underscoreproj.addModel((ru.novosoft.uml.foundation.core.MNamespace) _underscorecurModel);
        } catch (PropertyVetoException ex) {
            System.err.println("An error occurred adding the model to the project!");
            ex.printStackTrace();
        }
        Collection ownedElements = _underscorecurModel.getOwnedElements();
        Iterator oeIterator = ownedElements.iterator();
        while (oeIterator.hasNext()) {
            MModelElement me = (MModelElement) oeIterator.next();
            if (me instanceof MClass) {
                _underscoreproj.defineType((MClass) me);
            } else if (me instanceof MDataType) {
                _underscoreproj.defineType((MDataType) me);
            }
        }
    }

