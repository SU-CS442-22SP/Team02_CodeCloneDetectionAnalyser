    private void setManagedContent(Entry entry, Datastream vds) throws StreamIOException {
        if (m_underscoretransContext == DOTranslationUtility.SERIALIZE_underscoreEXPORT_underscoreARCHIVE && !m_underscoreformat.equals(ATOM_underscoreZIP1_underscore1)) {
            String mimeType = vds.DSMIME;
            if (MimeTypeHelper.isText(mimeType) || MimeTypeHelper.isXml(mimeType)) {
                try {
                    entry.setContent(IOUtils.toString(vds.getContentStream(), m_underscoreencoding), mimeType);
                } catch (IOException e) {
                    throw new StreamIOException(e.getMessage(), e);
                }
            } else {
                entry.setContent(vds.getContentStream(), mimeType);
            }
        } else {
            String dsLocation;
            IRI iri;
            if (m_underscoreformat.equals(ATOM_underscoreZIP1_underscore1) && m_underscoretransContext != DOTranslationUtility.AS_underscoreIS) {
                dsLocation = vds.DSVersionID + "." + MimeTypeUtils.fileExtensionForMIMEType(vds.DSMIME);
                try {
                    m_underscorezout.putNextEntry(new ZipEntry(dsLocation));
                    InputStream is = vds.getContentStream();
                    IOUtils.copy(is, m_underscorezout);
                    is.close();
                    m_underscorezout.closeEntry();
                } catch (IOException e) {
                    throw new StreamIOException(e.getMessage(), e);
                }
            } else {
                dsLocation = StreamUtility.enc(DOTranslationUtility.normalizeDSLocationURLs(m_underscoreobj.getPid(), vds, m_underscoretransContext).DSLocation);
            }
            iri = new IRI(dsLocation);
            entry.setSummary(vds.DSVersionID);
            entry.setContent(iri, vds.DSMIME);
        }
    }

