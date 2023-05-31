    public static void main(String[] args) {
        LogFrame.getInstance();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.trim().startsWith(DEBUG_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(DEBUG_underscorePARAMETER_underscoreNAME, arg.trim().substring(DEBUG_underscorePARAMETER_underscoreNAME.length() + 1).trim());
                if (properties.getProperty(DEBUG_underscorePARAMETER_underscoreNAME).toLowerCase().equals(DEBUG_underscoreTRUE)) {
                    DEBUG = true;
                }
            } else if (arg.trim().startsWith(MODE_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(MODE_underscorePARAMETER_underscoreNAME, arg.trim().substring(MODE_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(AUTOCONNECT_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(AUTOCONNECT_underscorePARAMETER_underscoreNAME, arg.trim().substring(AUTOCONNECT_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME, arg.trim().substring(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(LOAD_underscorePLUGINS_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(LOAD_underscorePLUGINS_underscorePARAMETER_underscoreNAME, arg.trim().substring(LOAD_underscorePLUGINS_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(ONTOLOGY_underscoreURL_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(ONTOLOGY_underscoreURL_underscorePARAMETER_underscoreNAME, arg.trim().substring(ONTOLOGY_underscoreURL_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(REPOSITORY_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(REPOSITORY_underscorePARAMETER_underscoreNAME, arg.trim().substring(REPOSITORY_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(ONTOLOGY_underscoreTYPE_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(ONTOLOGY_underscoreTYPE_underscorePARAMETER_underscoreNAME, arg.trim().substring(ONTOLOGY_underscoreTYPE_underscorePARAMETER_underscoreNAME.length() + 1).trim());
                if (!(properties.getProperty(ONTOLOGY_underscoreTYPE_underscorePARAMETER_underscoreNAME).equals(ONTOLOGY_underscoreTYPE_underscoreRDFXML) || properties.getProperty(ONTOLOGY_underscoreTYPE_underscorePARAMETER_underscoreNAME).equals(ONTOLOGY_underscoreTYPE_underscoreTURTLE) || properties.getProperty(ONTOLOGY_underscoreTYPE_underscorePARAMETER_underscoreNAME).equals(ONTOLOGY_underscoreTYPE_underscoreNTRIPPLES))) System.out.println("WARNING! Unknown ontology type: '" + properties.getProperty(ONTOLOGY_underscoreTYPE_underscorePARAMETER_underscoreNAME) + "' (Known types are: '" + ONTOLOGY_underscoreTYPE_underscoreRDFXML + "', '" + ONTOLOGY_underscoreTYPE_underscoreTURTLE + "', '" + ONTOLOGY_underscoreTYPE_underscoreNTRIPPLES + "')");
            } else if (arg.trim().startsWith(OWLIMSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(OWLIMSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME, arg.trim().substring(OWLIMSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(DOCSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(DOCSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME, arg.trim().substring(DOCSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(DOC_underscoreID_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(DOC_underscoreID_underscorePARAMETER_underscoreNAME, arg.trim().substring(DOC_underscoreID_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(ANNSET_underscoreNAME_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(ANNSET_underscoreNAME_underscorePARAMETER_underscoreNAME, arg.trim().substring(ANNSET_underscoreNAME_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(EXECUTIVE_underscoreSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(EXECUTIVE_underscoreSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME, arg.trim().substring(EXECUTIVE_underscoreSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(USER_underscoreID_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(USER_underscoreID_underscorePARAMETER_underscoreNAME, arg.trim().substring(USER_underscoreID_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(USER_underscorePASSWORD_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(USER_underscorePASSWORD_underscorePARAMETER_underscoreNAME, arg.trim().substring(USER_underscorePASSWORD_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(EXECUTIVE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(EXECUTIVE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME, arg.trim().substring(EXECUTIVE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(DOCSERVICE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(DOCSERVICE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME, arg.trim().substring(DOCSERVICE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME.length() + 1).trim());
                RichUIUtils.setDocServiceProxyFactoryClassname(properties.getProperty(DOCSERVICE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME));
            } else if (arg.trim().startsWith(LOAD_underscoreANN_underscoreSCHEMAS_underscoreNAME + "=")) {
                properties.put(LOAD_underscoreANN_underscoreSCHEMAS_underscoreNAME, arg.trim().substring(LOAD_underscoreANN_underscoreSCHEMAS_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(SELECT_underscoreAS_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(SELECT_underscoreAS_underscorePARAMETER_underscoreNAME, arg.trim().substring(SELECT_underscoreAS_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(SELECT_underscoreANN_underscoreTYPES_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(SELECT_underscoreANN_underscoreTYPES_underscorePARAMETER_underscoreNAME, arg.trim().substring(SELECT_underscoreANN_underscoreTYPES_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(ENABLE_underscoreONTOLOGY_underscoreEDITOR_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(ENABLE_underscoreONTOLOGY_underscoreEDITOR_underscorePARAMETER_underscoreNAME, arg.trim().substring(ENABLE_underscoreONTOLOGY_underscoreEDITOR_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(CLASSES_underscoreTO_underscoreHIDE_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(CLASSES_underscoreTO_underscoreHIDE_underscorePARAMETER_underscoreNAME, arg.trim().substring(CLASSES_underscoreTO_underscoreHIDE_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(CLASSES_underscoreTO_underscoreSHOW_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(CLASSES_underscoreTO_underscoreSHOW_underscorePARAMETER_underscoreNAME, arg.trim().substring(CLASSES_underscoreTO_underscoreSHOW_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(ENABLE_underscoreAPPLICATION_underscoreLOG_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(ENABLE_underscoreAPPLICATION_underscoreLOG_underscorePARAMETER_underscoreNAME, arg.trim().substring(ENABLE_underscoreAPPLICATION_underscoreLOG_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else {
                System.out.println("WARNING! Unknown or undefined parameter: '" + arg.trim() + "'");
            }
        }
        System.out.println(startupParamsToString());
        if (properties.getProperty(MODE_underscorePARAMETER_underscoreNAME) == null || (!(properties.getProperty(MODE_underscorePARAMETER_underscoreNAME).toLowerCase().equals(POOL_underscoreMODE)) && !(properties.getProperty(MODE_underscorePARAMETER_underscoreNAME).toLowerCase().equals(DIRECT_underscoreMODE)))) {
            String err = "Mandatory parameter '" + MODE_underscorePARAMETER_underscoreNAME + "' must be defined and must have a value either '" + POOL_underscoreMODE + "' or '" + DIRECT_underscoreMODE + "'.\n\nApplication will exit.";
            System.out.println(err);
            JOptionPane.showMessageDialog(new JFrame(), err, "Error!", JOptionPane.ERROR_underscoreMESSAGE);
            System.exit(-1);
        }
        if (properties.getProperty(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME) == null || properties.getProperty(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME).length() == 0) {
            String err = "Mandatory parameter '" + SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME + "' is missing.\n\nApplication will exit.";
            System.out.println(err);
            JOptionPane.showMessageDialog(new JFrame(), err, "Error!", JOptionPane.ERROR_underscoreMESSAGE);
            System.exit(-1);
        }
        try {
            String context = System.getProperty(CONTEXT);
            if (context == null || "".equals(context)) {
                context = DEFAULT_underscoreCONTEXT;
            }
            String s = System.getProperty(GateConstants.GATE_underscoreHOME_underscorePROPERTY_underscoreNAME);
            if (s == null || s.length() == 0) {
                File f = File.createTempFile("foo", "");
                String gateHome = f.getParent().toString() + context;
                f.delete();
                System.setProperty(GateConstants.GATE_underscoreHOME_underscorePROPERTY_underscoreNAME, gateHome);
                f = new File(System.getProperty(GateConstants.GATE_underscoreHOME_underscorePROPERTY_underscoreNAME));
                if (!f.exists()) {
                    f.mkdirs();
                }
            }
            s = System.getProperty(GateConstants.PLUGINS_underscoreHOME_underscorePROPERTY_underscoreNAME);
            if (s == null || s.length() == 0) {
                System.setProperty(GateConstants.PLUGINS_underscoreHOME_underscorePROPERTY_underscoreNAME, System.getProperty(GateConstants.GATE_underscoreHOME_underscorePROPERTY_underscoreNAME) + "/plugins");
                File f = new File(System.getProperty(GateConstants.PLUGINS_underscoreHOME_underscorePROPERTY_underscoreNAME));
                if (!f.exists()) {
                    f.mkdirs();
                }
            }
            s = System.getProperty(GateConstants.GATE_underscoreSITE_underscoreCONFIG_underscorePROPERTY_underscoreNAME);
            if (s == null || s.length() == 0) {
                System.setProperty(GateConstants.GATE_underscoreSITE_underscoreCONFIG_underscorePROPERTY_underscoreNAME, System.getProperty(GateConstants.GATE_underscoreHOME_underscorePROPERTY_underscoreNAME) + "/gate.xml");
            }
            if (properties.getProperty(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME) != null && properties.getProperty(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME).length() > 0) {
                File f = new File(System.getProperty(GateConstants.GATE_underscoreSITE_underscoreCONFIG_underscorePROPERTY_underscoreNAME));
                if (f.exists()) {
                    f.delete();
                }
                f.getParentFile().mkdirs();
                f.createNewFile();
                URL url = new URL(properties.getProperty(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME));
                InputStream is = url.openStream();
                FileOutputStream fos = new FileOutputStream(f);
                int i = is.read();
                while (i != -1) {
                    fos.write(i);
                    i = is.read();
                }
                fos.close();
                is.close();
            }
            try {
                Gate.init();
                gate.Main.applyUserPreferences();
            } catch (Exception e) {
                e.printStackTrace();
            }
            s = BASE_underscorePLUGIN_underscoreNAME + "," + properties.getProperty(LOAD_underscorePLUGINS_underscorePARAMETER_underscoreNAME);
            System.out.println("Loading plugins: " + s);
            loadPlugins(s, true);
            loadAnnotationSchemas(properties.getProperty(LOAD_underscoreANN_underscoreSCHEMAS_underscoreNAME), true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        MainFrame.getInstance().setVisible(true);
        MainFrame.getInstance().pack();
        if (properties.getProperty(MODE_underscorePARAMETER_underscoreNAME).toLowerCase().equals(DIRECT_underscoreMODE)) {
            if (properties.getProperty(AUTOCONNECT_underscorePARAMETER_underscoreNAME, "").toLowerCase().equals(AUTOCONNECT_underscoreTRUE)) {
                if (properties.getProperty(DOC_underscoreID_underscorePARAMETER_underscoreNAME) == null || properties.getProperty(DOC_underscoreID_underscorePARAMETER_underscoreNAME).length() == 0) {
                    String err = "Can't autoconnect. A parameter '" + DOC_underscoreID_underscorePARAMETER_underscoreNAME + "' is missing.";
                    System.out.println(err);
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), err, "Error!", JOptionPane.ERROR_underscoreMESSAGE);
                    ActionShowDocserviceConnectDialog.getInstance().actionPerformed(null);
                } else {
                    ActionConnectToDocservice.getInstance().actionPerformed(null);
                }
            } else {
                ActionShowDocserviceConnectDialog.getInstance().actionPerformed(null);
            }
        } else {
            if (properties.getProperty(AUTOCONNECT_underscorePARAMETER_underscoreNAME, "").toLowerCase().equals(AUTOCONNECT_underscoreTRUE)) {
                if (properties.getProperty(USER_underscoreID_underscorePARAMETER_underscoreNAME) == null || properties.getProperty(USER_underscoreID_underscorePARAMETER_underscoreNAME).length() == 0) {
                    String err = "Can't autoconnect. A parameter '" + USER_underscoreID_underscorePARAMETER_underscoreNAME + "' is missing.";
                    System.out.println(err);
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), err, "Error!", JOptionPane.ERROR_underscoreMESSAGE);
                    ActionShowExecutiveConnectDialog.getInstance().actionPerformed(null);
                } else {
                    ActionConnectToExecutive.getInstance().actionPerformed(null);
                }
            } else {
                ActionShowExecutiveConnectDialog.getInstance().actionPerformed(null);
            }
        }
    }

