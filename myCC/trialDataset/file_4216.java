    private void trainDepParser(byte flag, JarArchiveOutputStream zout) throws Exception {
        AbstractDepParser parser = null;
        OneVsAllDecoder decoder = null;
        if (flag == ShiftPopParser.FLAG_underscoreTRAIN_underscoreLEXICON) {
            System.out.println("\n* Save lexica");
            if (s_underscoredepParser.equals(AbstractDepParser.ALG_underscoreSHIFT_underscoreEAGER)) parser = new ShiftEagerParser(flag, s_underscorefeatureXml); else if (s_underscoredepParser.equals(AbstractDepParser.ALG_underscoreSHIFT_underscorePOP)) parser = new ShiftPopParser(flag, s_underscorefeatureXml);
        } else if (flag == ShiftPopParser.FLAG_underscoreTRAIN_underscoreINSTANCE) {
            System.out.println("\n* Print training instances");
            System.out.println("- loading lexica");
            if (s_underscoredepParser.equals(AbstractDepParser.ALG_underscoreSHIFT_underscoreEAGER)) parser = new ShiftEagerParser(flag, t_underscorexml, ENTRY_underscoreLEXICA); else if (s_underscoredepParser.equals(AbstractDepParser.ALG_underscoreSHIFT_underscorePOP)) parser = new ShiftPopParser(flag, t_underscorexml, ENTRY_underscoreLEXICA);
        } else if (flag == ShiftPopParser.FLAG_underscoreTRAIN_underscoreBOOST) {
            System.out.println("\n* Train conditional");
            decoder = new OneVsAllDecoder(m_underscoremodel);
            if (s_underscoredepParser.equals(AbstractDepParser.ALG_underscoreSHIFT_underscoreEAGER)) parser = new ShiftEagerParser(flag, t_underscorexml, t_underscoremap, decoder); else if (s_underscoredepParser.equals(AbstractDepParser.ALG_underscoreSHIFT_underscorePOP)) parser = new ShiftPopParser(flag, t_underscorexml, t_underscoremap, decoder);
        }
        AbstractReader<DepNode, DepTree> reader = null;
        DepTree tree;
        int n;
        if (s_underscoreformat.equals(AbstractReader.FORMAT_underscoreDEP)) reader = new DepReader(s_underscoretrainFile, true); else if (s_underscoreformat.equals(AbstractReader.FORMAT_underscoreCONLLX)) reader = new CoNLLXReader(s_underscoretrainFile, true);
        parser.setLanguage(s_underscorelanguage);
        reader.setLanguage(s_underscorelanguage);
        for (n = 0; (tree = reader.nextTree()) != null; n++) {
            parser.parse(tree);
            if (n % 1000 == 0) System.out.printf("\r- parsing: %dK", n / 1000);
        }
        System.out.println("\r- parsing: " + n);
        if (flag == ShiftPopParser.FLAG_underscoreTRAIN_underscoreLEXICON) {
            System.out.println("- saving");
            parser.saveTags(ENTRY_underscoreLEXICA);
            t_underscorexml = parser.getDepFtrXml();
        } else if (flag == ShiftPopParser.FLAG_underscoreTRAIN_underscoreINSTANCE || flag == ShiftPopParser.FLAG_underscoreTRAIN_underscoreBOOST) {
            a_underscoreyx = parser.a_underscoretrans;
            zout.putArchiveEntry(new JarArchiveEntry(ENTRY_underscorePARSER));
            PrintStream fout = new PrintStream(zout);
            fout.print(s_underscoredepParser);
            fout.flush();
            zout.closeArchiveEntry();
            zout.putArchiveEntry(new JarArchiveEntry(ENTRY_underscoreFEATURE));
            IOUtils.copy(new FileInputStream(s_underscorefeatureXml), zout);
            zout.closeArchiveEntry();
            zout.putArchiveEntry(new JarArchiveEntry(ENTRY_underscoreLEXICA));
            IOUtils.copy(new FileInputStream(ENTRY_underscoreLEXICA), zout);
            zout.closeArchiveEntry();
            if (flag == ShiftPopParser.FLAG_underscoreTRAIN_underscoreINSTANCE) t_underscoremap = parser.getDepFtrMap();
        }
    }

