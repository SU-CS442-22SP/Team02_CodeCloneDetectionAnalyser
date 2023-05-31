    public SWORDEntry ingestDepost(final DepositCollection pDeposit, final ServiceDocument pServiceDocument) throws SWORDException {
        try {
            ZipFileAccess tZipFile = new ZipFileAccess(super.getTempDir());
            LOG.debug("copying file");
            String tZipTempFileName = super.getTempDir() + "uploaded-file.tmp";
            IOUtils.copy(pDeposit.getFile(), new FileOutputStream(tZipTempFileName));
            Datastream tDatastream = new LocalDatastream(super.getGenericFileName(pDeposit), this.getContentType(), tZipTempFileName);
            _underscoredatastreamList.add(tDatastream);
            _underscoredatastreamList.addAll(tZipFile.getFiles(tZipTempFileName));
            int i = 0;
            boolean found = false;
            for (i = 0; i < _underscoredatastreamList.size(); i++) {
                if (_underscoredatastreamList.get(i).getId().equalsIgnoreCase("mets")) {
                    found = true;
                    break;
                }
            }
            if (found) {
                SAXBuilder tBuilder = new SAXBuilder();
                _underscoremets = new METSObject(tBuilder.build(((LocalDatastream) _underscoredatastreamList.get(i)).getPath()));
                LocalDatastream tLocalMETSDS = (LocalDatastream) _underscoredatastreamList.remove(i);
                new File(tLocalMETSDS.getPath()).delete();
                _underscoredatastreamList.add(_underscoremets.getMETSDs());
                _underscoredatastreamList.addAll(_underscoremets.getMetadataDatastreams());
            } else {
                throw new SWORDException("Couldn't find a METS document in the zip file, ensure it is named mets.xml or METS.xml");
            }
            SWORDEntry tEntry = super.ingestDepost(pDeposit, pServiceDocument);
            tZipFile.removeLocalFiles();
            return tEntry;
        } catch (IOException tIOExcpt) {
            String tMessage = "Couldn't retrieve METS from deposit: " + tIOExcpt.toString();
            LOG.error(tMessage);
            tIOExcpt.printStackTrace();
            throw new SWORDException(tMessage, tIOExcpt);
        } catch (JDOMException tJDOMExcpt) {
            String tMessage = "Couldn't build METS from deposit: " + tJDOMExcpt.toString();
            LOG.error(tMessage);
            tJDOMExcpt.printStackTrace();
            throw new SWORDException(tMessage, tJDOMExcpt);
        }
    }

