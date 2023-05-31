    public void testJTLM_underscorepublish100_underscoreblockSize() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/JTLM/schemas/TLMComposite.xsd", getClass(), m_underscorecompilerErrors);
        Assert.assertEquals(0, m_underscorecompilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.STRICT_underscoreOPTIONS);
        AlignmentType[] alignments = new AlignmentType[] { AlignmentType.preCompress, AlignmentType.compress };
        int[] blockSizes = { 1, 100, 101 };
        Transmogrifier encoder = new Transmogrifier();
        EXIDecoder decoder = new EXIDecoder(999);
        encoder.setOutputOptions(HeaderOptionsOutputType.lessSchemaId);
        encoder.setEXISchema(grammarCache);
        decoder.setEXISchema(grammarCache);
        for (AlignmentType alignment : alignments) {
            for (int i = 0; i < blockSizes.length; i++) {
                Scanner scanner;
                InputSource inputSource;
                encoder.setAlignmentType(alignment);
                encoder.setBlockSize(blockSizes[i]);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                encoder.setOutputStream(baos);
                URL url = resolveSystemIdAsURL("/JTLM/publish100.xml");
                inputSource = new InputSource(url.toString());
                inputSource.setByteStream(url.openStream());
                byte[] bts;
                int n_underscoreevents, n_underscoretexts;
                encoder.encode(inputSource);
                bts = baos.toByteArray();
                decoder.setInputStream(new ByteArrayInputStream(bts));
                scanner = decoder.processHeader();
                ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
                EXIEvent exiEvent;
                n_underscoreevents = 0;
                n_underscoretexts = 0;
                while ((exiEvent = scanner.nextEvent()) != null) {
                    ++n_underscoreevents;
                    if (exiEvent.getEventVariety() == EXIEvent.EVENT_underscoreCH) {
                        if (exiEvent.getCharacters().length() == 0) {
                            --n_underscoreevents;
                            continue;
                        }
                        if (n_underscoretexts % 100 == 0) {
                            final int n = n_underscoretexts / 100;
                            Assert.assertEquals(JTLMTest.publish100_underscorecentennials[n], exiEvent.getCharacters().makeString());
                        }
                        ++n_underscoretexts;
                    }
                    exiEventList.add(exiEvent);
                }
                Assert.assertEquals(10610, n_underscoreevents);
            }
        }
    }

