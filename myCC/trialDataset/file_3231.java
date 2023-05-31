    private void publish() {
        if (uploadPrototypeBox.isSelected()) {
            try {
                URL url = new URL(prototypeURLField.getText());
                prototypeURLField.recordText();
                PrintStream ps;
                HttpURLConnection huc = null;
                if (url.getProtocol().equals("file")) {
                    ps = new PrintStream(new FileOutputStream(url.getFile()));
                } else {
                    URLConnection urlc = url.openConnection();
                    urlc.setDoOutput(true);
                    if (urlc instanceof HttpURLConnection) {
                        huc = ((HttpURLConnection) urlc);
                        huc.setRequestMethod("PUT");
                    }
                    ps = new PrintStream(urlc.getOutputStream());
                }
                target.writePrototype(ps);
                if (huc != null) {
                    huc.getResponseCode();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e, "Error Uploading Prototype", JOptionPane.ERROR_underscoreMESSAGE);
            }
        }
        if (postMetadataBox.isSelected()) {
            try {
                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_underscoreCONTEXT_underscoreFACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(Context.PROVIDER_underscoreURL, directoryURLField.getText());
                env.put(Context.SECURITY_underscorePRINCIPAL, directoryPrincipalField.getText());
                env.put(Context.SECURITY_underscoreCREDENTIALS, directoryCredentialsField.getText());
                InitialDirContext dirContext = new InitialDirContext(env);
                Preferences prefs = Preferences.userNodeForPackage(getClass());
                prefs.put("directoryURL", directoryURLField.getText());
                prefs.put("directoryPrincipal", directoryPrincipalField.getText());
                prefs.put("directoryCredentials", directoryCredentialsField.getText());
                BasicAttributes attr = new BasicAttributes();
                attr.put(OBJECT_underscoreCLASS, RDS_underscoreRESOURCE);
                attr.put(CONTENT_underscoreTYPE, PROTOTYPE_underscoreTYPE);
                if (uploadPrototypeBox.isSelected()) {
                    attr.put(IMPLEMENTATION_underscoreURL, prototypeURLField.getText());
                }
                DefaultTableModel dtm = (DefaultTableModel) attributesTable.getModel();
                Preferences attrPrefs = prefs.node("attributes");
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    attr.put((String) dtm.getValueAt(i, 0), (String) dtm.getValueAt(i, 1));
                    attrPrefs.put((String) dtm.getValueAt(i, 0), (String) dtm.getValueAt(i, 1));
                }
                NameParser np = dirContext.getNameParser("");
                Enumeration comps = new ResourceName(prototypeNameField.getText()).getAll();
                prototypeNameField.recordText();
                Name resourcedn = np.parse("");
                while (comps.hasMoreElements()) {
                    resourcedn.add(RC + "=" + (String) comps.nextElement());
                    try {
                        dirContext.createSubcontext(resourcedn);
                    } catch (NameAlreadyBoundException nabe) {
                    }
                }
                dirContext.modifyAttributes(resourcedn, DirContext.REPLACE_underscoreATTRIBUTE, attr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e, "Error Posting Metadata", JOptionPane.ERROR_underscoreMESSAGE);
            }
        }
    }

