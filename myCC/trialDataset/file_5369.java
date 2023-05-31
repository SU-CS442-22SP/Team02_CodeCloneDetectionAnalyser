    public void testJTLM_underscorepublish911() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/JTLM/schemas/TLMComposite.xsd", getClass(), m_underscorecompilerErrors);
        Assert.assertEquals(0, m_underscorecompilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.STRICT_underscoreOPTIONS);
        AlignmentType alignment = AlignmentType.compress;
        Transmogrifier encoder = new Transmogrifier();
        EXIDecoder decoder = new EXIDecoder();
        Scanner scanner;
        InputSource inputSource;
        encoder.setAlignmentType(alignment);
        decoder.setAlignmentType(alignment);
        encoder.setEXISchema(grammarCache);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        encoder.setOutputStream(baos);
        URL url = resolveSystemIdAsURL("/JTLM/publish911.xml");
        inputSource = new InputSource(url.toString());
        inputSource.setByteStream(url.openStream());
        byte[] bts;
        int n_underscoreevents, n_underscoretexts;
        encoder.encode(inputSource);
        bts = baos.toByteArray();
        decoder.setEXISchema(grammarCache);
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
                    Assert.assertEquals(JTLMTest.publish911_underscorecentennials[n], exiEvent.getCharacters().makeString());
                }
                ++n_underscoretexts;
            }
            exiEventList.add(exiEvent);
        }
        Assert.assertEquals(96576, n_underscoreevents);
    }

