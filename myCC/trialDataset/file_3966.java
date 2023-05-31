    protected PTask commit_underscoreresult(Result r, SyrupConnection con) throws Exception {
        try {
            int logAction = LogEntry.ENDED;
            String kk = r.context().task().key();
            if (r.in_underscore1_underscoreconsumed() && r.context().in_underscore1_underscorelink() != null) {
                sqlImpl().updateFunctions().updateInLink(kk, false, null, con);
                logAction = logAction | LogEntry.IN_underscore1;
            }
            if (r.in_underscore2_underscoreconsumed() && r.context().in_underscore2_underscorelink() != null) {
                sqlImpl().updateFunctions().updateInLink(kk, true, null, con);
                logAction = logAction | LogEntry.IN_underscore2;
            }
            if (r.out_underscore1_underscoreresult() != null && r.context().out_underscore1_underscorelink() != null) {
                sqlImpl().updateFunctions().updateOutLink(kk, false, r.out_underscore1_underscoreresult(), con);
                logAction = logAction | LogEntry.OUT_underscore1;
            }
            if (r.out_underscore2_underscoreresult() != null && r.context().out_underscore2_underscorelink() != null) {
                sqlImpl().updateFunctions().updateOutLink(kk, true, r.out_underscore2_underscoreresult(), con);
                logAction = logAction | LogEntry.OUT_underscore2;
            }
            sqlImpl().loggingFunctions().log(r.context().task().key(), logAction, con);
            boolean isParent = r.context().task().isParent();
            if (r instanceof Workflow) {
                Workflow w = (Workflow) r;
                Task[] tt = w.tasks();
                Link[] ll = w.links();
                Hashtable tkeyMap = new Hashtable();
                for (int i = 0; i < tt.length; i++) {
                    String key = sqlImpl().creationFunctions().newTask(tt[i], r.context().task(), con);
                    tkeyMap.put(tt[i], key);
                }
                for (int j = 0; j < ll.length; j++) {
                    sqlImpl().creationFunctions().newLink(ll[j], tkeyMap, con);
                }
                String in_underscorelink_underscore1 = sqlImpl().queryFunctions().readInTask(kk, false, con);
                String in_underscorelink_underscore2 = sqlImpl().queryFunctions().readInTask(kk, true, con);
                String out_underscorelink_underscore1 = sqlImpl().queryFunctions().readOutTask(kk, false, con);
                String out_underscorelink_underscore2 = sqlImpl().queryFunctions().readOutTask(kk, true, con);
                sqlImpl().updateFunctions().rewireInLink(kk, false, w.in_underscore1_underscorebinding(), tkeyMap, con);
                sqlImpl().updateFunctions().rewireInLink(kk, true, w.in_underscore2_underscorebinding(), tkeyMap, con);
                sqlImpl().updateFunctions().rewireOutLink(kk, false, w.out_underscore1_underscorebinding(), tkeyMap, con);
                sqlImpl().updateFunctions().rewireOutLink(kk, true, w.out_underscore2_underscorebinding(), tkeyMap, con);
                for (int k = 0; k < tt.length; k++) {
                    String kkey = (String) tkeyMap.get(tt[k]);
                    sqlImpl().updateFunctions().checkAndUpdateDone(kkey, con);
                }
                sqlImpl().updateFunctions().checkAndUpdateDone(in_underscorelink_underscore1, con);
                sqlImpl().updateFunctions().checkAndUpdateDone(in_underscorelink_underscore2, con);
                sqlImpl().updateFunctions().checkAndUpdateDone(out_underscorelink_underscore1, con);
                sqlImpl().updateFunctions().checkAndUpdateDone(out_underscorelink_underscore2, con);
                for (int k = 0; k < tt.length; k++) {
                    String kkey = (String) tkeyMap.get(tt[k]);
                    sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(kkey, con);
                }
                sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(in_underscorelink_underscore1, con);
                sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(in_underscorelink_underscore2, con);
                sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(out_underscorelink_underscore1, con);
                sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(out_underscorelink_underscore2, con);
                isParent = true;
            }
            sqlImpl().updateFunctions().checkAndUpdateDone(kk, con);
            sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(kk, con);
            PreparedStatement s3 = null;
            s3 = con.prepareStatementFromCache(sqlImpl().sqlStatements().updateTaskModificationStatement());
            java.util.Date dd = new java.util.Date();
            s3.setLong(1, dd.getTime());
            s3.setBoolean(2, isParent);
            s3.setString(3, r.context().task().key());
            s3.executeUpdate();
            sqlImpl().loggingFunctions().log(kk, LogEntry.ENDED, con);
            con.commit();
            return sqlImpl().queryFunctions().readPTask(kk, con);
        } finally {
            con.rollback();
        }
    }

