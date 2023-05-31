    public void signAndSend() throws Exception {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
            sslContext.init(null, xtmArray, new java.security.SecureRandom());
        } catch (GeneralSecurityException gse) {
            this.addException("GeneralSecurityException", gse);
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
        String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());
        DigestMethod dm = fac.newDigestMethod(DigestMethod.SHA1, null);
        List transforms = new Vector(2);
        transforms.add(fac.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", (TransformParameterSpec) null));
        List prefixlist = new Vector(1);
        prefixlist.add("xsd");
        transforms.add(fac.newTransform("http://www.w3.org/2001/10/xml-exc-c14n#", new ExcC14NParameterSpec(prefixlist)));
        Random randgen = new Random();
        byte[] rand_underscorebytes = new byte[20];
        randgen.nextBytes(rand_underscorebytes);
        String assertion_underscoreid_underscorestr = "i" + new String(Hex.encodeHex(rand_underscorebytes));
        Reference ref = fac.newReference("#" + assertion_underscoreid_underscorestr, dm, transforms, null, null);
        CanonicalizationMethod cm = fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null);
        SignatureMethod sm = fac.newSignatureMethod(SignatureMethod.RSA_underscoreSHA1, null);
        SignedInfo si = fac.newSignedInfo(cm, sm, Collections.singletonList(ref));
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = null;
        if (TEST_underscoreSIGNED_underscoreWITH_underscoreWRONG_underscoreCERT == testNumber) {
            fis = new FileInputStream(resourceFolder + "z-xtra-sign.jks");
        } else {
            fis = new FileInputStream(resourceFolder + "z-idp-sign.jks");
        }
        ks.load(fis, "changeit".toCharArray());
        {
            Enumeration aliases = ks.aliases();
            for (; aliases.hasMoreElements(); ) {
                String alias = (String) aliases.nextElement();
                boolean b = ks.isKeyEntry(alias);
                b = ks.isCertificateEntry(alias);
                System.out.println(b + " " + alias);
            }
        }
        PrivateKey privateKey = (PrivateKey) ks.getKey("tomcat", "changeit".toCharArray());
        XMLSignature signature = fac.newXMLSignature(si, null);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        File docFile = new File(resourceFolder + "BaseRequest.xml");
        Document doc = db.parse(docFile);
        Element root = doc.getDocumentElement();
        NamedNodeMap root_underscoreatts = root.getAttributes();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        GregorianCalendar right_underscorenow = new GregorianCalendar();
        if (TEST_underscoreNOT_underscoreON_underscoreOR_underscoreAFTER_underscoreEXPIRED == testNumber) {
            right_underscorenow.add(Calendar.MINUTE, alterNowDateBy);
        }
        Date issue_underscoredate = right_underscorenow.getTime();
        right_underscorenow.add(Calendar.MINUTE, -10);
        Date auth_underscoreinstant_underscoredate = right_underscorenow.getTime();
        right_underscorenow.add(Calendar.MINUTE, 20);
        Date not_underscoreon_underscoreor_underscoreafter_underscoredate = right_underscorenow.getTime();
        System.out.println("Not on or after 1: " + sdf.format(right_underscorenow.getTime()));
        Node response_underscoreid = root_underscoreatts.getNamedItem("ID");
        randgen.nextBytes(rand_underscorebytes);
        response_underscoreid.setNodeValue("i" + new String(Hex.encodeHex(rand_underscorebytes)));
        Node response_underscoreissue_underscoreinstant = root_underscoreatts.getNamedItem("IssueInstant");
        response_underscoreissue_underscoreinstant.setNodeValue(sdf.format(issue_underscoredate));
        NodeList tmp_underscorenlist = root.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion");
        Element assertion_underscorenode = (Element) tmp_underscorenlist.item(0);
        NamedNodeMap ass_underscorenode_underscoreatts = assertion_underscorenode.getAttributes();
        Node assertion_underscoreid = ass_underscorenode_underscoreatts.getNamedItem("ID");
        assertion_underscoreid.setNodeValue(assertion_underscoreid_underscorestr);
        Node assertion_underscoreissue_underscoreinstant = ass_underscorenode_underscoreatts.getNamedItem("IssueInstant");
        assertion_underscoreissue_underscoreinstant.setNodeValue(sdf.format(issue_underscoredate));
        tmp_underscorenlist = assertion_underscorenode.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Subject");
        Element subject_underscorenode = (Element) tmp_underscorenlist.item(0);
        if (TEST_underscoreUNKNOWN_underscoreCONFIRMATION == testNumber) {
            tmp_underscorenlist = subject_underscorenode.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "SubjectConfirmation");
            Element subj_underscoreconf_underscorenode = (Element) tmp_underscorenlist.item(0);
            NamedNodeMap subj_underscoreconf_underscorenode_underscoreatts = subj_underscoreconf_underscorenode.getAttributes();
            Node method_underscorenode = subj_underscoreconf_underscorenode_underscoreatts.getNamedItem("Method");
            method_underscorenode.setNodeValue(badConfirmationMethod);
        }
        tmp_underscorenlist = subject_underscorenode.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "NameID");
        Element name_underscoreid_underscorenode = (Element) tmp_underscorenlist.item(0);
        NamedNodeMap name_underscoreid_underscorenode_underscoreatts = name_underscoreid_underscorenode.getAttributes();
        Node sp_underscorename_underscorequalifier = name_underscoreid_underscorenode_underscoreatts.getNamedItem("SPNameQualifier");
        sp_underscorename_underscorequalifier.setNodeValue(sPEntityId);
        Node name_underscoreid_underscorevalue = name_underscoreid_underscorenode.getFirstChild();
        randgen.nextBytes(rand_underscorebytes);
        name_underscoreid_underscorevalue.setNodeValue(new String(Hex.encodeHex(rand_underscorebytes)));
        tmp_underscorenlist = subject_underscorenode.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "SubjectConfirmationData");
        Element subj_underscoreconf_underscoredata_underscorenode = (Element) tmp_underscorenlist.item(0);
        NamedNodeMap subj_underscoreconf_underscoredata_underscorenode_underscoreatts = subj_underscoreconf_underscoredata_underscorenode.getAttributes();
        Node not_underscoreon_underscoreor_underscoreafter_underscorenode = subj_underscoreconf_underscoredata_underscorenode_underscoreatts.getNamedItem("NotOnOrAfter");
        not_underscoreon_underscoreor_underscoreafter_underscorenode.setNodeValue(sdf.format(not_underscoreon_underscoreor_underscoreafter_underscoredate));
        Node recipient_underscorenode = subj_underscoreconf_underscoredata_underscorenode_underscoreatts.getNamedItem("Recipient");
        if (TEST_underscoreWRONG_underscoreRECIPIENT == testNumber) {
            recipient_underscorenode.setNodeValue(badRecipientValue);
        } else {
            recipient_underscorenode.setNodeValue(sPAssertionConsumerService);
        }
        tmp_underscorenlist = assertion_underscorenode.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Audience");
        Element audience_underscorenode = (Element) tmp_underscorenlist.item(0);
        Node audience_underscorevalue = audience_underscorenode.getFirstChild();
        if (TEST_underscoreWRONG_underscoreAUDIENCE == testNumber) {
            audience_underscorevalue.setNodeValue(badAudienceValue);
        } else {
            audience_underscorevalue.setNodeValue(sPEntityId);
        }
        tmp_underscorenlist = assertion_underscorenode.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "AuthnStatement");
        Element authn_underscorestatement_underscorenode = (Element) tmp_underscorenlist.item(0);
        NamedNodeMap authn_underscorestatement_underscorenode_underscoreatts = authn_underscorestatement_underscorenode.getAttributes();
        Node authn_underscoreinstant_underscorenode = authn_underscorestatement_underscorenode_underscoreatts.getNamedItem("AuthnInstant");
        authn_underscoreinstant_underscorenode.setNodeValue(sdf.format(auth_underscoreinstant_underscoredate));
        Node sess_underscoreidx_underscorenode = authn_underscorestatement_underscorenode_underscoreatts.getNamedItem("SessionIndex");
        sess_underscoreidx_underscorenode.setNodeValue(assertion_underscoreid_underscorestr);
        DOMSignContext signContext = new DOMSignContext(privateKey, assertion_underscorenode, subject_underscorenode);
        signContext.putNamespacePrefix("http://www.w3.org/2000/09/xmldsig#", "ds");
        signContext.putNamespacePrefix("http://www.w3.org/2001/10/xml-exc-c14n#", "ec");
        signature.sign(signContext);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans;
        if (TEST_underscoreDATA_underscoreALTERED_underscoreAFTER_underscoreSIG == testNumber) {
            right_underscorenow.add(Calendar.MINUTE, 10);
            System.out.println("Not on or after: " + sdf.format(right_underscorenow.getTime()));
            not_underscoreon_underscoreor_underscoreafter_underscorenode.setNodeValue(sdf.format(right_underscorenow.getTime()));
        }
        trans = tf.newTransformer();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        trans.transform(new DOMSource(doc), new StreamResult(pw));
        if (useJavaPOST) {
            try {
                URL url = new URL(sPAssertionConsumerService);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setInstanceFollowRedirects(false);
                conn.setUseCaches(false);
                String base64ofDoc = Base64.encode(sw.toString().getBytes());
                DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
                String content = "SAMLResponse=" + URLEncoder.encode(base64ofDoc, "UTF-8");
                printout.writeBytes(content);
                printout.flush();
                printout.close();
                if (TEST_underscoreGOOD_underscoreREPLAY == testNumber) {
                    base64Assertion = Base64.encode(sw.toString().getBytes());
                    replay = true;
                }
                BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String redirect = conn.getHeaderField("Location");
                if (redirect != null) {
                    input.close();
                    URL url2 = new URL(redirect);
                    URLConnection conn2 = url2.openConnection();
                    String cookie = conn.getHeaderField("Set-Cookie");
                    if (cookie != null) {
                        int index = cookie.indexOf(";");
                        if (index >= 0) cookie = cookie.substring(0, index);
                        conn2.setRequestProperty("Cookie", cookie);
                    }
                    input = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                }
                StringBuffer buff = new StringBuffer();
                String str2;
                while (null != ((str2 = input.readLine()))) {
                    buff.append(str2);
                }
                input.close();
                result = buff.toString();
                success = true;
            } catch (MalformedURLException me) {
                this.addException("MalformedURLException", me);
            } catch (IOException ioe) {
                this.addException("IOException", ioe);
            }
        } else {
            base64Assertion = Base64.encode(sw.toString().getBytes());
            Transformer transPretty = tf.newTransformer(new StreamSource(resourceFolder + "PrettyPrint.xslt"));
            StringWriter swPretty = new StringWriter();
            PrintWriter pwPretty = new PrintWriter(sw);
            trans.transform(new DOMSource(doc), new StreamResult(pwPretty));
            prettyAssertion = sw.toString();
            System.out.println(XMLHelper.prettyPrintXML(doc.getFirstChild()));
            success = true;
        }
    }

