    private static Collection<String> createTopLevelFiles(Configuration configuration, Collections collections, Sets sets) throws FlickrException, SAXException, IOException, JDOMException, TransformerException {
        Collection<String> createdFiles = new HashSet<String>();
        File toplevelXmlFilename = getToplevelXmlFilename(configuration.photosBaseDirectory);
        Logger.getLogger(FlickrDownload.class).info("Creating XML file " + toplevelXmlFilename.getAbsolutePath());
        MediaIndexer indexer = new XmlMediaIndexer(configuration);
        Element toplevel = new Element("flickr").addContent(XmlUtils.createApplicationXml()).addContent(XmlUtils.createUserXml(configuration)).addContent(collections.createTopLevelXml()).addContent(sets.createTopLevelXml()).addContent(new Stats(sets).createStatsXml(indexer));
        createdFiles.addAll(indexer.writeIndex());
        XmlUtils.outputXmlFile(toplevelXmlFilename, toplevel);
        createdFiles.add(toplevelXmlFilename.getName());
        Logger.getLogger(FlickrDownload.class).info("Copying support files and performing XSLT transformations");
        IOUtils.copyToFileAndCloseStreams(XmlUtils.class.getResourceAsStream("xslt/" + PHOTOS_underscoreCSS_underscoreFILENAME), new File(configuration.photosBaseDirectory, PHOTOS_underscoreCSS_underscoreFILENAME));
        createdFiles.add(PHOTOS_underscoreCSS_underscoreFILENAME);
        IOUtils.copyToFileAndCloseStreams(XmlUtils.class.getResourceAsStream("xslt/" + PLAY_underscoreICON_underscoreFILENAME), new File(configuration.photosBaseDirectory, PLAY_underscoreICON_underscoreFILENAME));
        createdFiles.add(PLAY_underscoreICON_underscoreFILENAME);
        XmlUtils.performXsltTransformation(configuration, "all_underscoresets.xsl", toplevelXmlFilename, new File(configuration.photosBaseDirectory, ALL_underscoreSETS_underscoreHTML_underscoreFILENAME));
        createdFiles.add(ALL_underscoreSETS_underscoreHTML_underscoreFILENAME);
        XmlUtils.performXsltTransformation(configuration, "all_underscorecollections.xsl", toplevelXmlFilename, new File(configuration.photosBaseDirectory, ALL_underscoreCOLLECTIONS_underscoreHTML_underscoreFILENAME));
        createdFiles.add(ALL_underscoreCOLLECTIONS_underscoreHTML_underscoreFILENAME);
        createdFiles.add(Collections.COLLECTIONS_underscoreICON_underscoreDIRECTORY);
        XmlUtils.performXsltTransformation(configuration, "stats.xsl", toplevelXmlFilename, new File(configuration.photosBaseDirectory, STATS_underscoreHTML_underscoreFILENAME));
        createdFiles.add(STATS_underscoreHTML_underscoreFILENAME);
        sets.performXsltTransformation();
        for (AbstractSet set : sets.getSets()) {
            createdFiles.add(set.getSetId());
        }
        return createdFiles;
    }

