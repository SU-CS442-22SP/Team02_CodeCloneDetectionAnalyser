    public static void main(String[] args) {
        LogFrame.getInstance();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.trim().startsWith(DEBUG_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(DEBUG_underscorePARAMETER_underscoreNAME, arg.trim().substring(DEBUG_underscorePARAMETER_underscoreNAME.length() + 1).trim());
                if (properties.getProperty(DEBUG_underscorePARAMETER_underscoreNAME).toLowerCase().equals(DEBUG_underscoreTRUE)) {
                    DEBUG = true;
                }
            } else if (arg.trim().startsWith(AUTOCONNECT_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(AUTOCONNECT_underscorePARAMETER_underscoreNAME, arg.trim().substring(AUTOCONNECT_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME, arg.trim().substring(SITE_underscoreCONFIG_underscoreURL_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(DOCSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(DOCSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME, arg.trim().substring(DOCSERVICE_underscoreURL_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(DOC_underscoreID_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(DOC_underscoreID_underscorePARAMETER_underscoreNAME, arg.trim().substring(DOC_underscoreID_underscorePARAMETER_underscoreNAME.length() + 1).trim());
            } else if (arg.trim().startsWith(DOCSERVICE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME + "=")) {
                properties.put(DOCSERVICE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME, arg.trim().substring(DOCSERVICE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME.length() + 1).trim());
                RichUIUtils.setDocServiceProxyFactoryClassname(properties.getProperty(DOCSERVICE_underscorePROXY_underscoreFACTORY_underscorePARAMETER_underscoreNAME));
            } else {
                System.out.println("WARNING! Unknown or undefined parameter: '" + arg.trim() + "'");
            }
        }
        System.out.println("Annotation Diff GUI startup parameters:");
        System.out.println("------------------------------");
        for (Object propName : properties.keySet()) {
            System.out.println(propName.toString() + "=" + properties.getProperty((String) propName));
        }
        System.out.println("------------------------------");
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
        } catch (Throwable e) {
            e.printStackTrace();
        }
        MainFrame.getInstance().setVisible(true);
        MainFrame.getInstance().pack();
        if (properties.getProperty(AUTOCONNECT_underscorePARAMETER_underscoreNAME, "").toLowerCase().equals(AUTOCONNECT_underscoreTRUE)) {
            if (properties.getProperty(DOC_underscoreID_underscorePARAMETER_underscoreNAME) == null || properties.getProperty(DOC_underscoreID_underscorePARAMETER_underscoreNAME).length() == 0) {
                String err = "Can't autoconnect. A parameter '" + DOC_underscoreID_underscorePARAMETER_underscoreNAME + "' is missing.";
                System.out.println(err);
                JOptionPane.showMessageDialog(new JFrame(), err, "Error!", JOptionPane.ERROR_underscoreMESSAGE);
                ActionShowAnnDiffConnectDialog.getInstance().actionPerformed(null);
            } else {
                ActionConnectToAnnDiffGUI.getInstance().actionPerformed(null);
            }
        } else {
            ActionShowAnnDiffConnectDialog.getInstance().actionPerformed(null);
        }
    }

