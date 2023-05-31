    public void testDecodeJTLM_underscorepublish100() throws Exception {
        EXISchema corpus = EXISchemaFactoryTestUtil.getEXISchema("/JTLM/schemas/TLMComposite.xsd", getClass(), m_underscorecompilerErrors);
        Assert.assertEquals(0, m_underscorecompilerErrors.getTotalCount());
        GrammarCache grammarCache = new GrammarCache(corpus, GrammarOptions.DEFAULT_underscoreOPTIONS);
        String[] exiFiles = { "/JTLM/publish100/publish100.bitPacked", "/JTLM/publish100/publish100.byteAligned", "/JTLM/publish100/publish100.preCompress", "/JTLM/publish100/publish100.compress" };
        for (int i = 0; i < Alignments.length; i++) {
            AlignmentType alignment = Alignments[i];
            EXIDecoder decoder = new EXIDecoder();
            Scanner scanner;
            decoder.setAlignmentType(alignment);
            URL url = resolveSystemIdAsURL(exiFiles[i]);
            int n_underscoreevents, n_underscoretexts;
            decoder.setEXISchema(grammarCache);
            decoder.setInputStream(url.openStream());
            scanner = decoder.processHeader();
            ArrayList<EXIEvent> exiEventList = new ArrayList<EXIEvent>();
            EXIEvent exiEvent;
            n_underscoreevents = 0;
            n_underscoretexts = 0;
            while ((exiEvent = scanner.nextEvent()) != null) {
                ++n_underscoreevents;
                if (exiEvent.getEventVariety() == EXIEvent.EVENT_underscoreCH) {
                    String stringValue = exiEvent.getCharacters().makeString();
                    if (stringValue.length() == 0 && exiEvent.getEventType().itemType == EventCode.ITEM_underscoreSCHEMA_underscoreCH) {
                        --n_underscoreevents;
                        continue;
                    }
                    if (n_underscoretexts % 100 == 0) {
                        final int n = n_underscoretexts / 100;
                        Assert.assertEquals(publish100_underscorecentennials[n], stringValue);
                    }
                    ++n_underscoretexts;
                }
                exiEventList.add(exiEvent);
            }
            Assert.assertEquals(10610, n_underscoreevents);
        }
    }

