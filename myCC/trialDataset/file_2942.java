    @Override
    public void run() {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Backupthread started");
            }
            if (_underscorefile.exists()) {
                _underscorefile.delete();
            }
            final ZipOutputStream zOut = new ZipOutputStream(new FileOutputStream(_underscorefile));
            zOut.setLevel(9);
            final File xmlFile = File.createTempFile("mp3db", ".xml");
            final OutputStream ost = new FileOutputStream(xmlFile);
            final XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(ost, "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");
            writer.writeStartElement("mp3db");
            writer.writeAttribute("version", Integer.toString(Main.ENGINEVERSION));
            final MediafileDAO mfDAO = new MediafileDAO();
            final AlbumDAO aDAO = new AlbumDAO();
            final CdDAO cdDAO = new CdDAO();
            final CoveritemDAO ciDAO = new CoveritemDAO();
            int itemCount = 0;
            try {
                itemCount += mfDAO.getCount();
                itemCount += aDAO.getCount();
                itemCount += cdDAO.getCount();
                itemCount += ciDAO.getCount();
                fireStatusEvent(new StatusEvent(this, StatusEventType.MAX_underscoreVALUE, itemCount));
            } catch (final Exception e) {
                LOG.error("Error getting size", e);
                fireStatusEvent(new StatusEvent(this, StatusEventType.MAX_underscoreVALUE, -1));
            }
            int cdCounter = 0;
            int mediafileCounter = 0;
            int albumCounter = 0;
            int coveritemCounter = 0;
            int counter = 0;
            final List<CdIf> data = cdDAO.getCdsOrderById();
            if (data.size() > 0) {
                final Map<Integer, Integer> albums = new HashMap<Integer, Integer>();
                final Iterator<CdIf> it = data.iterator();
                while (it.hasNext() && !_underscorebreak) {
                    final CdIf cd = it.next();
                    final Integer cdId = Integer.valueOf(cdCounter++);
                    writer.writeStartElement(TypeConstants.XML_underscoreCD);
                    exportCd(writer, cd, cdId);
                    fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_underscoreVALUE, ++counter));
                    final List<MediafileIf> files = cd.getMediafiles();
                    final Iterator<MediafileIf> mfit = files.iterator();
                    MediafileIf mf;
                    while (mfit.hasNext() && !_underscorebreak) {
                        mf = mfit.next();
                        final Integer mfId = Integer.valueOf(mediafileCounter++);
                        writer.writeStartElement(TypeConstants.XML_underscoreMEDIAFILE);
                        exportMediafile(writer, mf, mfId);
                        fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_underscoreVALUE, ++counter));
                        final AlbumIf a = mf.getAlbum();
                        if (a != null) {
                            Integer inte;
                            if (albums.containsKey(a.getAid())) {
                                inte = albums.get(a.getAid());
                                writeLink(writer, TypeConstants.XML_underscoreALBUM, inte);
                            } else {
                                inte = Integer.valueOf(albumCounter++);
                                writer.writeStartElement(TypeConstants.XML_underscoreALBUM);
                                exportAlbum(writer, a, inte);
                                fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_underscoreVALUE, ++counter));
                                albums.put(a.getAid(), inte);
                                if (a.hasCoveritems() && !_underscorebreak) {
                                    final List<CoveritemIf> covers = a.getCoveritems();
                                    final Iterator<CoveritemIf> coit = covers.iterator();
                                    while (coit.hasNext() && !_underscorebreak) {
                                        final Integer coveritemId = Integer.valueOf(coveritemCounter++);
                                        exportCoveritem(writer, zOut, coit.next(), coveritemId);
                                        fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_underscoreVALUE, ++counter));
                                    }
                                }
                                writer.writeEndElement();
                            }
                            GenericDAO.getEntityManager().close();
                        }
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();
                    writer.flush();
                    it.remove();
                    GenericDAO.getEntityManager().close();
                }
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
            ost.flush();
            ost.close();
            if (_underscorebreak) {
                zOut.close();
                _underscorefile.delete();
            } else {
                zOut.putNextEntry(new ZipEntry("mp3.xml"));
                final InputStream xmlIn = FileUtils.openInputStream(xmlFile);
                IOUtils.copy(xmlIn, zOut);
                xmlIn.close();
                zOut.close();
            }
            xmlFile.delete();
            fireStatusEvent(new StatusEvent(this, StatusEventType.FINISH));
        } catch (final Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error backup database", e);
            }
            fireStatusEvent(new StatusEvent(this, e, ""));
            _underscoremessenger.fireMessageEvent(new MessageEvent(this, "ERROR", MessageEventTypeEnum.ERROR, GuiStrings.getInstance().getString("error.backup"), e));
        }
    }

