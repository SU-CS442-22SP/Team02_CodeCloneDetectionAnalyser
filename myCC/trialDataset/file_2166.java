private ArrayList execAtParentServer(ArrayList paramList) throws Exception {
        ArrayList outputList = null;
        String message = "";
        try {
            HashMap serverUrlMap = InitXml.getInstance().getServerMap();
            Iterator it = serverUrlMap.keySet().iterator();
            while (it.hasNext()) {
                String server = (String) it.next();
                String serverUrl = (String) serverUrlMap.get(server);
                serverUrl = serverUrl + Primer3Manager.servletName;
                URL url = new URL(serverUrl);
                URLConnection uc = url.openConnection();
                uc.setDoOutput(true);
                OutputStream os = uc.getOutputStream();
                StringBuffer buf = new StringBuffer();
                buf.append("actionType=designparent");
                for (int i = 0; i < paramList.size(); i++) {
                    Primer3Param param = (Primer3Param) paramList.get(i);
                    if (i == 0) {
                        buf.append("&sequence=" + param.getSequence());
                        buf.append("&upstream_underscoresize" + upstreamSize);
                        buf.append("&downstreamSize" + downstreamSize);
                        buf.append("&MARGIN_underscoreLENGTH=" + marginLength);
                        buf.append("&OVERLAP_underscoreLENGTH=" + overlapLength);
                        buf.append("&MUST_underscoreXLATE_underscorePRODUCT_underscoreMIN_underscoreSIZE=" + param.getPrimerProductMinSize());
                        buf.append("&MUST_underscoreXLATE_underscorePRODUCT_underscoreMAX_underscoreSIZE=" + param.getPrimerProductMaxSize());
                        buf.append("&PRIMER_underscorePRODUCT_underscoreOPT_underscoreSIZE=" + param.getPrimerProductOptSize());
                        buf.append("&PRIMER_underscoreMAX_underscoreEND_underscoreSTABILITY=" + param.getPrimerMaxEndStability());
                        buf.append("&PRIMER_underscoreMAX_underscoreMISPRIMING=" + param.getPrimerMaxMispriming());
                        buf.append("&PRIMER_underscorePAIR_underscoreMAX_underscoreMISPRIMING=" + param.getPrimerPairMaxMispriming());
                        buf.append("&PRIMER_underscoreMIN_underscoreSIZE=" + param.getPrimerMinSize());
                        buf.append("&PRIMER_underscoreOPT_underscoreSIZE=" + param.getPrimerOptSize());
                        buf.append("&PRIMER_underscoreMAX_underscoreSIZE=" + param.getPrimerMaxSize());
                        buf.append("&PRIMER_underscoreMIN_underscoreTM=" + param.getPrimerMinTm());
                        buf.append("&PRIMER_underscoreOPT_underscoreTM=" + param.getPrimerOptTm());
                        buf.append("&PRIMER_underscoreMAX_underscoreTM=" + param.getPrimerMaxTm());
                        buf.append("&PRIMER_underscoreMAX_underscoreDIFF_underscoreTM=" + param.getPrimerMaxDiffTm());
                        buf.append("&PRIMER_underscoreMIN_underscoreGC=" + param.getPrimerMinGc());
                        buf.append("&PRIMER_underscoreOPT_underscoreGC_underscorePERCENT=" + param.getPrimerOptGcPercent());
                        buf.append("&PRIMER_underscoreMAX_underscoreGC=" + param.getPrimerMaxGc());
                        buf.append("&PRIMER_underscoreSELF_underscoreANY=" + param.getPrimerSelfAny());
                        buf.append("&PRIMER_underscoreSELF_underscoreEND=" + param.getPrimerSelfEnd());
                        buf.append("&PRIMER_underscoreNUM_underscoreNS_underscoreACCEPTED=" + param.getPrimerNumNsAccepted());
                        buf.append("&PRIMER_underscoreMAX_underscorePOLY_underscoreX=" + param.getPrimerMaxPolyX());
                        buf.append("&PRIMER_underscoreGC_underscoreCLAMP=" + param.getPrimerGcClamp());
                    }
                    buf.append("&target=" + param.getPrimerSequenceId() + "," + (param.getTarget())[0] + "," + (param.getTarget())[1]);
                }
                PrintStream ps = new PrintStream(os);
                ps.print(buf.toString());
                ps.close();
                ObjectInputStream ois = new ObjectInputStream(uc.getInputStream());
                outputList = (ArrayList) ois.readObject();
                ois.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if ((outputList == null || outputList.size() == 0) && message != null && message.length() > 0) {
            throw new Exception(message);
        }
        return outputList;
    }
