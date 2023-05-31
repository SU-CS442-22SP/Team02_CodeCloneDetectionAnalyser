    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        debug("Start execute job " + this.getClass().getName());
        try {
            String name = "nixspam-ip.dump.gz";
            String f = this.path_underscoreapp_underscoreroot + "/" + this.properties.get("dir") + "/";
            try {
                org.apache.commons.io.FileUtils.forceMkdir(new File(f));
            } catch (IOException ex) {
                fatal("IOException", ex);
            }
            f += "/" + name;
            String url = "http://www.dnsbl.manitu.net/download/" + name;
            debug("(1) - start download: " + url);
            com.utils.HttpUtil.downloadData(url, f);
            com.utils.IOUtil.unzip(f, f.replace(".gz", ""));
            File file_underscoreto_underscoreread = new File(f.replaceAll(".gz", ""));
            BigFile lines = null;
            try {
                lines = new BigFile(file_underscoreto_underscoreread.toString());
            } catch (Exception e) {
                fatal("Excpetion", e);
                return;
            }
            try {
                Statement stat = conn_underscoreurl.createStatement();
                stat.executeUpdate(properties.get("query_underscoredelete"));
                stat.close();
            } catch (SQLException e) {
                fatal("SQLException", e);
            }
            try {
                conn_underscoreurl.setAutoCommit(false);
            } catch (SQLException e) {
                fatal("SQLException", e);
            }
            boolean ok = true;
            int i = 0;
            for (String line : lines) {
                if (StringUtil.isEmpty(line) || line.indexOf(" ") == -1) {
                    continue;
                }
                try {
                    line = line.substring(line.indexOf(" "));
                    line = line.trim();
                    if (getIPException(line)) {
                        continue;
                    }
                    Statement stat = this.conn_underscoreurl.createStatement();
                    stat.executeUpdate("insert into blacklist(url) values('" + line + "')");
                    stat.close();
                    i++;
                } catch (SQLException e) {
                    fatal("SQLException", e);
                    try {
                        conn_underscoreurl.rollback();
                    } catch (SQLException ex) {
                        fatal("SQLException", ex);
                    }
                    ok = false;
                    break;
                }
            }
            boolean del = file_underscoreto_underscoreread.delete();
            debug("File " + file_underscoreto_underscoreread + " del:" + del);
            name = "spam-ip.com_underscore" + DateTimeUtil.getNowWithFormat("MM-dd-yyyy") + ".csv";
            f = this.path_underscoreapp_underscoreroot + "/" + this.properties.get("dir") + "/";
            org.apache.commons.io.FileUtils.forceMkdir(new File(f));
            f += "/" + name;
            url = "http://spam-ip.com/csv_underscoredump/" + name;
            debug("(2) - start download: " + url);
            com.utils.HttpUtil.downloadData(url, f);
            file_underscoreto_underscoreread = new File(f);
            try {
                lines = new BigFile(file_underscoreto_underscoreread.toString());
            } catch (Exception e) {
                fatal("Exception", e);
                return;
            }
            try {
                conn_underscoreurl.setAutoCommit(false);
            } catch (SQLException e) {
                fatal("SQLException", e);
            }
            ok = true;
            for (String line : lines) {
                if (StringUtil.isEmpty(line) || line.indexOf(" ") == -1) {
                    continue;
                }
                try {
                    line = line.split(",")[1];
                    line = line.trim();
                    if (getIPException(line)) {
                        continue;
                    }
                    Statement stat = this.conn_underscoreurl.createStatement();
                    stat.executeUpdate("insert into blacklist(url) values('" + line + "')");
                    stat.close();
                    i++;
                } catch (SQLException e) {
                    fatal("SQLException", e);
                    try {
                        conn_underscoreurl.rollback();
                    } catch (SQLException ex) {
                        fatal("SQLException", ex);
                    }
                    ok = false;
                    break;
                }
            }
            del = file_underscoreto_underscoreread.delete();
            debug("File " + file_underscoreto_underscoreread + " del:" + del);
            if (ok) {
                debug("Import della BlackList Concluso tot righe: " + i);
                try {
                    conn_underscoreurl.commit();
                } catch (SQLException e) {
                    fatal("SQLException", e);
                }
            } else {
                fatal("Problemi con la Blacklist");
            }
            try {
                conn_underscoreurl.setAutoCommit(true);
            } catch (SQLException e) {
                fatal("SQLException", e);
            }
            try {
                Statement stat = this.conn_underscoreurl.createStatement();
                stat.executeUpdate("VACUUM");
                stat.close();
            } catch (SQLException e) {
                fatal("SQLException", e);
            }
        } catch (IOException ex) {
            fatal("IOException", ex);
        }
        debug("End execute job " + this.getClass().getName());
    }

