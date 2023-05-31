    public static void main(String[] args) throws IOException {
        String paramFileName = args[0];
        BufferedReader inFile_underscoreparams = new BufferedReader(new FileReader(paramFileName));
        String cands_underscorefileName = (inFile_underscoreparams.readLine().split("\\s+"))[0];
        String alignSrcCand_underscorephrasal_underscorefileName = (inFile_underscoreparams.readLine().split("\\s+"))[0];
        String alignSrcCand_underscoreword_underscorefileName = (inFile_underscoreparams.readLine().split("\\s+"))[0];
        String source_underscorefileName = (inFile_underscoreparams.readLine().split("\\s+"))[0];
        String trainSrc_underscorefileName = (inFile_underscoreparams.readLine().split("\\s+"))[0];
        String trainTgt_underscorefileName = (inFile_underscoreparams.readLine().split("\\s+"))[0];
        String trainAlign_underscorefileName = (inFile_underscoreparams.readLine().split("\\s+"))[0];
        String alignCache_underscorefileName = (inFile_underscoreparams.readLine().split("\\s+"))[0];
        String alignmentsType = "AlignmentGrids";
        int maxCacheSize = 1000;
        inFile_underscoreparams.close();
        int numSentences = countLines(source_underscorefileName);
        InputStream inStream_underscoresrc = new FileInputStream(new File(source_underscorefileName));
        BufferedReader srcFile = new BufferedReader(new InputStreamReader(inStream_underscoresrc, "utf8"));
        String[] srcSentences = new String[numSentences];
        for (int i = 0; i < numSentences; ++i) {
            srcSentences[i] = srcFile.readLine();
        }
        srcFile.close();
        println("Creating src vocabulary @ " + (new Date()));
        srcVocab = new Vocabulary();
        int[] sourceWordsSentences = Vocabulary.initializeVocabulary(trainSrc_underscorefileName, srcVocab, true);
        int numSourceWords = sourceWordsSentences[0];
        int numSourceSentences = sourceWordsSentences[1];
        println("Reading src corpus @ " + (new Date()));
        srcCorpusArray = SuffixArrayFactory.createCorpusArray(trainSrc_underscorefileName, srcVocab, numSourceWords, numSourceSentences);
        println("Creating src SA @ " + (new Date()));
        srcSA = SuffixArrayFactory.createSuffixArray(srcCorpusArray, maxCacheSize);
        println("Creating tgt vocabulary @ " + (new Date()));
        tgtVocab = new Vocabulary();
        int[] targetWordsSentences = Vocabulary.initializeVocabulary(trainTgt_underscorefileName, tgtVocab, true);
        int numTargetWords = targetWordsSentences[0];
        int numTargetSentences = targetWordsSentences[1];
        println("Reading tgt corpus @ " + (new Date()));
        tgtCorpusArray = SuffixArrayFactory.createCorpusArray(trainTgt_underscorefileName, tgtVocab, numTargetWords, numTargetSentences);
        println("Creating tgt SA @ " + (new Date()));
        tgtSA = SuffixArrayFactory.createSuffixArray(tgtCorpusArray, maxCacheSize);
        int trainingSize = srcCorpusArray.getNumSentences();
        if (trainingSize != tgtCorpusArray.getNumSentences()) {
            throw new RuntimeException("Source and target corpora have different number of sentences. This is bad.");
        }
        println("Reading alignment data @ " + (new Date()));
        alignments = null;
        if ("AlignmentArray".equals(alignmentsType)) {
            alignments = SuffixArrayFactory.createAlignments(trainAlign_underscorefileName, srcSA, tgtSA);
        } else if ("AlignmentGrids".equals(alignmentsType) || "AlignmentsGrid".equals(alignmentsType)) {
            alignments = new AlignmentGrids(new Scanner(new File(trainAlign_underscorefileName)), srcCorpusArray, tgtCorpusArray, trainingSize, true);
        } else if ("MemoryMappedAlignmentGrids".equals(alignmentsType)) {
            alignments = new MemoryMappedAlignmentGrids(trainAlign_underscorefileName, srcCorpusArray, tgtCorpusArray);
        }
        if (!fileExists(alignCache_underscorefileName)) {
            alreadyResolved_underscoresrcSet = new HashMap<String, TreeSet<Integer>>();
            alreadyResolved_underscoretgtSet = new HashMap<String, TreeSet<Integer>>();
        } else {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(alignCache_underscorefileName));
                alreadyResolved_underscoresrcSet = (HashMap<String, TreeSet<Integer>>) in.readObject();
                alreadyResolved_underscoretgtSet = (HashMap<String, TreeSet<Integer>>) in.readObject();
                in.close();
            } catch (FileNotFoundException e) {
                System.err.println("FileNotFoundException in AlignCandidates.main(String[]): " + e.getMessage());
                System.exit(99901);
            } catch (IOException e) {
                System.err.println("IOException in AlignCandidates.main(String[]): " + e.getMessage());
                System.exit(99902);
            } catch (ClassNotFoundException e) {
                System.err.println("ClassNotFoundException in AlignCandidates.main(String[]): " + e.getMessage());
                System.exit(99904);
            }
        }
        println("Processing candidates @ " + (new Date()));
        PrintWriter outFile_underscorealignSrcCand_underscorephrasal = new PrintWriter(alignSrcCand_underscorephrasal_underscorefileName);
        PrintWriter outFile_underscorealignSrcCand_underscoreword = new PrintWriter(alignSrcCand_underscoreword_underscorefileName);
        InputStream inStream_underscorecands = new FileInputStream(new File(cands_underscorefileName));
        BufferedReader candsFile = new BufferedReader(new InputStreamReader(inStream_underscorecands, "utf8"));
        String line = "";
        String cand = "";
        line = candsFile.readLine();
        int countSatisfied = 0;
        int countAll = 0;
        int countSatisfied_underscoresizeOne = 0;
        int countAll_underscoresizeOne = 0;
        int prev_underscorei = -1;
        String srcSent = "";
        String[] srcWords = null;
        int candsRead = 0;
        int C50count = 0;
        while (line != null) {
            ++candsRead;
            println("Read candidate on line #" + candsRead);
            int i = toInt((line.substring(0, line.indexOf("|||"))).trim());
            if (i != prev_underscorei) {
                srcSent = srcSentences[i];
                srcWords = srcSent.split("\\s+");
                prev_underscorei = i;
                println("New value for i: " + i + " seen @ " + (new Date()));
                C50count = 0;
            } else {
                ++C50count;
            }
            line = (line.substring(line.indexOf("|||") + 3)).trim();
            cand = (line.substring(0, line.indexOf("|||"))).trim();
            cand = cand.substring(cand.indexOf(" ") + 1, cand.length() - 1);
            JoshuaDerivationTree DT = new JoshuaDerivationTree(cand, 0);
            String candSent = DT.toSentence();
            String[] candWords = candSent.split("\\s+");
            String alignSrcCand = DT.alignments();
            outFile_underscorealignSrcCand_underscorephrasal.println(alignSrcCand);
            println("  i = " + i + ", alignSrcCand: " + alignSrcCand);
            String alignSrcCand_underscoreres = "";
            String[] linksSrcCand = alignSrcCand.split("\\s+");
            for (int k = 0; k < linksSrcCand.length; ++k) {
                String link = linksSrcCand[k];
                if (link.indexOf(',') == -1) {
                    alignSrcCand_underscoreres += " " + link.replaceFirst("--", "-");
                } else {
                    alignSrcCand_underscoreres += " " + resolve(link, srcWords, candWords);
                }
            }
            alignSrcCand_underscoreres = alignSrcCand_underscoreres.trim();
            println("  i = " + i + ", alignSrcCand_underscoreres: " + alignSrcCand_underscoreres);
            outFile_underscorealignSrcCand_underscoreword.println(alignSrcCand_underscoreres);
            if (C50count == 50) {
                println("50C @ " + (new Date()));
                C50count = 0;
            }
            line = candsFile.readLine();
        }
        outFile_underscorealignSrcCand_underscorephrasal.close();
        outFile_underscorealignSrcCand_underscoreword.close();
        candsFile.close();
        println("Finished processing candidates @ " + (new Date()));
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(alignCache_underscorefileName));
            out.writeObject(alreadyResolved_underscoresrcSet);
            out.writeObject(alreadyResolved_underscoretgtSet);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.err.println("IOException in AlignCandidates.main(String[]): " + e.getMessage());
            System.exit(99902);
        }
    }

