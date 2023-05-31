    private void addAuditDatastream() throws ObjectIntegrityException, StreamIOException {
        if (m_underscoreobj.getAuditRecords().size() == 0) {
            return;
        }
        String dsId = m_underscorepid.toURI() + "/AUDIT";
        String dsvId = dsId + "/" + DateUtility.convertDateToString(m_underscoreobj.getCreateDate());
        Entry dsEntry = m_underscorefeed.addEntry();
        dsEntry.setId(dsId);
        dsEntry.setTitle("AUDIT");
        dsEntry.setUpdated(m_underscoreobj.getCreateDate());
        dsEntry.addCategory(MODEL.STATE.uri, "A", null);
        dsEntry.addCategory(MODEL.CONTROL_underscoreGROUP.uri, "X", null);
        dsEntry.addCategory(MODEL.VERSIONABLE.uri, "false", null);
        dsEntry.addLink(dsvId, Link.REL_underscoreALTERNATE);
        Entry dsvEntry = m_underscorefeed.addEntry();
        dsvEntry.setId(dsvId);
        dsvEntry.setTitle("AUDIT.0");
        dsvEntry.setUpdated(m_underscoreobj.getCreateDate());
        ThreadHelper.addInReplyTo(dsvEntry, m_underscorepid.toURI() + "/AUDIT");
        dsvEntry.addCategory(MODEL.FORMAT_underscoreURI.uri, AUDIT1_underscore0.uri, null);
        dsvEntry.addCategory(MODEL.LABEL.uri, "Audit Trail for this object", null);
        if (m_underscoreformat.equals(ATOM_underscoreZIP1_underscore1)) {
            String name = "AUDIT.0.xml";
            try {
                m_underscorezout.putNextEntry(new ZipEntry(name));
                Reader r = new StringReader(DOTranslationUtility.getAuditTrail(m_underscoreobj));
                IOUtils.copy(r, m_underscorezout, m_underscoreencoding);
                m_underscorezout.closeEntry();
                r.close();
            } catch (IOException e) {
                throw new StreamIOException(e.getMessage(), e);
            }
            IRI iri = new IRI(name);
            dsvEntry.setSummary("AUDIT.0");
            dsvEntry.setContent(iri, "text/xml");
        } else {
            dsvEntry.setContent(DOTranslationUtility.getAuditTrail(m_underscoreobj), "text/xml");
        }
    }

