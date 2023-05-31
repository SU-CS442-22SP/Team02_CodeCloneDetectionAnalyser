    public void testSequence_underscore01() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/interop/schemaInformedGrammar/acceptance.xsd", getClass(), m_underscorecompilerErrors);
        Assert.assertEquals(0, m_underscorecompilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.STRICT_underscoreOPTIONS);
        AlignmentType[] alignments = new AlignmentType[] { AlignmentType.preCompress, AlignmentType.compress };
        int[] strategies = { Deflater.DEFAULT_underscoreSTRATEGY, Deflater.FILTERED, Deflater.HUFFMAN_underscoreONLY };
        for (AlignmentType alignment : alignments) {
            Transmogrifier encoder = new Transmogrifier();
            EXIDecoder decoder = new EXIDecoder(31);
            Scanner scanner;
            InputSource inputSource;
            encoder.setOutputOptions(HeaderOptionsOutputType.lessSchemaId);
            encoder.setAlignmentType(alignment);
            encoder.setDeflateLevel(java.util.zip.Deflater.BEST_underscoreCOMPRESSION);
            final boolean isCompress = alignment == AlignmentType.compress;
            byte[][] resultBytes = isCompress ? new byte[3][] : null;
            for (int i = 0; i < strategies.length; i++) {
                encoder.setDeflateStrategy(strategies[i]);
                encoder.setEXISchema(grammarCache);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                encoder.setOutputStream(baos);
                URL url = resolveSystemIdAsURL("/interop/schemaInformedGrammar/declaredProductions/sequence-01.xml");
                inputSource = new InputSource(url.toString());
                inputSource.setByteStream(url.openStream());
                byte[] bts;
                int n_underscoreevents;
                encoder.encode(inputSource);
                bts = baos.toByteArray();
                if (isCompress) resultBytes[i] = bts;
                decoder.setEXISchema(grammarCache);
                decoder.setInputStream(new ByteArrayInputStream(bts));
                scanner = decoder.processHeader();
                Assert.assertEquals(alignment, scanner.getHeaderOptions().getAlignmentType());
                ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
                EXIEvent exiEvent;
                n_underscoreevents = 0;
                while ((exiEvent = scanner.nextEvent()) != null) {
                    ++n_underscoreevents;
                    exiEventList.add(exiEvent);
                }
                Assert.assertEquals(19, n_underscoreevents);
                EventType eventType;
                EventTypeList eventTypeList;
                int pos = 0;
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreSD, exiEvent.getEventVariety());
                eventType = exiEvent.getEventType();
                Assert.assertSame(exiEvent, eventType);
                Assert.assertEquals(0, eventType.getIndex());
                eventTypeList = eventType.getEventTypeList();
                Assert.assertEquals(1, eventTypeList.getLength());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
                Assert.assertEquals("A", exiEvent.getName());
                Assert.assertEquals("urn:foo", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
                Assert.assertEquals("AB", exiEvent.getName());
                Assert.assertEquals("urn:foo", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreCH, exiEvent.getEventVariety());
                Assert.assertEquals("", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
                Assert.assertEquals("AC", exiEvent.getName());
                Assert.assertEquals("urn:foo", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreCH, exiEvent.getEventVariety());
                Assert.assertEquals("", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
                Assert.assertEquals("AC", exiEvent.getName());
                Assert.assertEquals("urn:foo", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreCH, exiEvent.getEventVariety());
                Assert.assertEquals("", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
                Assert.assertEquals("AD", exiEvent.getName());
                Assert.assertEquals("urn:foo", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreCH, exiEvent.getEventVariety());
                Assert.assertEquals("", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreSE, exiEvent.getEventVariety());
                Assert.assertEquals("AE", exiEvent.getName());
                Assert.assertEquals("urn:foo", exiEvent.getURI());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreCH, exiEvent.getEventVariety());
                Assert.assertEquals("", exiEvent.getCharacters().makeString());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreEE, exiEvent.getEventVariety());
                exiEvent = exiEventList.get(pos++);
                Assert.assertEquals(EXIEvent.EVENT_underscoreED, exiEvent.getEventVariety());
            }
            if (isCompress) {
                Assert.assertTrue(resultBytes[0].length < resultBytes[1].length);
                Assert.assertTrue(resultBytes[1].length < resultBytes[2].length);
            }
        }
    }

