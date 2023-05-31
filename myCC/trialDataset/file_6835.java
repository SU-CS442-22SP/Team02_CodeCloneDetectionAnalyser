    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        debug("Start execute job " + this.getClass().getName());
        String dir = this.path_underscoreapp_underscoreroot + "/" + this.properties.get("dir") + "/";
        try {
            File dir_underscoref = new File(dir);
            if (!dir_underscoref.exists()) {
                debug("(0) - make dir: " + dir_underscoref + " - ");
                org.apache.commons.io.FileUtils.forceMkdir(dir_underscoref);
            }
        } catch (IOException ex) {
            fatal("IOException", ex);
        }
        debug("Files:" + this.properties.get("url"));
        String[] url_underscoreto_underscoredownload = properties.get("url").split(";");
        for (String u : url_underscoreto_underscoredownload) {
            if (StringUtil.isNullOrEmpty(u)) {
                continue;
            }
            u = StringUtil.trim(u);
            debug("(0) url: " + u);
            String f_underscorename = u.substring(u.lastIndexOf("/"), u.length());
            debug("(1) - start download: " + u + " to file name: " + new File(dir + "/" + f_underscorename).toString());
            com.utils.HttpUtil.downloadData(u, new File(dir + "/" + f_underscorename).toString());
        }
        try {
            conn_underscorestats.setAutoCommit(false);
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        String[] query_underscoredelete = properties.get("query_underscoredelete").split(";");
        for (String q : query_underscoredelete) {
            if (StringUtil.isNullOrEmpty(q)) {
                continue;
            }
            q = StringUtil.trim(q);
            debug("(2) - " + q);
            try {
                Statement stat = conn_underscorestats.createStatement();
                stat.executeUpdate(q);
                stat.close();
            } catch (SQLException e) {
                try {
                    conn_underscorestats.rollback();
                } catch (SQLException ex) {
                    fatal("SQLException", ex);
                }
                fatal("SQLException", e);
            }
        }
        for (String u : url_underscoreto_underscoredownload) {
            if (StringUtil.isNullOrEmpty(u)) {
                continue;
            }
            u = StringUtil.trim(u);
            try {
                Statement stat = conn_underscorestats.createStatement();
                String f_underscorename = new File(dir + "/" + u.substring(u.lastIndexOf("/"), u.length())).toString();
                debug("(3) - start import: " + f_underscorename);
                BigFile lines = new BigFile(f_underscorename);
                int n = 0;
                for (String l : lines) {
                    String fields[] = l.split(",");
                    String query = "";
                    if (f_underscorename.indexOf("hip_underscorecountries.csv") != -1) {
                        query = "insert into hip_underscorecountries values (" + fields[0] + ",'" + normalize(fields[1]) + "','" + normalize(fields[2]) + "')";
                    } else if (f_underscorename.indexOf("hip_underscoreip4_underscorecity_underscorelat_underscorelng.csv") != -1) {
                        query = "insert into hip_underscoreip4_underscorecity_underscorelat_underscorelng values (" + fields[0] + ",'" + normalize(fields[1]) + "'," + fields[2] + "," + fields[3] + ")";
                    } else if (f_underscorename.indexOf("hip_underscoreip4_underscorecountry.csv") != -1) {
                        query = "insert into hip_underscoreip4_underscorecountry values (" + fields[0] + "," + fields[1] + ")";
                    }
                    debug(n + " - " + query);
                    stat.executeUpdate(query);
                    n++;
                }
                debug("(4) tot import per il file" + f_underscorename + " : " + n);
                stat.close();
                new File(f_underscorename).delete();
            } catch (SQLException ex) {
                fatal("SQLException", ex);
                try {
                    conn_underscorestats.rollback();
                } catch (SQLException ex2) {
                    fatal("SQLException", ex2);
                }
            } catch (IOException ex) {
                fatal("IOException", ex);
            } catch (Exception ex3) {
                fatal("Exception", ex3);
            }
        }
        try {
            conn_underscorestats.commit();
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        try {
            conn_underscorestats.setAutoCommit(true);
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        try {
            debug("(6) Vacuum");
            Statement stat = this.conn_underscorestats.createStatement();
            stat.executeUpdate("VACUUM");
            stat.close();
        } catch (SQLException e) {
            fatal("SQLException", e);
        }
        debug("End execute job " + this.getClass().getName());
    }

