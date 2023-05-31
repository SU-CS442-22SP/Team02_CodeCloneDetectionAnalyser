    public String identify(String baseURL) throws OAIException {
        PrefixResolverDefault prefixResolver;
        XPath xpath;
        XPathContext xpathSupport;
        int ctxtNode;
        XObject list;
        Node node;
        boolean v2 = false;
        priCheckBaseURL();
        try {
            URL url = new URL(baseURL + "?verb=Identify");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http = frndTrySend(http);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            if (validation == VALIDATION_underscoreVERY_underscoreSTRICT) {
                docFactory.setValidating(true);
            } else {
                docFactory.setValidating(false);
            }
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document xml = null;
            try {
                xml = docBuilder.parse(http.getInputStream());
            } catch (IllegalArgumentException iae) {
                throw new OAIException(OAIException.CRITICAL_underscoreERR, iae.getMessage());
            } catch (SAXException se) {
                if (validation != VALIDATION_underscoreLOOSE) {
                    throw new OAIException(OAIException.XML_underscorePARSE_underscoreERR, se.getMessage());
                } else {
                    try {
                        url = new URL(baseURL + "?verb=Identify");
                        http.disconnect();
                        http = (HttpURLConnection) url.openConnection();
                        http = frndTrySend(http);
                        xml = docBuilder.parse(priCreateDummyIdentify(http.getInputStream()));
                    } catch (SAXException se2) {
                        throw new OAIException(OAIException.XML_underscorePARSE_underscoreERR, se2.getMessage());
                    }
                }
            }
            try {
                descrNamespaceNode = xml.createElement("Identify");
                descrNamespaceNode.setAttribute("xmlns:oai_underscoreid", XMLNS_underscoreOAI + "Identify");
                descrNamespaceNode.setAttribute("xmlns:id", XMLNS_underscoreID);
                descrNamespaceNode.setAttribute("xmlns:epr", XMLNS_underscoreEPR);
                prefixResolver = new PrefixResolverDefault(descrNamespaceNode);
                xpathSupport = new XPathContext();
                ctxtNode = xpathSupport.getDTMHandleFromNode(xml);
                xpath = new XPath("/oai_underscoreid:Identify", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                node = list.nodeset().nextNode();
                if (node == null) {
                    descrNamespaceNode.setAttribute("xmlns:oai_underscoreid", XMLNS_underscoreOAI_underscore2_underscore0);
                    descrNamespaceNode.setAttribute("xmlns:id", XMLNS_underscoreID_underscore2_underscore0);
                    descrNamespaceNode.setAttribute("xmlns:epr", XMLNS_underscoreEPR);
                    prefixResolver = new PrefixResolverDefault(descrNamespaceNode);
                    xpath = new XPath("/oai_underscoreid:OAI-PMH", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    node = list.nodeset().nextNode();
                    if (node == null) {
                        descrNamespaceNode.setAttribute("xmlns:oai_underscoreid", XMLNS_underscoreOAI_underscore1_underscore0 + "Identify");
                        descrNamespaceNode.setAttribute("xmlns:id", XMLNS_underscoreID_underscore1_underscore0);
                        descrNamespaceNode.setAttribute("xmlns:epr", XMLNS_underscoreEPR_underscore1_underscore0);
                        prefixResolver = new PrefixResolverDefault(descrNamespaceNode);
                    } else {
                        xpath = new XPath("oai_underscoreid:OAI-PMH/oai_underscoreid:error", null, prefixResolver, XPath.SELECT, null);
                        list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                        ixmlErrors = list.nodelist();
                        if (getLastOAIErrorCount() > 0) {
                            strProtocolVersion = "2";
                            throw new OAIException(OAIException.OAI_underscoreERR, getLastOAIError().getCode() + ": " + getLastOAIError().getReason());
                        }
                        v2 = true;
                    }
                }
                xpath = new XPath("//oai_underscoreid:repositoryName", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                node = list.nodeset().nextNode();
                if (node != null) {
                    strRepositoryName = node.getFirstChild().getNodeValue();
                } else {
                    if (validation == VALIDATION_underscoreLOOSE) {
                        strRepositoryName = "UNKNOWN";
                    } else {
                        throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "Identify missing repositoryName");
                    }
                }
                xpath = new XPath("//oai_underscoreid:baseURL", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                node = list.nodeset().nextNode();
                if (node != null) {
                    strBaseURL = node.getFirstChild().getNodeValue();
                } else {
                    if (validation != VALIDATION_underscoreLOOSE) {
                        throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "Identify missing baseURL");
                    }
                }
                xpath = new XPath("//oai_underscoreid:protocolVersion", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                node = list.nodeset().nextNode();
                if (node != null) {
                    strProtocolVersion = node.getFirstChild().getNodeValue();
                } else {
                    if (validation == VALIDATION_underscoreLOOSE) {
                        strProtocolVersion = "UNKNOWN";
                    } else {
                        throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "Identify missing protocolVersion");
                    }
                }
                xpath = new XPath("//oai_underscoreid:adminEmail", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                NodeList nl = list.nodelist();
                if (nl.getLength() > 0) {
                    strAdminEmail = new String[nl.getLength()];
                    for (int i = 0; i < nl.getLength(); i++) {
                        strAdminEmail[i] = nl.item(i).getFirstChild().getNodeValue();
                    }
                } else {
                    if (validation == VALIDATION_underscoreLOOSE) {
                        strAdminEmail = new String[1];
                        strAdminEmail[0] = "mailto:UNKNOWN";
                    } else {
                        throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "Identify missing adminEmail");
                    }
                }
                if (v2) {
                    xpath = new XPath("//oai_underscoreid:earliestDatestamp", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    node = list.nodeset().nextNode();
                    if (node != null) {
                        strEarliestDatestamp = node.getFirstChild().getNodeValue();
                    } else {
                        if (validation == VALIDATION_underscoreLOOSE) {
                            strEarliestDatestamp = "UNKNOWN";
                        } else {
                            throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "Identify missing earliestDatestamp");
                        }
                    }
                    xpath = new XPath("//oai_underscoreid:deletedRecord", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    node = list.nodeset().nextNode();
                    if (node != null) {
                        strDeletedRecord = node.getFirstChild().getNodeValue();
                    } else {
                        if (validation == VALIDATION_underscoreLOOSE) {
                            strDeletedRecord = "UNKNOWN";
                        } else {
                            throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "Identify missing deletedRecordp");
                        }
                    }
                    xpath = new XPath("//oai_underscoreid:granularity", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    node = list.nodeset().nextNode();
                    if (node != null) {
                        strGranularity = node.getFirstChild().getNodeValue();
                    } else {
                        if (validation == VALIDATION_underscoreLOOSE) {
                            strGranularity = "UNKNOWN";
                        } else {
                            throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "Identify missing granularity");
                        }
                    }
                    xpath = new XPath("//oai_underscoreid:compression", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    nl = list.nodelist();
                    if (nl.getLength() > 0) {
                        strCompression = new String[nl.getLength()];
                        for (int i = 0; i < nl.getLength(); i++) {
                            strCompression[i] = nl.item(i).getFirstChild().getNodeValue();
                        }
                    }
                }
                xpath = new XPath("//oai_underscoreid:description", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                ixmlDescriptions = list.nodelist();
                xpath = new XPath("//oai_underscoreid:responseDate", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                node = list.nodeset().nextNode();
                if (node != null) {
                    strResponseDate = node.getFirstChild().getNodeValue();
                } else {
                    if (validation == VALIDATION_underscoreLOOSE) {
                        strResponseDate = "";
                    } else {
                        throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "GetRecord missing responseDate");
                    }
                }
                xpath = new XPath("//oai_underscoreid:requestURL | //oai_underscoreid:request", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                node = list.nodeset().nextNode();
                if (node != null) {
                    ixmlRequest = node;
                } else {
                    if (validation == VALIDATION_underscoreLOOSE) {
                        ixmlRequest = null;
                    } else {
                        throw new OAIException(OAIException.INVALID_underscoreRESPONSE_underscoreERR, "GetRecord missing requestURL");
                    }
                }
                state = STATE_underscoreIDENTIFIED;
                xpath = null;
                prefixResolver = null;
                xpathSupport = null;
                list = null;
            } catch (TransformerException te) {
                throw new OAIException(OAIException.CRITICAL_underscoreERR, te.getMessage());
            }
            url = null;
            docFactory = null;
            docBuilder = null;
        } catch (IOException ie) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, ie.getMessage());
        } catch (FactoryConfigurationError fce) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, fce.getMessage());
        } catch (ParserConfigurationException pce) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, pce.getMessage());
        }
        return strRepositoryName;
    }

