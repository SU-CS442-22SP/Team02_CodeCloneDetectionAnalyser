    protected GVTFontFamily getFontFamily(BridgeContext ctx, ParsedURL purl) {
        String purlStr = purl.toString();
        Element e = getBaseElement(ctx);
        SVGDocument svgDoc = (SVGDocument) e.getOwnerDocument();
        String docURL = svgDoc.getURL();
        ParsedURL pDocURL = null;
        if (docURL != null) pDocURL = new ParsedURL(docURL);
        String baseURI = XMLBaseSupport.getCascadedXMLBase(e);
        purl = new ParsedURL(baseURI, purlStr);
        UserAgent userAgent = ctx.getUserAgent();
        try {
            userAgent.checkLoadExternalResource(purl, pDocURL);
        } catch (SecurityException ex) {
            userAgent.displayError(ex);
            return null;
        }
        if (purl.getRef() != null) {
            Element ref = ctx.getReferencedElement(e, purlStr);
            if (!ref.getNamespaceURI().equals(SVG_underscoreNAMESPACE_underscoreURI) || !ref.getLocalName().equals(SVG_underscoreFONT_underscoreTAG)) {
                return null;
            }
            SVGDocument doc = (SVGDocument) e.getOwnerDocument();
            SVGDocument rdoc = (SVGDocument) ref.getOwnerDocument();
            Element fontElt = ref;
            if (doc != rdoc) {
                fontElt = (Element) doc.importNode(ref, true);
                String base = XMLBaseSupport.getCascadedXMLBase(ref);
                Element g = doc.createElementNS(SVG_underscoreNAMESPACE_underscoreURI, SVG_underscoreG_underscoreTAG);
                g.appendChild(fontElt);
                g.setAttributeNS(XMLBaseSupport.XML_underscoreNAMESPACE_underscoreURI, "xml:base", base);
                CSSUtilities.computeStyleAndURIs(ref, fontElt, purlStr);
            }
            Element fontFaceElt = null;
            for (Node n = fontElt.getFirstChild(); n != null; n = n.getNextSibling()) {
                if ((n.getNodeType() == Node.ELEMENT_underscoreNODE) && n.getNamespaceURI().equals(SVG_underscoreNAMESPACE_underscoreURI) && n.getLocalName().equals(SVG_underscoreFONT_underscoreFACE_underscoreTAG)) {
                    fontFaceElt = (Element) n;
                    break;
                }
            }
            SVGFontFaceElementBridge fontFaceBridge;
            fontFaceBridge = (SVGFontFaceElementBridge) ctx.getBridge(SVG_underscoreNAMESPACE_underscoreURI, SVG_underscoreFONT_underscoreFACE_underscoreTAG);
            GVTFontFace gff = fontFaceBridge.createFontFace(ctx, fontFaceElt);
            return new SVGFontFamily(gff, fontElt, ctx);
        }
        try {
            Font font = Font.createFont(Font.TRUETYPE_underscoreFONT, purl.openStream());
            return new AWTFontFamily(this, font);
        } catch (Exception ex) {
        }
        return null;
    }

