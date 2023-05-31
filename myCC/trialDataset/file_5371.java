    public void testEmptyBlock_underscore01() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/compression/emptyBlock_underscore01.xsd", getClass(), m_underscorecompilerErrors);
        Assert.assertEquals(0, m_underscorecompilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.STRICT_underscoreOPTIONS);
        Transmogrifier encoder = new Transmogrifier();
        EXIDecoder decoder = new EXIDecoder(31);
        Scanner scanner;
        InputSource inputSource;
        encoder.setOutputOptions(HeaderOptionsOutputType.lessSchemaId);
        encoder.setAlignmentType(AlignmentType.compress);
        encoder.setBlockSize(1);
        encoder.setEXISchema(grammarCache);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        encoder.setOutputStream(baos);
        URL url = resolveSystemIdAsURL("/compression/emptyBlock_underscore01.xml");
        inputSource = new InputSource(url.toString());
        inputSource.setByteStream(url.openStream());
        byte[] bts;
        int n_underscoreevents;
        encoder.encode(inputSource);
        bts = baos.toByteArray();
        decoder.setEXISchema(grammarCache);
        decoder.setInputStream(new ByteArrayInputStream(bts));
        scanner = decoder.processHeader();
        ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
        EXIEvent exiEvent;
        n_underscoreevents = 0;
        while ((exiEvent = scanner.nextEvent()) != null) {
            ++n_underscoreevents;
            exiEventList.add(exiEvent);
        }
        Assert.assertEquals(11, n_underscoreevents);
        Assert.assertEquals(1, ((ChannellingScanner) scanner).getBlockCount());
        EventType eventType;
        EventTypeList eventTypeList;
        int pos = 0;
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreSD, exiEvent.getEventVariety());
        eventType = exiEvent.getEventType();
        Assert.assertSame(exiEvent, eventType);
        Assert.assertEquals(0, eventType.getIndex());
        eventTypeList = eventType.getEventTypeList();
        Assert.assertNull(eventTypeList.getEE());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
        Assert.assertEquals("root", exiEvent.getName());
        Assert.assertEquals("", eventType.getURI());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
        Assert.assertEquals("parent", exiEvent.getName());
        Assert.assertEquals("", eventType.getURI());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
        Assert.assertEquals("child", exiEvent.getName());
        Assert.assertEquals("", eventType.getURI());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreCH, exiEvent.getEventVariety());
        Assert.assertEquals("42", exiEvent.getCharacters().makeString());
        int tp = ((EventTypeSchema) exiEvent.getEventType()).getSchemaSubstance();
        Assert.assertEquals(EXISchemaConst.UNSIGNED_underscoreBYTE_underscoreTYPE, corpus.getSerialOfType(tp));
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
        Assert.assertEquals("adjunct", exiEvent.getName());
        Assert.assertEquals("", exiEvent.getURI());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
        exiEvent = exiEventList.get(pos++);
        Assert.assertEquals(EXIEvent.EVENT_underscoreED, exiEvent.getEventVariety());
    }

