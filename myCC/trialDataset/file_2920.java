    public static boolean installMetricsCfg(Db db, String xmlFileName) throws Exception {
        String xmlText = FileHelper.asString(xmlFileName);
        Bundle bundle = new Bundle();
        loadMetricsCfg(bundle, xmlFileName, xmlText);
        try {
            db.begin();
            PreparedStatement psExists = db.prepareStatement("SELECT e_underscorebundle_underscoreid, xml_underscoredecl_underscorepath, xml_underscoretext FROM sdw.e_underscorebundle WHERE xml_underscoredecl_underscorepath = ?;");
            psExists.setString(1, xmlFileName);
            ResultSet rsExists = db.executeQuery(psExists);
            if (rsExists.next()) {
                db.rollback();
                return false;
            }
            PreparedStatement psId = db.prepareStatement("SELECT currval('sdw.e_underscorebundle_underscoreserial');");
            PreparedStatement psAdd = db.prepareStatement("INSERT INTO sdw.e_underscorebundle (xml_underscoredecl_underscorepath, xml_underscoretext, sdw_underscoremajor_underscoreversion, sdw_underscoreminor_underscoreversion, file_underscoremajor_underscoreversion, file_underscoreminor_underscoreversion) VALUES (?, ?, ?, ?, ?, ?);");
            psAdd.setString(1, xmlFileName);
            psAdd.setString(2, xmlText);
            FileInformation fi = bundle.getSingleFileInformation();
            if (!xmlFileName.equals(fi.filename)) throw new IllegalStateException("FileInformation bad for " + xmlFileName);
            psAdd.setInt(3, Globals.SDW_underscoreMAJOR_underscoreVER);
            psAdd.setInt(4, Globals.SDW_underscoreMINOR_underscoreVER);
            psAdd.setInt(5, fi.majorVer);
            psAdd.setInt(6, fi.minorVer);
            if (1 != db.executeUpdate(psAdd)) {
                throw new IllegalStateException("Could not add " + xmlFileName);
            }
            int bundleId = DbHelper.getIntKey(psId);
            PreparedStatement psGroupId = db.prepareStatement("SELECT currval('sdw.e_underscoremetric_underscoregroup_underscoreserial');");
            PreparedStatement psAddGroup = db.prepareStatement("INSERT INTO sdw.e_underscoremetric_underscoregroup (bundle_underscoreid, metric_underscoregroup_underscorename) VALUES (?, ?);");
            psAddGroup.setInt(1, bundleId);
            PreparedStatement psMetricId = db.prepareStatement("SELECT currval('sdw.e_underscoremetric_underscorename_underscoreserial');");
            PreparedStatement psAddMetric = db.prepareStatement("INSERT INTO sdw.e_underscoremetric_underscorename (bundle_underscoreid, metric_underscorename) VALUES (?, ?);");
            psAddMetric.setInt(1, bundleId);
            PreparedStatement psAddGroup2Metric = db.prepareStatement("INSERT INTO sdw.e_underscoremetric_underscoregroups (metric_underscorename_underscoreid, metric_underscoregroup_underscoreid) VALUES (?, ?);");
            Iterator<MetricGroup> i = bundle.getAllMetricGroups();
            while (i.hasNext()) {
                MetricGroup grp = i.next();
                psAddGroup.setString(2, grp.groupName);
                if (1 != db.executeUpdate(psAddGroup)) throw new IllegalStateException("Could not add group " + grp.groupName + " from " + xmlFileName);
                int groupId = DbHelper.getIntKey(psGroupId);
                psAddGroup2Metric.setInt(2, groupId);
                Iterator<String> j = grp.getAllMetricNames();
                while (j.hasNext()) {
                    String metricName = j.next();
                    psAddMetric.setString(2, metricName);
                    if (1 != db.executeUpdate(psAddMetric)) throw new IllegalStateException("Could not add " + metricName + " from " + xmlFileName);
                    int metricId = DbHelper.getIntKey(psMetricId);
                    psAddGroup2Metric.setInt(1, metricId);
                    if (1 != db.executeUpdate(psAddGroup2Metric)) throw new IllegalStateException("Could not add group " + grp.groupName + " -> " + metricName + " from " + xmlFileName);
                }
            }
            return true;
        } catch (Exception e) {
            db.rollback();
            throw e;
        } finally {
            db.commitUnless();
        }
    }

