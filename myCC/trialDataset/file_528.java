    private void trainSRLParser(byte flag, JarArchiveOutputStream zout) throws Exception {
        AbstractSRLParser labeler = null;
        AbstractDecoder[] decoder = null;
        if (flag == SRLParser.FLAG_underscoreTRAIN_underscoreLEXICON) {
            System.out.println("\n* Save lexica");
            labeler = new SRLParser(flag, s_underscorefeatureXml);
        } else if (flag == SRLParser.FLAG_underscoreTRAIN_underscoreINSTANCE) {
            System.out.println("\n* Print training instances");
            System.out.println("- loading lexica");
            labeler = new SRLParser(flag, t_underscorexml, s_underscorelexiconFiles);
        } else if (flag == SRLParser.FLAG_underscoreTRAIN_underscoreBOOST) {
            System.out.println("\n* Train boost");
            decoder = new AbstractDecoder[m_underscoremodel.length];
            for (int i = 0; i < decoder.length; i++) decoder[i] = new OneVsAllDecoder((OneVsAllModel) m_underscoremodel[i]);
            labeler = new SRLParser(flag, t_underscorexml, t_underscoremap, decoder);
        }
        AbstractReader<DepNode, DepTree> reader = new SRLReader(s_underscoretrainFile, true);
        DepTree tree;
        int n;
        labeler.setLanguage(s_underscorelanguage);
        reader.setLanguage(s_underscorelanguage);
        for (n = 0; (tree = reader.nextTree()) != null; n++) {
            labeler.parse(tree);
            if (n % 1000 == 0) System.out.printf("\r- parsing: %dK", n / 1000);
        }
        System.out.println("\r- labeling: " + n);
        if (flag == SRLParser.FLAG_underscoreTRAIN_underscoreLEXICON) {
            System.out.println("- labeling");
            labeler.saveTags(s_underscorelexiconFiles);
            t_underscorexml = labeler.getSRLFtrXml();
        } else if (flag == SRLParser.FLAG_underscoreTRAIN_underscoreINSTANCE || flag == SRLParser.FLAG_underscoreTRAIN_underscoreBOOST) {
            a_underscoreyx = labeler.a_underscoretrans;
            zout.putArchiveEntry(new JarArchiveEntry(ENTRY_underscoreFEATURE));
            IOUtils.copy(new FileInputStream(s_underscorefeatureXml), zout);
            zout.closeArchiveEntry();
            for (String lexicaFile : s_underscorelexiconFiles) {
                zout.putArchiveEntry(new JarArchiveEntry(lexicaFile));
                IOUtils.copy(new FileInputStream(lexicaFile), zout);
                zout.closeArchiveEntry();
            }
            if (flag == SRLParser.FLAG_underscoreTRAIN_underscoreINSTANCE) t_underscoremap = labeler.getSRLFtrMap();
        }
    }

