    private void initialize(OAIRepository repo, String u, String v, String params) throws OAIException {
        oParent = repo;
        strVerb = v;
        strBaseURL = u;
        strParams = params;
        strResumptionToken = "";
        iResumptionCount = 0;
        boolInitialized = false;
        boolValidResponse = false;
        iIndex = 1;
        iCount = -1;
        iCursor = -1;
        iRealCursor = -1;
        iCompleteListSize = -1;
        if (!strVerb.equals("ListIdentifiers") && !strVerb.equals("ListMetadataFormats") && !strVerb.equals("ListRecords") && !strVerb.equals("ListSets")) {
            throw new OAIException(OAIException.INVALID_underscoreVERB_underscoreERR, "Invalid verb");
        }
        if (strBaseURL.length() == 0) {
            throw new OAIException(OAIException.NO_underscoreBASE_underscoreURL_underscoreERR, "No baseURL");
        }
        if (params.length() > 0) {
            if (params.charAt(0) != '&') {
                params = "&" + params;
            }
        }
        try {
            URL url = new URL(strBaseURL + "?verb=" + strVerb + params);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http = oParent.frndTrySend(http);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            if (oParent.getValidation() == OAIRepository.VALIDATION_underscoreVERY_underscoreSTRICT) {
                docFactory.setValidating(true);
            } else {
                docFactory.setValidating(false);
            }
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            try {
                xml = docBuilder.parse(http.getInputStream());
                boolValidResponse = true;
            } catch (IllegalArgumentException iae) {
                throw new OAIException(OAIException.CRITICAL_underscoreERR, iae.getMessage());
            } catch (SAXException se) {
                if (oParent.getValidation() != OAIRepository.VALIDATION_underscoreLOOSE) {
                    throw new OAIException(OAIException.XML_underscorePARSE_underscoreERR, se.getMessage() + " Try loose validation.");
                } else {
                    try {
                        http.disconnect();
                        url = new URL(strBaseURL + "?verb=" + strVerb + params);
                        http = (HttpURLConnection) url.openConnection();
                        http = oParent.frndTrySend(http);
                        xml = docBuilder.parse(priCreateDummyResponse(http.getInputStream()));
                    } catch (SAXException se2) {
                        throw new OAIException(OAIException.XML_underscorePARSE_underscoreERR, se2.getMessage());
                    }
                }
            }
            namespaceNode = xml.createElement(strVerb);
            namespaceNode.setAttribute("xmlns:oai", OAIRepository.XMLNS_underscoreOAI + strVerb);
            namespaceNode.setAttribute("xmlns:dc", OAIRepository.XMLNS_underscoreDC);
            PrefixResolverDefault prefixResolver = new PrefixResolverDefault(namespaceNode);
            XPath xpath = new XPath("//oai:" + strVerb + "/oai:" + priGetMainNodeName(), null, prefixResolver, XPath.SELECT, null);
            XPathContext xpathSupport = new XPathContext();
            int ctxtNode = xpathSupport.getDTMHandleFromNode(xml);
            XObject list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
            Node node = list.nodeset().nextNode();
            if (node == null) {
                namespaceNode.setAttribute("xmlns:oai", OAIRepository.XMLNS_underscoreOAI_underscore2_underscore0);
                prefixResolver = new PrefixResolverDefault(namespaceNode);
                xpath = new XPath("/oai:OAI-PMH", null, prefixResolver, XPath.SELECT, null);
                list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                node = list.nodeset().nextNode();
                if (node == null) {
                    namespaceNode.setAttribute("xmlns:oai", OAIRepository.XMLNS_underscoreOAI_underscore1_underscore0 + strVerb);
                } else {
                    xpath = new XPath("oai:OAI-PMH/oai:error", null, prefixResolver, XPath.SELECT, null);
                    list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
                    NodeList nl = list.nodelist();
                    if (nl.getLength() > 0) {
                        oParent.frndSetErrors(nl);
                        throw new OAIException(OAIException.OAI_underscoreERR, oParent.getLastOAIError().getCode() + ": " + oParent.getLastOAIError().getReason());
                    }
                }
            }
            xpath = new XPath("//oai:" + strVerb + "/oai:" + priGetMainNodeName(), null, prefixResolver, XPath.SELECT, null);
            list = xpath.execute(xpathSupport, ctxtNode, prefixResolver);
            nodeList = list.nodelist();
            boolInitialized = true;
            oParent.frndSetNamespaceNode(namespaceNode);
            xpath = new XPath("//oai:requestURL | //oai:request", null, prefixResolver, XPath.SELECT, null);
            node = xpath.execute(xpathSupport, ctxtNode, prefixResolver).nodeset().nextNode();
            if (node != null) {
                oParent.frndSetRequest(node);
            }
            oParent.frndSetResponseDate(getResponseDate());
            docFactory = null;
            docBuilder = null;
            url = null;
            prefixResolver = null;
            xpathSupport = null;
            xpath = null;
        } catch (TransformerException te) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, te.getMessage());
        } catch (MalformedURLException mue) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, mue.getMessage());
        } catch (FactoryConfigurationError fce) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, fce.getMessage());
        } catch (ParserConfigurationException pce) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, pce.getMessage());
        } catch (IOException ie) {
            throw new OAIException(OAIException.CRITICAL_underscoreERR, ie.getMessage());
        }
    }

