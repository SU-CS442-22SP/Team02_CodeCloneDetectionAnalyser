    private void setInlineXML(Entry entry, DatastreamXMLMetadata ds) throws UnsupportedEncodingException, StreamIOException {
        String content;
        if (m_underscoreobj.hasContentModel(Models.SERVICE_underscoreDEPLOYMENT_underscore3_underscore0) && (ds.DatastreamID.equals("SERVICE-PROFILE") || ds.DatastreamID.equals("WSDL"))) {
            content = DOTranslationUtility.normalizeInlineXML(new String(ds.xmlContent, m_underscoreencoding), m_underscoretransContext);
        } else {
            content = new String(ds.xmlContent, m_underscoreencoding);
        }
        if (m_underscoreformat.equals(ATOM_underscoreZIP1_underscore1)) {
            String name = ds.DSVersionID + ".xml";
            try {
                m_underscorezout.putNextEntry(new ZipEntry(name));
                InputStream is = new ByteArrayInputStream(content.getBytes(m_underscoreencoding));
                IOUtils.copy(is, m_underscorezout);
                m_underscorezout.closeEntry();
                is.close();
            } catch (IOException e) {
                throw new StreamIOException(e.getMessage(), e);
            }
            IRI iri = new IRI(name);
            entry.setSummary(ds.DSVersionID);
            entry.setContent(iri, ds.DSMIME);
        } else {
            entry.setContent(content, ds.DSMIME);
        }
    }

