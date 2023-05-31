    @Override
    public void execute() throws ProcessorExecutionException {
        try {
            if (getSource().getPaths() == null || getSource().getPaths().size() == 0 || getDestination().getPaths() == null || getDestination().getPaths().size() == 0) {
                throw new ProcessorExecutionException("No input and/or output paths specified.");
            }
            String temp_underscoredir_underscoreprefix = getDestination().getPath().getParent().toString() + "/bcc_underscore" + getDestination().getPath().getName() + "_underscore";
            SequenceTempDirMgr dirMgr = new SequenceTempDirMgr(temp_underscoredir_underscoreprefix, context);
            dirMgr.setSeqNum(0);
            Path tmpDir;
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": Transform input to AdjSetVertex");
            Transformer transformer = new OutAdjVertex2AdjSetVertexTransformer();
            transformer.setConf(context);
            transformer.setSrcPath(getSource().getPath());
            tmpDir = dirMgr.getTempDir();
            transformer.setDestPath(tmpDir);
            transformer.setMapperNum(getMapperNum());
            transformer.setReducerNum(getReducerNum());
            transformer.execute();
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": Transform input to LabeledAdjSetVertex");
            Vertex2LabeledTransformer l_underscoretransformer = new Vertex2LabeledTransformer();
            l_underscoretransformer.setConf(context);
            l_underscoretransformer.setSrcPath(tmpDir);
            tmpDir = dirMgr.getTempDir();
            l_underscoretransformer.setDestPath(tmpDir);
            l_underscoretransformer.setMapperNum(getMapperNum());
            l_underscoretransformer.setReducerNum(getReducerNum());
            l_underscoretransformer.setOutputValueClass(LabeledAdjSetVertex.class);
            l_underscoretransformer.execute();
            Graph src;
            Graph dest;
            Path path_underscoreto_underscoreremember = tmpDir;
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": SpanningTreeRootChoose");
            src = new Graph(Graph.defaultGraph());
            src.setPath(tmpDir);
            dest = new Graph(Graph.defaultGraph());
            tmpDir = dirMgr.getTempDir();
            dest.setPath(tmpDir);
            GraphAlgorithm choose_underscoreroot = new SpanningTreeRootChoose();
            choose_underscoreroot.setConf(context);
            choose_underscoreroot.setSource(src);
            choose_underscoreroot.setDestination(dest);
            choose_underscoreroot.setMapperNum(getMapperNum());
            choose_underscoreroot.setReducerNum(getReducerNum());
            choose_underscoreroot.execute();
            Path the_underscorefile = new Path(tmpDir.toString() + "/part-00000");
            FileSystem client = FileSystem.get(context);
            if (!client.exists(the_underscorefile)) {
                throw new ProcessorExecutionException("Did not find the chosen vertex in " + the_underscorefile.toString());
            }
            FSDataInputStream input_underscorestream = client.open(the_underscorefile);
            ByteArrayOutputStream output_underscorestream = new ByteArrayOutputStream();
            IOUtils.copyBytes(input_underscorestream, output_underscorestream, context, false);
            String the_underscoreline = output_underscorestream.toString();
            String root_underscorevertex_underscoreid = the_underscoreline.substring(SpanningTreeRootChoose.SPANNING_underscoreTREE_underscoreROOT.length()).trim();
            input_underscorestream.close();
            output_underscorestream.close();
            System.out.println("++++++> Chosen the root of spanning tree = " + root_underscorevertex_underscoreid);
            while (true) {
                System.out.println("++++++>" + dirMgr.getSeqNum() + " Generate the spanning tree rooted at : (" + root_underscorevertex_underscoreid + ") from " + tmpDir);
                src = new Graph(Graph.defaultGraph());
                src.setPath(path_underscoreto_underscoreremember);
                tmpDir = dirMgr.getTempDir();
                dest = new Graph(Graph.defaultGraph());
                dest.setPath(tmpDir);
                path_underscoreto_underscoreremember = tmpDir;
                GraphAlgorithm spanning = new SpanningTreeGenerate();
                spanning.setConf(context);
                spanning.setSource(src);
                spanning.setDestination(dest);
                spanning.setMapperNum(getMapperNum());
                spanning.setReducerNum(getReducerNum());
                spanning.setParameter(ConstantLabels.ROOT_underscoreID, root_underscorevertex_underscoreid);
                spanning.execute();
                System.out.println("++++++>" + dirMgr.getSeqNum() + " Test spanning convergence");
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                tmpDir = dirMgr.getTempDir();
                dest = new Graph(Graph.defaultGraph());
                dest.setPath(tmpDir);
                GraphAlgorithm conv_underscoretester = new SpanningConvergenceTest();
                conv_underscoretester.setConf(context);
                conv_underscoretester.setSource(src);
                conv_underscoretester.setDestination(dest);
                conv_underscoretester.setMapperNum(getMapperNum());
                conv_underscoretester.setReducerNum(getReducerNum());
                conv_underscoretester.execute();
                long vertexes_underscoreout_underscoreof_underscoretree = MRConsoleReader.getMapOutputRecordNum(conv_underscoretester.getFinalStatus());
                System.out.println("++++++> number of vertexes out of the spanning tree = " + vertexes_underscoreout_underscoreof_underscoretree);
                if (vertexes_underscoreout_underscoreof_underscoretree == 0) {
                    break;
                }
            }
            System.out.println("++++++> From spanning tree to sets of edges");
            src = new Graph(Graph.defaultGraph());
            src.setPath(path_underscoreto_underscoreremember);
            tmpDir = dirMgr.getTempDir();
            dest = new Graph(Graph.defaultGraph());
            dest.setPath(tmpDir);
            GraphAlgorithm tree2set = new Tree2EdgeSet();
            tree2set.setConf(context);
            tree2set.setSource(src);
            tree2set.setDestination(dest);
            tree2set.setMapperNum(getMapperNum());
            tree2set.setReducerNum(getReducerNum());
            tree2set.execute();
            long map_underscoreinput_underscorerecords_underscorenum = -1;
            long map_underscoreoutput_underscorerecords_underscorenum = -2;
            Stack<Path> expanding_underscorestack = new Stack<Path>();
            do {
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetMinorJoin");
                GraphAlgorithm minorjoin = new EdgeSetMinorJoin();
                minorjoin.setConf(context);
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                dest = new Graph(Graph.defaultGraph());
                tmpDir = dirMgr.getTempDir();
                dest.setPath(tmpDir);
                minorjoin.setSource(src);
                minorjoin.setDestination(dest);
                minorjoin.setMapperNum(getMapperNum());
                minorjoin.setReducerNum(getReducerNum());
                minorjoin.execute();
                expanding_underscorestack.push(tmpDir);
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetJoin");
                GraphAlgorithm join = new EdgeSetJoin();
                join.setConf(context);
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                dest = new Graph(Graph.defaultGraph());
                tmpDir = dirMgr.getTempDir();
                dest.setPath(tmpDir);
                join.setSource(src);
                join.setDestination(dest);
                join.setMapperNum(getMapperNum());
                join.setReducerNum(getReducerNum());
                join.execute();
                map_underscoreinput_underscorerecords_underscorenum = MRConsoleReader.getMapInputRecordNum(join.getFinalStatus());
                map_underscoreoutput_underscorerecords_underscorenum = MRConsoleReader.getMapOutputRecordNum(join.getFinalStatus());
                System.out.println("++++++> map in/out : " + map_underscoreinput_underscorerecords_underscorenum + "/" + map_underscoreoutput_underscorerecords_underscorenum);
            } while (map_underscoreinput_underscorerecords_underscorenum != map_underscoreoutput_underscorerecords_underscorenum);
            while (expanding_underscorestack.size() > 0) {
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetExpand");
                GraphAlgorithm expand = new EdgeSetExpand();
                expand.setConf(context);
                src = new Graph(Graph.defaultGraph());
                src.addPath(expanding_underscorestack.pop());
                src.addPath(tmpDir);
                dest = new Graph(Graph.defaultGraph());
                tmpDir = dirMgr.getTempDir();
                dest.setPath(tmpDir);
                expand.setSource(src);
                expand.setDestination(dest);
                expand.setMapperNum(getMapperNum());
                expand.setReducerNum(getReducerNum());
                expand.execute();
                System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetMinorExpand");
                GraphAlgorithm minorexpand = new EdgeSetMinorExpand();
                minorexpand.setConf(context);
                src = new Graph(Graph.defaultGraph());
                src.setPath(tmpDir);
                dest = new Graph(Graph.defaultGraph());
                tmpDir = dirMgr.getTempDir();
                dest.setPath(tmpDir);
                minorexpand.setSource(src);
                minorexpand.setDestination(dest);
                minorexpand.setMapperNum(getMapperNum());
                minorexpand.setReducerNum(getReducerNum());
                minorexpand.execute();
            }
            System.out.println("++++++>" + dirMgr.getSeqNum() + ": EdgeSetSummarize");
            GraphAlgorithm summarize = new EdgeSetSummarize();
            summarize.setConf(context);
            src = new Graph(Graph.defaultGraph());
            src.setPath(tmpDir);
            dest = new Graph(Graph.defaultGraph());
            dest.setPath(getDestination().getPath());
            summarize.setSource(src);
            summarize.setDestination(dest);
            summarize.setMapperNum(getMapperNum());
            summarize.setReducerNum(getReducerNum());
            summarize.execute();
            dirMgr.deleteAll();
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        } catch (IllegalAccessException e) {
            throw new ProcessorExecutionException(e);
        }
    }

