    public void launch(String xmlControl, String xmlDoc, long docId) {
        AgentLauncher l;
        Environment env;
        Properties prop;
        Resource res;
        String token;
        String deflt;
        String answ;
        String key;
        String entry;
        ShipService service;
        de.fhg.igd.util.URL url;
        java.net.URL wsurl;
        NodeList flow;
        InputSource xmlcontrolstream;
        TreeMap results;
        synchronized (lock_underscore) {
            if (xmlControl == null || xmlControl.length() == 0 || xmlDoc == null || xmlDoc.length() == 0) {
                System.out.println("---- Need control AND XML document! ----");
                return;
            }
            Vector v_underscoredelegations_underscorehost = new Vector();
            Vector v_underscoredelegations_underscoreurl = new Vector();
            Vector v_underscoredelegations_underscoremethod = new Vector();
            xmlcontrolstream = new InputSource(new StringReader(xmlControl));
            NodeList destinations = SimpleXMLParser.parseDocument(xmlcontrolstream, AgentBehaviour.XML_underscoreDELEGATE);
            for (int i = 0; i < destinations.getLength(); i++) {
                if (destinations.item(i).getTextContent() != null && destinations.item(i).getTextContent().length() > 0) {
                    System.out.println(destinations.item(i).getTextContent());
                    entry = SimpleXMLParser.findChildEntry(destinations.item(i), AgentBehaviour.XML_underscoreHOST);
                    v_underscoredelegations_underscorehost.add(entry);
                    entry = SimpleXMLParser.findChildEntry(destinations.item(i), AgentBehaviour.XML_underscoreURL);
                    v_underscoredelegations_underscoreurl.add(entry);
                    entry = SimpleXMLParser.findChildEntry(destinations.item(i), AgentBehaviour.XML_underscoreMETHOD);
                    v_underscoredelegations_underscoremethod.add(entry);
                }
            }
            token = "";
            results = new TreeMap();
            for (int i = 0; i < TOKEN_underscoreLENGTH; i++) {
                token = token + (char) (Math.random() * 26 + 65);
            }
            results.put(token, null);
            prop = AgentStructure.defaults();
            prop.setProperty(AgentStructure.PROP_underscoreAGENT_underscoreCLASS, AGENT_underscore);
            prop.setProperty(AgentBehaviour.CTX_underscoreDOCID, String.valueOf(docId));
            prop.setProperty(AgentBehaviour.CTX_underscoreXML, xmlDoc);
            prop.setProperty("token", token);
            deflt = prop.getProperty(AgentStructure.PROP_underscoreAGENT_underscoreEXCLUDE);
            prop.setProperty(AgentStructure.PROP_underscoreAGENT_underscoreEXCLUDE, deflt + ":" + ADDITIONAL_underscoreEXCLUDES);
            service = (ShipService) getEnvironment().lookup(WhatIs.stringValue(ShipService.WHATIS));
            for (int i = 0; i < v_underscoredelegations_underscorehost.size(); i++) {
                System.out.println("\n-----SCANNING DELEGATES-----");
                System.out.println("\n-----DELEGATE " + i + "-----");
                System.out.println("-----HOST: " + i + ": " + (String) v_underscoredelegations_underscorehost.elementAt(i));
                System.out.println("-----URL: " + i + ": " + (String) v_underscoredelegations_underscoreurl.elementAt(i));
                System.out.println("-----METHOD: " + i + ": " + (String) v_underscoredelegations_underscoremethod.elementAt(i));
                try {
                    url = new de.fhg.igd.util.URL((String) v_underscoredelegations_underscorehost.elementAt(i));
                    boolean alive = service.isAlive(url);
                    System.out.println("-----ALIVE: " + alive);
                    if (alive) {
                        wsurl = new java.net.URL((String) v_underscoredelegations_underscoreurl.elementAt(i));
                        try {
                            wsurl.openStream();
                            System.out.println("-----WEBSERVICE: ON");
                            if (!prop.containsKey(0 + "." + AgentBehaviour.XML_underscoreURL)) {
                                System.out.println("-----MIGRATION: First online host found. I will migrate here:)!");
                                prop.setProperty(0 + "." + AgentBehaviour.XML_underscoreHOST, (String) v_underscoredelegations_underscorehost.elementAt(i));
                                prop.setProperty(0 + "." + AgentBehaviour.XML_underscoreURL, (String) v_underscoredelegations_underscoreurl.elementAt(i));
                                prop.setProperty(0 + "." + AgentBehaviour.XML_underscoreMETHOD, (String) v_underscoredelegations_underscoremethod.elementAt(i));
                            } else {
                                System.out.println("-----MIGRATION: I will not migrate here:(!");
                            }
                        } catch (IOException ex) {
                            System.out.println("-----WEBSERVICE: Could not connect to the webservice!");
                            System.out.println("-----MIGRATION: WEBSERVICE NOT FOUND! I will not migrate here:(!");
                        }
                    }
                } catch (ShipException she) {
                    System.out.println("-----ALIVE: false");
                    System.out.println("-----MIGRATION: HOST NOT FOUND! I will not migrate here:(!");
                } catch (SecurityException see) {
                    System.out.println("-----EXCEPTION: Access connection to remote SHIP service fails! " + "No proper ShipPermission permission to invoke lookups! " + "Ignoring this host....");
                } catch (MalformedURLException murle) {
                    System.out.println("-----EXCEPTION: The host URL is not valid! Ignoring this host....");
                }
            }
            res = new MemoryResource();
            env = Environment.getEnvironment();
            key = WhatIs.stringValue(AgentLauncher.WHATIS);
            l = (AgentLauncher) env.lookup(key);
            if (l == null) {
                System.out.println("Can't find the agent launcher");
                return;
            }
            try {
                l.launchAgent(res, prop);
            } catch (IllegalAgentException ex) {
                System.out.println(ex);
            } catch (GeneralSecurityException ex) {
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            }
            syncmap_underscore.put(token, results);
            System.out.println("----- TOKEN = " + token + "------");
        }
        try {
            synchronized (token) {
                token.wait(TIMEOUT);
                Map m_underscoreresults = (Map) syncmap_underscore.get(token);
                Collection c_underscoreresults = m_underscoreresults.values();
                String[] sa_underscoreresults = (String[]) c_underscoreresults.toArray(new String[0]);
                answ = "";
                for (int j = 0; j < sa_underscoreresults.length; j++) {
                    answ = answ + sa_underscoreresults[j];
                }
                syncmap_underscore.remove(token);
                System.out.println("----- " + answ + " -----");
                callbackWS(xmlControl, answ, docId);
            }
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }

