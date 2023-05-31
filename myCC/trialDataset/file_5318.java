    private void modifyDialog(boolean fileExists) {
        if (fileExists) {
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_underscoreREVOCATION_underscoreLOCATION)) {
                RevLocation = ((String) vars.get(EnvironmentalVariables.WEBDAV_underscoreREVOCATION_underscoreLOCATION));
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_underscoreCERTIFICATE_underscoreLOCATION)) {
                CertLocation = ((String) vars.get(EnvironmentalVariables.WEBDAV_underscoreCERTIFICATE_underscoreLOCATION));
            }
            if (vars.containsKey(EnvironmentalVariables.HOLDER_underscoreNAME_underscoreSTRING)) {
                jHolderName.setText((String) vars.get(EnvironmentalVariables.HOLDER_underscoreNAME_underscoreSTRING));
            } else jHolderName.setText("<EMPTY>");
            if (vars.containsKey(EnvironmentalVariables.LDAP_underscoreHOLDER_underscoreEDITOR_underscoreUTILITY)) {
                if (vars.containsKey(EnvironmentalVariables.HOLDER_underscoreEDITOR_underscoreUTILITY_underscoreSERVER)) {
                    jProviderURL.setText((String) vars.get(EnvironmentalVariables.HOLDER_underscoreEDITOR_underscoreUTILITY_underscoreSERVER));
                }
            }
            if (vars.containsKey(EnvironmentalVariables.SERIAL_underscoreNUMBER_underscoreSTRING)) {
                serialNumber = (String) vars.get(EnvironmentalVariables.SERIAL_underscoreNUMBER_underscoreSTRING);
            } else serialNumber = "<EMPTY>";
            if (vars.containsKey(EnvironmentalVariables.VALIDITY_underscorePERIOD_underscoreSTRING)) {
                jValidityPeriod.setText((String) vars.get(EnvironmentalVariables.VALIDITY_underscorePERIOD_underscoreSTRING));
            } else jValidityPeriod.setText("<EMPTY>");
            if (vars.containsKey(LDAPSavingUtility.LDAP_underscoreSAVING_underscoreUTILITY_underscoreAC_underscoreTYPE)) {
                String acType = (String) vars.get(LDAPSavingUtility.LDAP_underscoreSAVING_underscoreUTILITY_underscoreAC_underscoreTYPE);
                if ((!acType.equals("")) && (!acType.equals("<EMPTY>"))) jACType.setText((String) vars.get(LDAPSavingUtility.LDAP_underscoreSAVING_underscoreUTILITY_underscoreAC_underscoreTYPE)); else jACType.setText("attributeCertificateAttribute");
            }
            if (utils.containsKey("issrg.acm.extensions.SimpleSigningUtility")) {
                if (vars.containsKey(DefaultSecurity.DEFAULT_underscoreFILE_underscoreSTRING)) {
                    jDefaultProfile.setText((String) vars.get(DefaultSecurity.DEFAULT_underscoreFILE_underscoreSTRING));
                } else jDefaultProfile.setText("<EMPTY>");
                jCHEntrust.setSelected(true);
            } else {
                jCHEntrust.setSelected(false);
                jDefaultProfile.setEnabled(false);
            }
            if (utils.containsKey("issrg.acm.extensions.ACMDISSigningUtility")) {
                if (vars.containsKey("DefaultDIS")) {
                    jDISAddress.setText((String) vars.get("DefaultDIS"));
                } else jDISAddress.setText("<EMPTY>");
                jDIS.setSelected(true);
                jCHEntrust.setSelected(true);
                jDefaultProfile.setEnabled(true);
                if (vars.containsKey(DefaultSecurity.DEFAULT_underscoreFILE_underscoreSTRING)) {
                    jDefaultProfile.setText((String) vars.get(DefaultSecurity.DEFAULT_underscoreFILE_underscoreSTRING));
                } else jDefaultProfile.setText("permis.p12");
            } else {
                jDIS.setSelected(false);
                jDISAddress.setEnabled(false);
            }
            if (vars.containsKey(EnvironmentalVariables.AAIA_underscoreLOCATION)) {
                jaaia[0].setSelected(true);
            }
            if (vars.containsKey(EnvironmentalVariables.NOREV_underscoreLOCATION)) {
                jnorev[0].setSelected(true);
                jdavrev[0].setEnabled(false);
                jdavrev[1].setEnabled(false);
                jdavrev[1].setSelected(false);
            }
            if (vars.containsKey(EnvironmentalVariables.DAVREV_underscoreLOCATION)) {
                jdavrev[0].setSelected(true);
                jnorev[0].setEnabled(false);
                jnorev[1].setEnabled(false);
                jnorev[1].setSelected(true);
            }
            if (vars.containsKey("LDAPSavingUtility.ProviderURI")) {
                jProviderURL.setText((String) vars.get("LDAPSavingUtility.ProviderURI"));
            } else jProviderURL.setText("<EMPTY>");
            if (vars.containsKey("LDAPSavingUtility.Login")) {
                jProviderLogin.setText((String) vars.get("LDAPSavingUtility.Login"));
            } else jProviderLogin.setText("<EMPTY>");
            if (vars.containsKey("LDAPSavingUtility.Password")) {
                jProviderPassword.setText((String) vars.get("LDAPSavingUtility.Password"));
            } else jProviderPassword.setText("<EMPTY>");
            if ((!vars.containsKey(EnvironmentalVariables.TRUSTSTORE)) || (((String) vars.get(EnvironmentalVariables.TRUSTSTORE)).equals(""))) {
                vars.put(EnvironmentalVariables.TRUSTSTORE, "truststorefile");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_underscoreHOST)) {
                jWebDAVHost.setText((String) vars.get(EnvironmentalVariables.WEBDAV_underscoreHOST));
            } else {
                jWebDAVHost.setText("<EMPTY>");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_underscorePORT)) {
                jWebDAVPort.setText((String) vars.get(EnvironmentalVariables.WEBDAV_underscorePORT));
            } else {
                jWebDAVPort.setText("<EMPTY>");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_underscorePROTOCOL)) {
                if (vars.get(EnvironmentalVariables.WEBDAV_underscorePROTOCOL).equals("HTTPS")) {
                    jWebDAVHttps.setSelected(true);
                    jWebDAVSelectP12.setEnabled(true);
                    jWebDAVP12Filename.setEnabled(true);
                    jWebDAVP12Password.setEnabled(true);
                    jWebDAVSSL.setEnabled(true);
                    addWebDAVSSL.setEnabled(true);
                } else {
                    jWebDAVHttps.setSelected(false);
                    jWebDAVSelectP12.setEnabled(false);
                    jWebDAVP12Filename.setEnabled(false);
                    jWebDAVP12Password.setEnabled(false);
                    jWebDAVSSL.setEnabled(false);
                    addWebDAVSSL.setEnabled(false);
                }
            } else {
                jWebDAVHttps.setSelected(false);
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_underscoreP12FILENAME)) {
                jWebDAVP12Filename.setText((String) vars.get(EnvironmentalVariables.WEBDAV_underscoreP12FILENAME));
            } else {
                jWebDAVP12Filename.setText("<EMPTY>");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_underscoreP12PASSWORD)) {
                jWebDAVP12Password.setText((String) vars.get(EnvironmentalVariables.WEBDAV_underscoreP12PASSWORD));
            } else {
                jWebDAVP12Password.setText("<EMPTY>");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_underscoreSSLCERTIFICATE)) {
                jWebDAVSSL.setText((String) vars.get(EnvironmentalVariables.WEBDAV_underscoreSSLCERTIFICATE));
            } else {
                jWebDAVSSL.setText("<EMPTY>");
            }
        } else {
            jHolderName.setText("cn=A Permis Test User, o=PERMIS, c=gb");
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(new Date().toString().getBytes());
                byte[] result = md.digest();
                BigInteger bi = new BigInteger(result);
                bi = bi.abs();
                serialNumber = bi.toString(16);
            } catch (Exception e) {
                serialNumber = "<EMPTY>";
            }
            jValidityPeriod.setText("<EMPTY>");
            jDefaultProfile.setText("permis.p12");
            jCHEntrust.setSelected(true);
            jProviderURL.setText("ldap://sec.cs.kent.ac.uk/c=gb");
            jProviderLogin.setText("");
            jProviderPassword.setText("");
            jWebDAVHost.setText("");
            jWebDAVPort.setText("443");
            jWebDAVP12Filename.setText("");
            jACType.setText("attributeCertificateAttribute");
            vars.put(EnvironmentalVariables.TRUSTSTORE, "truststorefile");
            saveChanges();
        }
    }

