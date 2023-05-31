    public static void main(String args[]) throws IOException, TrimmerException, DataStoreException {
        Options options = new Options();
        options.addOption(new CommandLineOptionBuilder("ace", "path to ace file").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("phd", "path to phd file").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("out", "path to new ace file").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("min_underscoresanger", "min sanger end coveage default =" + DEFAULT_underscoreMIN_underscoreSANGER_underscoreEND_underscoreCLONE_underscoreCVG).build());
        options.addOption(new CommandLineOptionBuilder("min_underscorebiDriection", "min bi directional end coveage default =" + DEFAULT_underscoreMIN_underscoreBI_underscoreDIRECTIONAL_underscoreEND_underscoreCOVERAGE).build());
        options.addOption(new CommandLineOptionBuilder("ignore_underscorethreshold", "min end coveage threshold to stop trying to trim default =" + DEFAULT_underscoreIGNORE_underscoreEND_underscoreCVG_underscoreTHRESHOLD).build());
        CommandLine commandLine;
        PhdDataStore phdDataStore = null;
        AceContigDataStore datastore = null;
        try {
            commandLine = CommandLineUtils.parseCommandLine(options, args);
            int minSangerEndCloneCoverage = commandLine.hasOption("min_underscoresanger") ? Integer.parseInt(commandLine.getOptionValue("min_underscoresanger")) : DEFAULT_underscoreMIN_underscoreSANGER_underscoreEND_underscoreCLONE_underscoreCVG;
            int minBiDirectionalEndCoverage = commandLine.hasOption("min_underscorebiDriection") ? Integer.parseInt(commandLine.getOptionValue("min_underscorebiDriection")) : DEFAULT_underscoreMIN_underscoreBI_underscoreDIRECTIONAL_underscoreEND_underscoreCOVERAGE;
            int ignoreThresholdEndCoverage = commandLine.hasOption("ignore_underscorethreshold") ? Integer.parseInt(commandLine.getOptionValue("ignore_underscorethreshold")) : DEFAULT_underscoreIGNORE_underscoreEND_underscoreCVG_underscoreTHRESHOLD;
            AceContigTrimmer trimmer = new NextGenClosureAceContigTrimmer(minSangerEndCloneCoverage, minBiDirectionalEndCoverage, ignoreThresholdEndCoverage);
            File aceFile = new File(commandLine.getOptionValue("ace"));
            File phdFile = new File(commandLine.getOptionValue("phd"));
            phdDataStore = new DefaultPhdFileDataStore(phdFile);
            datastore = new IndexedAceFileDataStore(aceFile);
            File tempFile = File.createTempFile("nextGenClosureAceTrimmer", ".ace");
            tempFile.deleteOnExit();
            OutputStream tempOut = new FileOutputStream(tempFile);
            int numberOfContigs = 0;
            int numberOfTotalReads = 0;
            for (AceContig contig : datastore) {
                AceContig trimmedAceContig = trimmer.trimContig(contig);
                if (trimmedAceContig != null) {
                    numberOfContigs++;
                    numberOfTotalReads += trimmedAceContig.getNumberOfReads();
                    AceFileWriter.writeAceFile(trimmedAceContig, phdDataStore, tempOut);
                }
            }
            IOUtil.closeAndIgnoreErrors(tempOut);
            OutputStream masterAceOut = new FileOutputStream(new File(commandLine.getOptionValue("out")));
            masterAceOut.write(String.format("AS %d %d%n", numberOfContigs, numberOfTotalReads).getBytes());
            InputStream tempInput = new FileInputStream(tempFile);
            IOUtils.copy(tempInput, masterAceOut);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp(options);
        } finally {
            IOUtil.closeAndIgnoreErrors(phdDataStore, datastore);
        }
    }

