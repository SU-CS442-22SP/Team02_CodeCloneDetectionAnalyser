    private Datastream addManagedDatastreamVersion(Entry entry) throws StreamIOException, ObjectIntegrityException {
        Datastream ds = new DatastreamManagedContent();
        setDSCommonProperties(ds, entry);
        ds.DSLocationType = "INTERNAL_underscoreID";
        ds.DSMIME = getDSMimeType(entry);
        IRI contentLocation = entry.getContentSrc();
        if (contentLocation != null) {
            if (m_underscoreobj.isNew()) {
                ValidationUtility.validateURL(contentLocation.toString(), ds.DSControlGrp);
            }
            if (m_underscoreformat.equals(ATOM_underscoreZIP1_underscore1)) {
                if (!contentLocation.isAbsolute() && !contentLocation.isPathAbsolute()) {
                    File f = getContentSrcAsFile(contentLocation);
                    contentLocation = new IRI(DatastreamManagedContent.TEMP_underscoreSCHEME + f.getAbsolutePath());
                }
            }
            ds.DSLocation = contentLocation.toString();
            ds.DSLocation = (DOTranslationUtility.normalizeDSLocationURLs(m_underscoreobj.getPid(), ds, m_underscoretransContext)).DSLocation;
            return ds;
        }
        try {
            File temp = File.createTempFile("binary-datastream", null);
            OutputStream out = new FileOutputStream(temp);
            if (MimeTypeHelper.isText(ds.DSMIME) || MimeTypeHelper.isXml(ds.DSMIME)) {
                IOUtils.copy(new StringReader(entry.getContent()), out, m_underscoreencoding);
            } else {
                IOUtils.copy(entry.getContentStream(), out);
            }
            ds.DSLocation = DatastreamManagedContent.TEMP_underscoreSCHEME + temp.getAbsolutePath();
        } catch (IOException e) {
            throw new StreamIOException(e.getMessage(), e);
        }
        return ds;
    }

