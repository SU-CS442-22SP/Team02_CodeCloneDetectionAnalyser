    private String choosePivotVertex() throws ProcessorExecutionException {
        String result = null;
        Graph src;
        Graph dest;
        Path tmpDir;
        System.out.println("##########>" + _underscoredirMgr.getSeqNum() + " Choose the pivot vertex");
        src = new Graph(Graph.defaultGraph());
        src.setPath(_underscorecurr_underscorepath);
        dest = new Graph(Graph.defaultGraph());
        try {
            tmpDir = _underscoredirMgr.getTempDir();
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        }
        dest.setPath(tmpDir);
        GraphAlgorithm choose_underscorepivot = new PivotChoose();
        choose_underscorepivot.setConf(context);
        choose_underscorepivot.setSource(src);
        choose_underscorepivot.setDestination(dest);
        choose_underscorepivot.setMapperNum(getMapperNum());
        choose_underscorepivot.setReducerNum(getReducerNum());
        choose_underscorepivot.execute();
        try {
            Path the_underscorefile = new Path(tmpDir.toString() + "/part-00000");
            FileSystem client = FileSystem.get(context);
            if (!client.exists(the_underscorefile)) {
                throw new ProcessorExecutionException("Did not find the chosen vertex in " + the_underscorefile.toString());
            }
            FSDataInputStream input_underscorestream = client.open(the_underscorefile);
            ByteArrayOutputStream output_underscorestream = new ByteArrayOutputStream();
            IOUtils.copyBytes(input_underscorestream, output_underscorestream, context, false);
            String the_underscoreline = output_underscorestream.toString();
            result = the_underscoreline.substring(PivotChoose.KEY_underscorePIVOT.length()).trim();
            input_underscorestream.close();
            output_underscorestream.close();
            System.out.println("##########> Chosen pivot id = " + result);
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        }
        return result;
    }

