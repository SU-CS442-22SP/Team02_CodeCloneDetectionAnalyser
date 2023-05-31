    public ICCColorSpaceExt createICCColorSpaceExt(BridgeContext ctx, Element paintedElement, String iccProfileName) {
        ICCColorSpaceExt cs = cache.request(iccProfileName.toLowerCase());
        if (cs != null) {
            return cs;
        }
        Document doc = paintedElement.getOwnerDocument();
        NodeList list = doc.getElementsByTagNameNS(SVG_underscoreNAMESPACE_underscoreURI, SVG_underscoreCOLOR_underscorePROFILE_underscoreTAG);
        int n = list.getLength();
        Element profile = null;
        for (int i = 0; i < n; i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_underscoreNODE) {
                Element profileNode = (Element) node;
                String nameAttr = profileNode.getAttributeNS(null, SVG_underscoreNAME_underscoreATTRIBUTE);
                if (iccProfileName.equalsIgnoreCase(nameAttr)) {
                    profile = profileNode;
                }
            }
        }
        if (profile == null) return null;
        String href = XLinkSupport.getXLinkHref(profile);
        ICC_underscoreProfile p = null;
        if (href != null) {
            String baseURI = ((SVGOMDocument) doc).getURL();
            ParsedURL purl = new ParsedURL(baseURI, href);
            if (!purl.complete()) throw new BridgeException(paintedElement, ERR_underscoreURI_underscoreMALFORMED, new Object[] { href });
            try {
                ParsedURL pDocURL = null;
                if (baseURI != null) {
                    pDocURL = new ParsedURL(baseURI);
                }
                ctx.getUserAgent().checkLoadExternalResource(purl, pDocURL);
                p = ICC_underscoreProfile.getInstance(purl.openStream());
            } catch (IOException e) {
                throw new BridgeException(paintedElement, ERR_underscoreURI_underscoreIO, new Object[] { href });
            } catch (SecurityException e) {
                throw new BridgeException(paintedElement, ERR_underscoreURI_underscoreUNSECURE, new Object[] { href });
            }
        }
        if (p == null) {
            return null;
        }
        int intent = convertIntent(profile);
        cs = new ICCColorSpaceExt(p, intent);
        cache.put(iccProfileName.toLowerCase(), cs);
        return cs;
    }

