    @Override
    public Representation getRepresentation(Variant variant) {
        Representation representation = null;
        MediaType requestMediaType = variant.getMediaType();
        if (MediaType.TEXT_underscoreXML.equals(requestMediaType)) {
            try {
                representation = new DomRepresentation(MediaType.TEXT_underscoreXML);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        String strJobsUrl = ((ResourceApplication) getApplication()).getJobsUrl();
        org.dom4j.Document reportDoc = null;
        try {
            SAXReader reader = new SAXReader();
            reportDoc = reader.read(strJobsUrl + "/" + strJobId + "/report.xml");
        } catch (DocumentException e) {
            String strErrMsg = "Unable to find job, " + strJobId + ": " + e.getMessage();
            log.debug(strErrMsg);
            Representation rep = SetRepositoryMessage(Status.CLIENT_underscoreERROR_underscoreNOT_underscoreFOUND, null, "JobNotFound", strErrMsg, null);
            return rep;
        }
        if (MediaType.TEXT_underscoreXML.equals(requestMediaType)) {
            log.debug("XML Media Type requested");
            try {
                reportDoc.removeProcessingInstruction("xml-stylesheet");
                DOMWriter writer = new DOMWriter();
                Document doc = writer.write(reportDoc);
                doc.normalizeDocument();
                ((DomRepresentation) representation).setDocument(doc);
                return representation;
            } catch (DocumentException e) {
                String strErrMsg = "Error display job's report: " + e.getMessage();
                log.debug(strErrMsg);
                Representation rep = SetRepositoryMessage(Status.CLIENT_underscoreERROR_underscoreCONFLICT, null, "ClientConflict", strErrMsg, null);
                return rep;
            }
        } else if (MediaType.TEXT_underscoreHTML.equals(requestMediaType)) {
            log.debug("HTML Media Type requested");
            try {
                ProcessingInstruction pi = reportDoc.processingInstruction("xml-stylesheet");
                if (pi != null) {
                    String strXslHref = pi.getValue("href");
                    URL url = new URL(strXslHref);
                    XSLTransformer reportDocTransformer = new XSLTransformer();
                    log.debug("constructed the reportDocTransformer");
                    reportDoc = reportDocTransformer.transform(reportDoc, url.openStream());
                }
            } catch (MalformedURLException e) {
                String strErrMsg = "Error accessing referenced XSL-STYLESHEET: " + e.getMessage();
                log.debug(strErrMsg);
                Representation rep = SetRepositoryMessage(Status.SERVER_underscoreERROR_underscoreINTERNAL, null, "InternalError", strErrMsg, null);
                return rep;
            } catch (IOException e) {
                String strErrMsg = "Error accessing referenced XSL-STYLESHEET: " + e.getMessage();
                log.debug(strErrMsg);
                Representation rep = SetRepositoryMessage(Status.SERVER_underscoreERROR_underscoreINTERNAL, null, "InternalError", strErrMsg, null);
                return rep;
            } catch (FedoraAPIException e) {
                String strErrMsg = "Error accessing referenced XSL-STYLESHEET: " + e.getMessage();
                log.debug(strErrMsg);
                Representation rep = SetRepositoryMessage(Status.SERVER_underscoreERROR_underscoreINTERNAL, null, "InternalError", strErrMsg, null);
                return rep;
            }
            representation = new StringRepresentation(reportDoc.asXML(), MediaType.TEXT_underscoreHTML);
            return representation;
        }
        return null;
    }

