    public String drive() {
        logger.info("\n");
        logger.info("===========================================================");
        logger.info("========== Start drive method =============================");
        logger.info("===========================================================");
        logger.entering(cl, "drive");
        xstream = new XStream(new JsonHierarchicalStreamDriver());
        xstream.setMode(XStream.NO_underscoreREFERENCES);
        xstream.alias("AuditDiffFacade", AuditDiffFacade.class);
        File auditSchemaFile = null;
        File auditSchemaXsdFile = null;
        try {
            if (configFile == null) {
                logger.severe("Request Failed: configFile is null");
                return null;
            } else {
                if (configFile.getAuditSchemaFile() != null) {
                    logger.info("auditSchemaFile=" + configFile.getAuditSchemaFile());
                    logger.info("auditSchemaXsdFile=" + configFile.getAuditSchemaXsdFile());
                    logger.info("plnXpathFile=" + configFile.getPlnXpathFile());
                    logger.info("auditSchemaFileDir=" + configFile.getAuditSchemaFileDir());
                    logger.info("auditReportFile=" + configFile.getAuditReportFile());
                    logger.info("auditReportXsdFile=" + configFile.getAuditReportXsdFile());
                } else {
                    logger.severe("Request Failed: auditSchemaFile is null");
                    return null;
                }
            }
            File test = new File(configFile.getAuditSchemaFileDir() + File.separator + "temp.xml");
            auditSchemaFile = new File(configFile.getAuditSchemaFile());
            if (!auditSchemaFile.exists() || auditSchemaFile.length() == 0L) {
                logger.severe("Request Failed: the audit schema file does not exist or empty");
                return null;
            }
            auditSchemaXsdFile = null;
            if (configFile.getAuditSchemaXsdFile() != null) {
                auditSchemaXsdFile = new File(configFile.getAuditSchemaXsdFile());
            } else {
                logger.severe("Request Failed: the audit schema xsd file is null");
                return null;
            }
            if (!auditSchemaXsdFile.exists() || auditSchemaXsdFile.length() == 0L) {
                logger.severe("Request Failed: the audit schema xsd file does not exist or empty");
                return null;
            }
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_underscoreXML_underscoreSCHEMA_underscoreNS_underscoreURI);
            Schema schema = factory.newSchema(auditSchemaXsdFile);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(auditSchemaFile);
            validator.validate(source);
        } catch (SAXException e) {
            logger.warning("SAXException caught trying to validate input Schema Files: ");
            e.printStackTrace();
        } catch (IOException e) {
            logger.warning("IOException caught trying to read input Schema File: ");
            e.printStackTrace();
        }
        String xPathFile = null;
        if (configFile.getPlnXpathFile() != null) {
            xPathFile = configFile.getPlnXpathFile();
            logger.info("Attempting to retrieve xpaths from file: '" + xPathFile + "'");
            XpathUtility.readFile(xPathFile);
        } else {
            logger.severe("Configuration file does not have a value for the Xpath Filename");
            return null;
        }
        Properties xpathProps = XpathUtility.getXpathsProps();
        if (xpathProps == null) {
            logger.severe("No Xpaths could be extracted from file: '" + xPathFile + "' - xpath properties object is null");
            return null;
        }
        if (xpathProps.isEmpty()) {
            logger.severe("No Xpaths could be extracted from file: '" + xPathFile + "' - xpath properties object is empty");
            return null;
        }
        logger.info(xpathProps.size() + " xpaths retrieved.");
        for (String key : xpathProps.stringPropertyNames()) {
            logger.info("Key=" + key + "   Value=" + xpathProps.getProperty(key));
        }
        logger.info("\n");
        logger.info("===========================================================");
        logger.info("========== Process XML Schema File BEGIN ==================");
        logger.info("===========================================================");
        SchemaSAXReader sax = new SchemaSAXReader();
        ArrayList<String> key_underscorematches = new ArrayList<String>(sax.parseDocument(auditSchemaFile, xpathProps));
        logger.info("Check Input xpath hash against xpaths found in Schema.");
        Comparison comp_underscorekeys = new Comparison();
        ArrayList<String> in_underscorexpath_underscorenot_underscorein_underscoreschema = new ArrayList<String>(comp_underscorekeys.keys_underscorenot_underscorein_underscoreboth_underscorehashes(xpathProps, Utility.arraylist_underscoreto_underscoremap(key_underscorematches, "key_underscorematches"), "xpath Properties", "hm_underscorekey_underscorematches"));
        if (in_underscorexpath_underscorenot_underscorein_underscoreschema.size() > 0) {
            logger.severe("All XPaths in Input xpath Properties list were not found in Schema.");
            logger.severe("Xpaths in xpath Properties list missing from schema file:" + xstream.toXML(in_underscorexpath_underscorenot_underscorein_underscoreschema));
            logger.severe("Quitting.");
            return null;
        }
        Map<String, Map> schema_underscoreaudit_underscorehashbox = sax.get_underscoreaudit_underscorehashbox();
        logger.info("schema_underscoreaudit_underscorehashbox\n" + xstream.toXML(schema_underscoreaudit_underscorehashbox));
        Map<String, Map> schema_underscorenetwork_underscorehashbox = sax.get_underscorenet_underscorehashbox();
        logger.info("schema_underscorenetwork_underscorehashbox\n" + xstream.toXML(schema_underscorenetwork_underscorehashbox));
        Map<String, Map> schema_underscorehost_underscorehashbox = sax.get_underscorehost_underscorehashbox();
        Map<String, Map> schema_underscoreau_underscorehashbox = sax.get_underscoreau_underscorehashbox();
        logger.info("schema_underscoreau_underscorehashbox\n" + xstream.toXML(schema_underscoreau_underscorehashbox));
        Hasherator hr = new Hasherator();
        Set<String> s_underscorehost_underscorehb_underscoreadditions = new HashSet<String>();
        s_underscorehost_underscorehb_underscoreadditions.add("/SSP/network/@network_underscoreid");
        schema_underscorehost_underscorehashbox = hr.copy_underscorehashbox_underscoreentries(schema_underscorenetwork_underscorehashbox, schema_underscorehost_underscorehashbox, s_underscorehost_underscorehb_underscoreadditions);
        logger.info("schema_underscorehost_underscorehashbox(after adding network name)\n" + xstream.toXML(schema_underscorehost_underscorehashbox));
        Map<String, String> transforms_underscores_underscoreau_underscorehb = new HashMap<String, String>();
        transforms_underscores_underscoreau_underscorehb.put("/SSP/archivalUnits/au/auCapabilities/storageRequired/@max_underscoresize", "s_underscoregigabytes_underscoreto_underscorestring_underscorebytes_underscoreunformatted()");
        schema_underscoreau_underscorehashbox = hr.convert_underscorehashbox_underscorevals(schema_underscoreau_underscorehashbox, transforms_underscores_underscoreau_underscorehb);
        Map<String, String> transforms_underscores_underscorehost_underscorehb = new HashMap<String, String>();
        transforms_underscores_underscorehost_underscorehb.put("/SSP/hosts/host/hostCapabilities/storageAvailable/@max_underscoresize", "s_underscoregigabytes_underscoreto_underscorestring_underscorebytes_underscoreunformatted()");
        schema_underscorehost_underscorehashbox = hr.convert_underscorehashbox_underscorevals(schema_underscorehost_underscorehashbox, transforms_underscores_underscorehost_underscorehb);
        logger.info("schema_underscorehost_underscorehashbox(after transformations)\n" + xstream.toXML(schema_underscorehost_underscorehashbox));
        logger.info("\n");
        logger.info("========== Process Schema  END ============================");
        logger.info("\n");
        logger.info("========== Database Operations ============================");
        MYSQLWorkPlnHostSummaryDAO daowphs = new MYSQLWorkPlnHostSummaryDAO();
        daowphs.drop();
        daowphs.create();
        daowphs.updateTimestamp();
        CachedRowSet rs_underscoreq0_underscoreN = daowphs.query_underscore0_underscoreN();
        double d_underscorespace_underscoretotal = DBUtil.get_underscoresingle_underscoredb_underscoredouble_underscorevalue(rs_underscoreq0_underscoreN, "net_underscoresum_underscorerepo_underscoresize");
        double d_underscorespace_underscoreused = DBUtil.get_underscoresingle_underscoredb_underscoredouble_underscorevalue(rs_underscoreq0_underscoreN, "net_underscoresum_underscoreused_underscorespace");
        double d_underscorespace_underscorefree = d_underscorespace_underscoretotal - d_underscorespace_underscoreused;
        double d_underscoreavg_underscoreuptime = DBUtil.get_underscoresingle_underscoredb_underscoredouble_underscorevalue(rs_underscoreq0_underscoreN, "net_underscoreavg_underscoreuptime");
        long space_underscoretotal = (long) d_underscorespace_underscoretotal;
        long space_underscoreused = (long) d_underscorespace_underscoreused;
        long space_underscorefree = space_underscoretotal - space_underscoreused;
        String f_underscorespace_underscoretotal = Utility.l_underscorebytes_underscoreto_underscoreother_underscoreunits_underscoreformatted(space_underscoretotal, 3, "T");
        String f_underscorespace_underscoreused = Utility.l_underscorebytes_underscoreto_underscoreother_underscoreunits_underscoreformatted(space_underscoreused, 3, "G");
        String f_underscorespace_underscorefree = Utility.l_underscorebytes_underscoreto_underscoreother_underscoreunits_underscoreformatted(space_underscorefree, 3, "T");
        String f_underscorespace_underscorefree2 = Utility.l_underscorebytes_underscoreto_underscoreother_underscoreunits_underscoreformatted(space_underscorefree, 3, null);
        logger.info("d_underscorespace_underscoretotal: " + d_underscorespace_underscoretotal + "\n" + "d_underscorespace_underscoreused: " + d_underscorespace_underscoreused + "\n" + "space_underscoretotal: " + space_underscoretotal + "\n" + "space_underscoreused: " + space_underscoreused + "\n" + "space_underscorefree: " + space_underscorefree + "\n\n" + "Double.toString( d_underscorespace_underscoretotal ): " + Double.toString(d_underscorespace_underscoretotal) + "\n\n" + "f_underscorespace_underscoretotal: " + f_underscorespace_underscoretotal + "\n" + "f_underscorespace_underscoreused: " + f_underscorespace_underscoreused + "\n" + "f_underscorespace_underscorefree: " + f_underscorespace_underscorefree + "\n" + "f_underscorespace_underscorefree2: " + f_underscorespace_underscorefree2);
        rprtCnst = new ReportData();
        logger.info("\n");
        logger.info("========== Load Report Constants from Calculations ===========");
        rprtCnst.addKV("REPORT_underscoreHOSTS_underscoreTOTAL_underscoreDISKSPACE", f_underscorespace_underscoretotal);
        rprtCnst.addKV("REPORT_underscoreHOSTS_underscoreTOTAL_underscoreDISKSPACE_underscoreUSED", f_underscorespace_underscoreused);
        rprtCnst.addKV("REPORT_underscoreHOSTS_underscoreTOTAL_underscoreDISKSPACE_underscoreFREE", f_underscorespace_underscorefree);
        rprtCnst.addKV("REPORT_underscoreHOSTS_underscoreMEAN_underscoreUPTIME", Utility.ms_underscoreto_underscoredd_underscorehh_underscoremm_underscoress_underscoreformatted((long) d_underscoreavg_underscoreuptime));
        logger.info("r=\n" + rprtCnst.toString());
        logger.info("\n");
        logger.info("========== Load Report Constants from ConfigFile =============");
        rprtCnst.addKV("REPORT_underscoreFILENAME_underscoreSCHEMA_underscoreFILENAME", configFile.getAuditSchemaFile());
        rprtCnst.addKV("REPORT_underscoreFILENAME_underscoreSCHEMA_underscoreFILE_underscoreXSD_underscoreFILENAME", configFile.getAuditSchemaXsdFile());
        rprtCnst.addKV("REPORT_underscoreFILENAME_underscoreXML_underscoreDIFF_underscoreFILENAME", configFile.getAuditReportFile());
        rprtCnst.addKV("REPORT_underscoreFILENAME_underscoreXML_underscoreDIFF_underscoreFILE_underscoreXSD_underscoreFILENAME", configFile.getAuditReportXsdFile());
        logger.info("\n");
        logger.info("========== Load Report Constants from Hashboxes ==============");
        Set auditHBKeySet = hr.getMapKeyset(schema_underscoreaudit_underscorehashbox, "schema_underscoreaudit_underscorehashbox");
        String audit_underscoreid = hr.singleKeysetEntryToString(auditHBKeySet);
        logger.info("audit_underscoreid: " + audit_underscoreid);
        Set networkHBKeySet = hr.getMapKeyset(schema_underscorenetwork_underscorehashbox, "schema_underscorenetwork_underscorehashbox");
        String network_underscoreid = hr.singleKeysetEntryToString(networkHBKeySet);
        logger.info("network_underscoreid: " + network_underscoreid);
        rprtCnst.addKV("REPORT_underscoreAUDIT_underscoreID", audit_underscoreid);
        rprtCnst.addKV("REPORT_underscoreAUDIT_underscoreREPORT_underscoreEMAIL", hr.extractSingleValueFromHashbox(schema_underscoreaudit_underscorehashbox, "schema_underscoreaudit_underscorehashbox", audit_underscoreid, "/SSP/audit/auditReportEmail"));
        rprtCnst.addKV("REPORT_underscoreAUDIT_underscoreINTERVAL", hr.extractSingleValueFromHashbox(schema_underscoreaudit_underscorehashbox, "schema_underscoreaudit_underscorehashbox", audit_underscoreid, "/SSP/audit/auditReportInterval/@maxDays"));
        rprtCnst.addKV("REPORT_underscoreSCHEMA_underscoreVERSION", hr.extractSingleValueFromHashbox(schema_underscoreaudit_underscorehashbox, "schema_underscoreaudit_underscorehashbox", audit_underscoreid, "/SSP/audit/schemaVersion"));
        rprtCnst.addKV("REPORT_underscoreCLASSIFICATION_underscoreGEOGRAPHIC_underscoreSUMMARY_underscoreSCHEME", hr.extractSingleValueFromHashbox(schema_underscoreaudit_underscorehashbox, "schema_underscoreaudit_underscorehashbox", audit_underscoreid, "/SSP/audit/geographicSummaryScheme"));
        rprtCnst.addKV("REPORT_underscoreCLASSIFICATION_underscoreSUBJECT_underscoreSUMMARY_underscoreSCHEME", hr.extractSingleValueFromHashbox(schema_underscoreaudit_underscorehashbox, "schema_underscoreaudit_underscorehashbox", audit_underscoreid, "/SSP/audit/subjectSummaryScheme"));
        rprtCnst.addKV("REPORT_underscoreCLASSIFICATION_underscoreOWNER_underscoreINSTITUTION_underscoreSUMMARY_underscoreSCHEME", hr.extractSingleValueFromHashbox(schema_underscoreaudit_underscorehashbox, "schema_underscoreaudit_underscorehashbox", audit_underscoreid, "/SSP/audit/ownerInstSummaryScheme"));
        rprtCnst.addKV("REPORT_underscoreNETWORK_underscoreID", network_underscoreid);
        rprtCnst.addKV("REPORT_underscoreNETWORK_underscoreADMIN_underscoreEMAIL", hr.extractSingleValueFromHashbox(schema_underscorenetwork_underscorehashbox, "schema_underscorenetwork_underscorehashbox", network_underscoreid, "/SSP/network/networkIdentity/accessBase/@adminEmail"));
        rprtCnst.addKV("REPORT_underscoreGEOGRAPHIC_underscoreCODING", hr.extractSingleValueFromHashbox(schema_underscorenetwork_underscorehashbox, "schema_underscorenetwork_underscorehashbox", network_underscoreid, "/SSP/network/networkIdentity/geographicCoding"));
        logger.info("\n");
        logger.info("===========================================================");
        logger.info("========== Process Network Data BEGIN======================");
        logger.info("===========================================================");
        Set<String> tableSet0 = reportAuOverviewFacade.findAllTables();
        String reportAuOverviewTable = "report_underscoreau_underscoreoverview";
        int n_underscoretabs = 0;
        if (tableSet0 != null && !tableSet0.isEmpty()) {
            logger.fine("Table List N=" + tableSet0.size());
            for (String tableName : tableSet0) {
                n_underscoretabs++;
                if (tableName.equalsIgnoreCase(reportAuOverviewTable)) {
                    logger.fine(n_underscoretabs + " " + tableName + " <--");
                } else {
                    logger.fine(n_underscoretabs + " " + tableName);
                }
            }
        } else {
            logger.fine("No tables found in DB.");
        }
        if (!tableSet0.contains(reportAuOverviewTable)) {
            logger.info("Database does not contain table '" + reportAuOverviewTable + "'");
        }
        List<ReportAuOverview> repAuOvTabAllData = null;
        repAuOvTabAllData = reportAuOverviewFacade.findAll();
        if (repAuOvTabAllData != null && !(repAuOvTabAllData.isEmpty())) {
            logger.fine("\n" + reportAuOverviewTable + " table has " + repAuOvTabAllData.size() + " rows.");
            int n_underscorerows = 0;
            for (ReportAuOverview row : repAuOvTabAllData) {
                n_underscorerows++;
                logger.fine(n_underscorerows + " " + row.toString());
            }
        } else {
            logger.fine(reportAuOverviewTable + " is null, empty, or nonexistent.");
        }
        logger.fine("report_underscoreau_underscoreoverview Table xstream Dump:\n" + xstream.toXML(repAuOvTabAllData));
        logger.fine("\n");
        logger.fine("Iterate over repAuOvTabAllData 2");
        Iterator it = repAuOvTabAllData.iterator();
        int n_underscoreel = 0;
        while (it.hasNext()) {
            ++n_underscoreel;
            String el = it.next().toString();
            logger.fine(n_underscoreel + ". " + el);
        }
        Class aClass = edu.harvard.iq.safe.saasystem.entities.ReportAuOverview.class;
        String reportAuOverviewTableName = reportAuOverviewFacade.getTableName();
        logger.fine("\n");
        logger.fine("EntityManager Tests");
        logger.fine("Table: " + reportAuOverviewTableName);
        logger.fine("\n");
        logger.fine("Schema: " + reportAuOverviewFacade.getSchema());
        logger.fine("\n");
        Set columnList = reportAuOverviewFacade.getColumnList(reportAuOverviewFacade.getTableName());
        logger.fine("Columns (fields) in table '" + reportAuOverviewTableName + "' (N=" + columnList.size() + ")");
        Set<String> colList = new HashSet();
        Iterator colNames = columnList.iterator();
        int n_underscoreel2 = 0;
        while (colNames.hasNext()) {
            ++n_underscoreel2;
            String el = colNames.next().toString();
            logger.fine(n_underscoreel2 + ". " + el);
            colList.add(el);
        }
        logger.fine(colList.size() + " entries in Set 'colList' ");
        logger.info("========== Query 'au_underscoreoverview_underscoretable'=============");
        MySQLAuOverviewDAO daoao = new MySQLAuOverviewDAO();
        CachedRowSet rs_underscoreq1_underscoreA = daoao.query_underscoreq1_underscoreA();
        int[] au_underscoretable_underscorerc = DBUtil.get_underscorers_underscoredims(rs_underscoreq1_underscoreA);
        logger.info("Au Table Query ResultSet has " + au_underscoretable_underscorerc[0] + " rows and " + au_underscoretable_underscorerc[1] + " columns.");
        rprtCnst.addKV("REPORT_underscoreN_underscoreAUS_underscoreIN_underscoreNETWORK", Integer.toString(au_underscoretable_underscorerc[0]));
        logger.info("========== Create 'network_underscoreau_underscorehashbox' ==========");
        Map<String, Map> network_underscoreau_underscorehashbox = new TreeMap<String, Map>(DBUtil.rs_underscoreto_underscorehashbox(rs_underscoreq1_underscoreA, null, "au_underscoreid"));
        logger.info("network_underscoreau_underscorehashbox before transformations\n" + xstream.toXML(network_underscoreau_underscorehashbox));
        Map<String, String> transforms_underscoren_underscoreau_underscorehb = new HashMap<String, String>();
        transforms_underscoren_underscoreau_underscorehb.put("last_underscores_underscorecrawl_underscoreend", "ms_underscoreto_underscoredecimal_underscoredays_underscoreelapsed()");
        transforms_underscoren_underscoreau_underscorehb.put("last_underscores_underscorepoll_underscoreend", "ms_underscoreto_underscoredecimal_underscoredays_underscoreelapsed()");
        transforms_underscoren_underscoreau_underscorehb.put("crawl_underscoreduration", "ms_underscoreto_underscoredecimal_underscoredays()");
        network_underscoreau_underscorehashbox = hr.convert_underscorehashbox_underscorevals(network_underscoreau_underscorehashbox, transforms_underscoren_underscoreau_underscorehb);
        Map<String, String> auNVerifiedRegions = reportAuOverviewFacade.getAuNVerifiedRegions();
        logger.fine("auNVerifiedRegions\n" + xstream.toXML(auNVerifiedRegions));
        network_underscoreau_underscorehashbox = hr.addNewInnerHashEntriesToHashbox(network_underscoreau_underscorehashbox, auNVerifiedRegions, "au_underscoren_underscoreverified_underscoreregions");
        logger.info("network_underscoreau_underscorehashbox after Transformations and Addition of 'au_underscoren_underscoreverified_underscoreregions'" + xstream.toXML(network_underscoreau_underscorehashbox));
        logger.info("========== Compare AUs BEGIN ==============================");
        ArrayList<String> al_underscoreaus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork = new ArrayList<String>(comp_underscorekeys.keys_underscorenot_underscorein_underscoreboth_underscorehashes(schema_underscoreau_underscorehashbox, network_underscoreau_underscorehashbox, "schema_underscoreaus", "network_underscoreaus"));
        Map<String, String> h_underscoreaus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork = hr.get_underscorenames_underscorefrom_underscoreid_underscorelist(schema_underscoreau_underscorehashbox, al_underscoreaus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork, "/SSP/archivalUnits/au/auIdentity/name");
        rprtCnst.addKV("REPORT_underscoreN_underscoreAUS_underscoreIN_underscoreSCHEMA_underscoreNOT_underscoreIN_underscoreNETWORK", Integer.toString(al_underscoreaus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork.size()));
        rprtCnst.set_underscoreh_underscoreaus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork(h_underscoreaus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork);
        MYSQLReportAusInSchemaNotInNetworkDAO daoraisnin = new MYSQLReportAusInSchemaNotInNetworkDAO();
        daoraisnin.create();
        daoraisnin.update(h_underscoreaus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork);
        ArrayList<String> al_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema = new ArrayList<String>(comp_underscorekeys.keys_underscorenot_underscorein_underscoreboth_underscorehashes(network_underscoreau_underscorehashbox, schema_underscoreau_underscorehashbox, "network_underscoreaus", "schema_underscoreaus"));
        Utility.print_underscorearraylist(al_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema, "aus in_underscorenetwork_underscorenot_underscorein_underscoreschema");
        Map<String, String> h_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema = hr.get_underscorenames_underscorefrom_underscoreid_underscorelist(network_underscoreau_underscorehashbox, al_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema, "au_underscorename");
        rprtCnst.addKV("REPORT_underscoreN_underscoreAUS_underscoreIN_underscoreNETWORK_underscoreNOT_underscoreIN_underscoreSCHEMA", Integer.toString(al_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema.size()));
        rprtCnst.set_underscoreh_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema(h_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema);
        MYSQLReportAusInNetworkNotInSchemaDAO daorainnis = new MYSQLReportAusInNetworkNotInSchemaDAO();
        daorainnis.create();
        daorainnis.update(h_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema);
        Comparison comp_underscoreau = new Comparison(schema_underscoreau_underscorehashbox, "Schema_underscoreAU", network_underscoreau_underscorehashbox, "Network_underscoreAU", XpathUtility.getXpathToDbColumnMap(), XpathUtility.getXpathToCompOpMap());
        comp_underscoreau.init();
        logger.info("Attempting to create DB table 'lockss_underscoreaudit.audit_underscoreresults_underscoreau'");
        MYSQLAuditResultsAuDAO daoara = new MYSQLAuditResultsAuDAO();
        daoara.create();
        String results_underscoretable_underscoreau = "audit_underscoreresults_underscoreau";
        String sql_underscorevals_underscoreau_underscoreschema = comp_underscoreau.iterate_underscorehbs_underscoreau(daoara, results_underscoretable_underscoreau, "au", h_underscoreaus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema);
        CachedRowSet rs_underscoreRA2 = daoara.query_underscoreq1_underscoreRA();
        String n_underscoreaus_underscorenot_underscoreverified = DBUtil.get_underscoresingle_underscorecount_underscorefrom_underscorers(rs_underscoreRA2);
        rprtCnst.addKV("REPORT_underscoreN_underscoreAUS_underscoreNOT_underscoreVERIFIED", DBUtil.get_underscoresingle_underscorecount_underscorefrom_underscorers(rs_underscoreRA2));
        logger.info("\nInstantiating Result Class from main()");
        DiffResult result = new DiffResult();
        Map au_underscorecomp_underscorehost = result.get_underscoreresult_underscorehash("au");
        logger.info("========== Compare AUs END ================================");
        logger.info("========== Process Network Host Table =====================");
        logger.info("========== Query 'lockss_underscorebox_underscoretable' and =========");
        logger.info("================ 'repository_underscorespace_underscoretable' =======\n");
        MySQLLockssBoxRepositorySpaceDAO daolbrs = new MySQLLockssBoxRepositorySpaceDAO();
        CachedRowSet rs_underscoreq1_underscoreH = daolbrs.query_underscoreq1_underscoreH();
        int[] host_underscoretable_underscorerc = DBUtil.get_underscorers_underscoredims(rs_underscoreq1_underscoreH);
        logger.info("Host Table Query ResultSet has " + host_underscoretable_underscorerc[0] + " rows and " + host_underscoretable_underscorerc[1] + " columns.");
        rprtCnst.addKV("REPORT_underscoreN_underscoreHOSTS_underscoreIN_underscoreNETWORK", Integer.toString(host_underscoretable_underscorerc[0]));
        Long numberOfMemberHosts;
        if (StringUtils.isNotBlank(saasConfigurationRegistry.getSaasConfigProperties().getProperty("saas.ip.fromlockssxml"))) {
            numberOfMemberHosts = Long.parseLong(Integer.toString(saasConfigurationRegistry.getSaasConfigProperties().getProperty("saas.ip.fromlockssxml").split(",").length));
        } else {
            if (StringUtils.isNotBlank(saasConfigurationRegistry.getSaasAuditConfigProperties().getProperty("saas.targetIp"))) {
                numberOfMemberHosts = Long.parseLong(Integer.toString(saasConfigurationRegistry.getSaasAuditConfigProperties().getProperty("saas.targetIp").split(",").length));
            } else {
                numberOfMemberHosts = 0L;
            }
        }
        rprtCnst.addKV("REPORT_underscoreN_underscoreHOSTS_underscoreIN_underscoreNETWORK_underscore2", Long.toString(numberOfMemberHosts));
        Long numberOfReachableHosts;
        numberOfReachableHosts = lockssBoxFacade.getTotalHosts();
        rprtCnst.addKV("REPORT_underscoreN_underscoreHOSTS_underscoreIN_underscoreNETWORK_underscoreREACHABLE", Long.toString(numberOfReachableHosts));
        Map<String, Map> network_underscorehost_underscorehashbox = new TreeMap<String, Map>(DBUtil.rs_underscoreto_underscorehashbox(rs_underscoreq1_underscoreH, null, "ip_underscoreaddress"));
        logger.info("network_underscorehost_underscorehashbox before transformations\n" + xstream.toXML(network_underscorehost_underscorehashbox));
        Map<String, String> transforms_underscoren_underscorehost_underscorehb = new HashMap<String, String>();
        transforms_underscoren_underscorehost_underscorehb.put("repo_underscoresize", "SciToStr2()");
        transforms_underscoren_underscorehost_underscorehb.put("used_underscorespace", "SciToStr2()");
        network_underscorehost_underscorehashbox = hr.convert_underscorehashbox_underscorevals(network_underscorehost_underscorehashbox, transforms_underscoren_underscorehost_underscorehb);
        logger.info("network_underscorehost_underscorehashbox(after transformations)\n" + xstream.toXML(network_underscorehost_underscorehashbox));
        Map<String, String> network_underscorehost_underscorehb_underscoresel_underscoreused_underscorespace = hr.join_underscorehash_underscorepk_underscoreto_underscoreinner_underscorehash_underscorevalue(network_underscorehost_underscorehashbox, "used_underscorespace");
        Map<String, String> schema_underscorehost_underscorehb_underscoresel_underscoresize = hr.join_underscorehash_underscorepk_underscoreto_underscoreinner_underscorehash_underscorevalue(schema_underscorehost_underscorehashbox, "/SSP/hosts/host/hostCapabilities/storageAvailable/@max_underscoresize");
        logger.info("\n========== Process Network  END ===========================");
        logger.info("========== Compare Key Sets (IDs)==========================");
        Set<String> sa_underscorehb_underscorekeys = hr.gen_underscorehash_underscorekeyset(schema_underscoreau_underscorehashbox, "schema_underscoreau_underscorehashbox");
        hr.set_underscorehash_underscorekeyset(sa_underscorehb_underscorekeys, "s_underscoreau_underscorehb");
        Set<String> sh_underscorehb_underscorekeys = hr.gen_underscorehash_underscorekeyset(schema_underscorehost_underscorehashbox, "schema_underscorehost_underscorehashbox");
        hr.set_underscorehash_underscorekeyset(sh_underscorehb_underscorekeys, "s_underscoreh_underscorehb");
        Set<String> na_underscorehb_underscorekeys = hr.gen_underscorehash_underscorekeyset(network_underscoreau_underscorehashbox, "network_underscoreau_underscorehashbox");
        hr.set_underscorehash_underscorekeyset(na_underscorehb_underscorekeys, "n_underscoreau_underscorehb");
        Set<String> nh_underscorehb_underscorekeys = hr.gen_underscorehash_underscorekeyset(network_underscorehost_underscorehashbox, "network_underscorehost_underscorehashbox");
        hr.set_underscorehash_underscorekeyset(nh_underscorehb_underscorekeys, "n_underscoreh_underscorehb");
        Set<String> aus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork = new TreeSet<String>(hr.get_underscorehash_underscorekeyset("s_underscoreau_underscorehb"));
        aus_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork.removeAll(hr.get_underscorehash_underscorekeyset("n_underscoreau_underscorehb"));
        Set<String> aus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema = new TreeSet<String>(hr.get_underscorehash_underscorekeyset("n_underscoreau_underscorehb"));
        aus_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema.removeAll(hr.get_underscorehash_underscorekeyset("s_underscoreau_underscorehb"));
        Set<String> symmetricDiff = new HashSet<String>(hr.get_underscorehash_underscorekeyset("s_underscoreau_underscorehb"));
        symmetricDiff.addAll(hr.get_underscorehash_underscorekeyset("n_underscoreau_underscorehb"));
        Set<String> tmp = new HashSet<String>(hr.get_underscorehash_underscorekeyset("s_underscoreau_underscorehb"));
        tmp.retainAll(hr.get_underscorehash_underscorekeyset("n_underscoreau_underscorehb"));
        symmetricDiff.removeAll(tmp);
        Set<String> hosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema = new TreeSet<String>(hr.get_underscorehash_underscorekeyset("n_underscoreh_underscorehb"));
        hosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema.removeAll(hr.get_underscorehash_underscorekeyset("s_underscoreh_underscorehb"));
        Set<String> hosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork = new TreeSet<String>(hr.get_underscorehash_underscorekeyset("s_underscoreh_underscorehb"));
        hosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork.removeAll(hr.get_underscorehash_underscorekeyset("n_underscoreh_underscorehb"));
        ArrayList<String> al_underscorehosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork = new ArrayList<String>(comp_underscorekeys.keys_underscorenot_underscorein_underscoreboth_underscorehashes(schema_underscorehost_underscorehashbox, network_underscorehost_underscorehashbox, "schema_underscorehosts", "network_underscorehosts"));
        Map<String, String> h_underscorehosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork = hr.get_underscorenames_underscorefrom_underscoreid_underscorelist(schema_underscorehost_underscorehashbox, al_underscorehosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork, "/SSP/hosts/host/hostIdentity/name");
        rprtCnst.addKV("REPORT_underscoreN_underscoreHOSTS_underscoreIN_underscoreSCHEMA_underscoreNOT_underscoreIN_underscoreNETWORK", Integer.toString(al_underscorehosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork.size()));
        rprtCnst.set_underscoreh_underscorehosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork(h_underscorehosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork);
        MYSQLReportHostsInSchemaNotInNetworkDAO daorhisnin = new MYSQLReportHostsInSchemaNotInNetworkDAO();
        daorhisnin.create();
        daorhisnin.update(h_underscorehosts_underscorein_underscoreschema_underscorenot_underscorein_underscorenetwork);
        ArrayList<String> al_underscorehosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema = new ArrayList<String>(comp_underscorekeys.keys_underscorenot_underscorein_underscoreboth_underscorehashes(network_underscorehost_underscorehashbox, schema_underscorehost_underscorehashbox, "network_underscorehosts", "schema_underscorehosts"));
        Map<String, String> h_underscorehosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema = hr.get_underscorenames_underscorefrom_underscoreid_underscorelist(network_underscorehost_underscorehashbox, al_underscorehosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema, "host_underscorename");
        rprtCnst.addKV("REPORT_underscoreN_underscoreHOSTS_underscoreIN_underscoreNETWORK_underscoreNOT_underscoreIN_underscoreSCHEMA", Integer.toString(al_underscorehosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema.size()));
        rprtCnst.set_underscoreh_underscorehosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema(h_underscorehosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema);
        MYSQLReportHostsInNetworkNotInSchemaDAO rhinnis = new MYSQLReportHostsInNetworkNotInSchemaDAO();
        rhinnis.create();
        rhinnis.update(h_underscorehosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema);
        logger.info("========== Compare Hosts BEGIN ============================");
        Comparison comp_underscorehost = new Comparison(schema_underscorehost_underscorehashbox, "Schema_underscoreHost", network_underscorehost_underscorehashbox, "Network_underscoreHost", XpathUtility.getXpathToDbColumnMap(), XpathUtility.getXpathToCompOpMap());
        comp_underscorehost.init();
        MYSQLAuditResultsHostDAO daoarh = new MYSQLAuditResultsHostDAO();
        daoarh.create();
        String sql_underscorevals_underscorehost_underscoreschema = comp_underscorehost.iterate_underscorehbs_underscorehost(daoarh, "audit_underscoreresults_underscorehost", "host", h_underscorehosts_underscorein_underscorenetwork_underscorenot_underscorein_underscoreschema);
        CachedRowSet rs_underscoreRH = daoarh.query_underscoreq1_underscoreRH();
        String n_underscorehosts_underscorenot_underscoremeeting_underscorestorage = DBUtil.get_underscoresingle_underscorecount_underscorefrom_underscorers(rs_underscoreRH);
        rprtCnst.addKV("REPORT_underscoreN_underscoreHOSTS_underscoreNOT_underscoreMEETING_underscoreSTORAGE", n_underscorehosts_underscorenot_underscoremeeting_underscorestorage);
        logger.info("Calling result.get_underscoreresult_underscorehash( \"host\" ) from main()");
        Map host_underscorecomp_underscorehash = result.get_underscoreresult_underscorehash("host");
        Map au_underscorecomp_underscorehash2 = result.get_underscoreresult_underscorehash("au");
        logger.info("========== Compare Hosts END ==============================");
        Map<String, String> map_underscorehost_underscoreip_underscoreto_underscorehost_underscorename = hr.make_underscoreid_underscorehash(schema_underscorehost_underscorehashbox, "/SSP/hosts/host/hostIdentity/name");
        rprtCnst.addKV("REPORT_underscoreN_underscoreHOSTS_underscoreIN_underscoreSCHEMA", Integer.toString(map_underscorehost_underscoreip_underscoreto_underscorehost_underscorename.size()));
        String[] host_underscoreip_underscorelist = hr.hash_underscorekeys_underscoreto_underscorearray(schema_underscorehost_underscorehashbox);
        String[][] col2 = Utility.add_underscorecolumn_underscoreto_underscorearray1(map_underscorehost_underscoreip_underscoreto_underscorehost_underscorename.values().toArray(new String[0]), host_underscoreip_underscorelist, null);
        Map<String, String> map_underscoreau_underscorekey_underscorestring_underscoreto_underscoreau_underscorename = hr.make_underscoreid_underscorehash(schema_underscoreau_underscorehashbox, "/SSP/archivalUnits/au/auIdentity/name");
        logger.info("Length map_underscoreau_underscorekey_underscorestring_underscoreto_underscoreau_underscorename.values().toArray(new String[0]: " + map_underscoreau_underscorekey_underscorestring_underscoreto_underscoreau_underscorename.values().toArray(new String[0]).length);
        rprtCnst.addKV("REPORT_underscoreN_underscoreAUS_underscoreIN_underscoreSCHEMA", Integer.toString(map_underscoreau_underscorekey_underscorestring_underscoreto_underscoreau_underscorename.size()));
        MySQLLockssBoxArchivalUnitStatusDAO daolbaus = new MySQLLockssBoxArchivalUnitStatusDAO();
        int[] rc = daolbaus.getResultSetDimensions();
        int n_underscorers_underscorerows = rc[0];
        int n_underscorers_underscorecols = rc[1];
        logger.info("\n" + n_underscorers_underscorerows + " rows (Host-AU's). " + n_underscorers_underscorecols + " columns.");
        rprtCnst.addKV("REPORT_underscoreN_underscoreHOST_underscoreAUS_underscoreIN_underscoreNETWORK", Integer.toString(n_underscorers_underscorerows));
        logger.info("================== Query 'audit_underscoreresults_underscorehost' Table ==========");
        CachedRowSet NNonCompliantAUsCRS = daoara.getNNonCompliantAUs();
        String NNonCompliantAUs = DBUtil.get_underscoresingle_underscorecount_underscorefrom_underscorers(NNonCompliantAUsCRS);
        rprtCnst.addKV("REPORT_underscoreN_underscoreAUS_underscoreNONCOMPLIANT", NNonCompliantAUs);
        logger.info("================== Query 'audit_underscoreresults_underscorehost' Table END ======");
        logger.info("========== Output Report ==================================");
        MYSQLReportConstantsDAO daorc = new MYSQLReportConstantsDAO();
        daorc.create();
        daorc.update(rprtCnst.getBox());
        MYSQLReportHostSummaryDAO daorhs = new MYSQLReportHostSummaryDAO();
        daorhs.create();
        CachedRowSet crsarh = daoarh.queryAll();
        daorhs.update(crsarh);
        daorhs.update_underscorenew_underscorecolumn("space_underscoreoffered", schema_underscorehost_underscorehb_underscoresel_underscoresize);
        daorhs.update_underscorenew_underscorecolumn("space_underscoreused", network_underscorehost_underscorehb_underscoresel_underscoreused_underscorespace);
        Map<String, String> computation_underscorecols_underscorein_underscorenet_underscorehost_underscoresummary = new HashMap<String, String>();
        computation_underscorecols_underscorein_underscorenet_underscorehost_underscoresummary.put("space_underscoretotal", "1");
        computation_underscorecols_underscorein_underscorenet_underscorehost_underscoresummary.put("space_underscoreused", "2");
        daorhs.update_underscorecompute_underscorecolumn("space_underscorefree", computation_underscorecols_underscorein_underscorenet_underscorehost_underscoresummary);
        logger.info("========== Audit Report Writer ======================================");
        AuditReportXMLWriter arxw = new AuditReportXMLWriter(rprtCnst, configFile.getAuditReportFile());
        Set<String> tableSet = tracAuditChecklistDataFacade.findAllTables();
        String tracResultTable = "trac_underscoreaudit_underscorechecklist_underscoredata";
        List<TracAuditChecklistData> evidenceList = null;
        if (tableSet.contains(tracResultTable)) {
            evidenceList = tracAuditChecklistDataFacade.findAll();
            logger.info("TRAC evidence list is size:" + evidenceList.size());
        } else {
            logger.info("Database does not contain table 'trac_underscoreaudit_underscorechecklist_underscoredata'");
        }
        Map<String, String> tracDataMap = new LinkedHashMap<String, String>();
        for (TracAuditChecklistData tracdata : evidenceList) {
            tracDataMap.put(tracdata.getAspectId(), tracdata.getEvidence());
        }
        String writeTimestamp = arxw.write(daoarh, daoara, daorc, tracDataMap);
        File target = new File(configFile.getAuditReportFileDir() + File.separator + configFile.getAuditSchemaFileName() + "." + writeTimestamp);
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;
        try {
            sourceChannel = new FileInputStream(auditSchemaFile).getChannel();
            targetChannel = new FileOutputStream(target).getChannel();
            targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception occurred while copying file", e);
        } finally {
            try {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
                if (targetChannel != null) {
                    targetChannel.close();
                }
            } catch (IOException e) {
                logger.info("closing channels failed");
            }
        }
        logger.info("\n========== EXIT drive() ===========================================");
        return writeTimestamp;
    }

