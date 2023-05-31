    public OAIRecord getRecord(String identifier, String metadataPrefix) throws OAIException {
        PrefixResolverDefault prefixResolver;
        XPath xpath;
        XPathContext xpathSupport;
        int ctxtNode;
        XObject list;
        Node node;
        OAIRecord rec = new OAIRecord();
        priCheckBaseURL();
        String params = priBuildParamString("", "", "", identifier, metadataPrefix);
        try {
            URL url = new URL(strBaseURL + "?verb=GetRecord" + params);
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
                rec.frndSetValid(true);
            } catch (IllegalArgumentException iae) {
                throw new OAIException(OAIException.CRITICAL_underscoreERR, iae.getMessage());
            } catch (SAXException se) {
                if (validation != VALIDATION_underscoreLOOSE) {
                    throw new OAIException(OAIException.XML_underscorePARSE_underscoreERR, se.getMessage());
                } else {
                    try {
                        url = new URL(strBaseURL + "?verb=GetRecord" + params);
                        http.disconnect();
                        http = (HttpURLConnection) url.openConnection();
                        http = frndTrySend(http);
                        xml = docBuilder.parse(priCreateDummyGetRecord(identifier, http.getInputStream()));
                        rec.frndSetValid(false);
                    } catch (SAXException se2) {
                        throw new OAIException(OAIException.XML_underscorePARSE_underscoreERR, se2.getMessage());
                    }
                }
            }
            try {
                namespaceNode = xml.createElement("GetRecord");
                namespaceNode.setAttribute("xmlns:oai", XMLNS_underscoreOAI + "GetRecord");
                namespaceNode.setAttribute("xmlns:dc", XMLNS_underscoreDC);
                prefixResolver = new PrefixResolverDefault(namespaceNode);
                xpath = new XPath("/oai:GetRecord/oai:record", null, prefixResolver, XPath.SELECT, null);
                xpathSupport = new XPathContext();
                ctxtNode = xpathSupport.getDTMHandleFromNode(xml);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                node = list.nodeset().nextNode();
                if (node == null) {
                    namespaceNode.setAttribute("xmlns:oai", XMLNS_underscoreOAI_underscore2_underscore0);
                    prefixResolver = new PrefixResolverDefault(namespaceNode);
                    xpath = new XPath("/oai:OAI-PMH/oai:GetRecord/oai:record", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    node = list.nodeset().nextNode();
                    if (node == null) {
                        namespaceNode.setAttribute("xmlns:oai", XMLNS_underscoreOAI_underscore1_underscore0 + "GetRecord");
                        prefixResolver = new PrefixResolverDefault(namespaceNode);
                        xpath = new XPath("/oai:GetRecord/oai:record", null, prefixResolver, XPath.SELECT, null);
                        list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                        node = list.nodeset().nextNode();
                    } else {
                        xpath = new XPath("oai:OAI-PMH/oai:error", null, prefixResolver, XPath.SELECT, null);
                        list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                        ixmlErrors = list.nodelist();
                        if (ixmlErrors.getLength() > 0) {
                            strProtocolVersion = "2";
                            throw new OAIException(OAIException.OAI_underscoreERR, getLastOAIError().getCode() + ": " + getLastOAIError().getReason());
                        }
                    }
                }
                if (node != null) {
                    rec.frndSetRepository(this);
                    rec.frndSetMetadataPrefix(metadataPrefix);
                    rec.frndSetIdOnly(false);
                    ctxtNode = xpathSupport.getDTMHandleFromNode(node);
                    xpath = new XPath("//oai:header/oai:identifier", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    rec.frndSetIdentifier(list.nodeset().nextNode().getFirstChild().getNodeValue());
                    xpath = new XPath("//oai:header/oai:datestamp", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    rec.frndSetDatestamp(list.nodeset().nextNode().getFirstChild().getNodeValue());
                    rec.frndSetRecord(node);
                    NamedNodeMap nmap = node.getAttributes();
                    if (nmap != null) {
                        if (nmap.getNamedItem("status") != null) {
                            rec.frndSetStatus(nmap.getNamedItem("status").getFirstChild().getNodeValue());
                        }
                    }
                } else {
                    rec = null;
                }
                xpath = new XPath("//oai:responseDate", null, prefixResolver, XPath.SELECT, null);
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
                xpath = new XPath("//oai:requestURL | //oai:request", null, prefixResolver, XPath.SELECT, null);
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
        } catch (MalformedURLException mue) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, mue.getMessage());
        } catch (FactoryConfigurationError fce) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, fce.getMessage());
        } catch (ParserConfigurationException pce) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, pce.getMessage());
        } catch (IOException ie) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, ie.getMessage());
        }
        return rec;
    }

