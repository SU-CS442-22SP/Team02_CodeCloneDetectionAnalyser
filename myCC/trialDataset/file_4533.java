    public int save(String newInventory, String inventoryType, int compareResult, boolean renameCorruptedFile) {
        if (newInventory == null || newInventory.equals("")) return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        Category log = ThreadCategory.getInstance(getClass());
        try {
            log.debug(newInventory);
            init(newInventory);
        } catch (ValidationException ve) {
            log.error("Unable to parse new Inventory.");
            log.error(ve);
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        } catch (MarshalException me) {
            log.error("Unable to parse new Inventory.");
            log.error(me);
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        }
        InventoryConfiguration iConfig = InventoryConfigFactory.getInstance().getConfiguration();
        String directory_underscorerepository = iConfig.getFileRepository();
        String path = (String) parameters.get("path");
        if (path == null) {
            log.error("Parameter 'path' not found.");
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        }
        java.sql.Connection dbConn = null;
        try {
            dbConn = DatabaseConnectionFactory.getInstance().getConnection();
            dbConn.setAutoCommit(false);
        } catch (SQLException s) {
            log.error("Unable to connect to DB");
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        } catch (Exception s) {
            log.error("Unable to connect to DB");
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        }
        String nodeDirectory_underscorerepository = "";
        String file_underscorerepository = "";
        String oldPathToFile = "";
        String newPathToFile = "";
        boolean renameFile = false;
        ResultSet rs = null;
        try {
            PreparedStatement stmt = dbConn.prepareStatement(SELECT_underscoreNODEID_underscoreBY_underscoreINTERFACE);
            stmt.setString(1, ipAddress);
            rs = stmt.executeQuery();
            while (rs.next()) {
                nodeId = rs.getInt(1);
            }
            if (directory_underscorerepository.endsWith("/") == false && directory_underscorerepository.endsWith(File.separator) == false) {
                directory_underscorerepository += File.separator;
            }
            nodeDirectory_underscorerepository = directory_underscorerepository + nodeId;
        } catch (SQLException s) {
            try {
                dbConn.rollback();
            } catch (SQLException sqle) {
                log.error("Unable to rollback on db. " + sqle);
            }
            log.error("Unable to read from DB");
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        }
        rs = null;
        try {
            PreparedStatement stmt = dbConn.prepareStatement(SELECT_underscorePATHTOFILE);
            stmt.setInt(1, nodeId);
            stmt.setString(2, inventoryType);
            stmt.setString(3, "A");
            rs = stmt.executeQuery();
            while (rs.next()) {
                oldPathToFile = rs.getString(1);
            }
            String newDirRep = nodeDirectory_underscorerepository;
            if (path.startsWith("/") == false && path.startsWith(File.separator) == false) {
                newDirRep += File.separator;
            }
            long time = System.currentTimeMillis();
            Timestamp currTime = new Timestamp(time);
            java.util.Date currTimeDate = new java.util.Date(currTime.getTime());
            SimpleDateFormat ObjectformatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            newPathToFile = newDirRep + path + "[" + ObjectformatDate.format(currTimeDate) + "]";
        } catch (SQLException s) {
            log.error("Unable to read from DB");
            try {
                dbConn.rollback();
            } catch (SQLException sqle) {
                log.error("Unable to rollback on db. " + sqle);
            }
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        }
        int returnValue = InventoryMonitor.CONFIGURATION_underscoreSAVED;
        rs = null;
        int found = -1;
        try {
            PreparedStatement stmt = dbConn.prepareStatement(COUNT_underscoreNODEID_underscoreCONFIGURATION_underscoreNAME);
            stmt.setInt(1, nodeId);
            stmt.setString(2, inventoryType);
            rs = stmt.executeQuery();
            while (rs.next()) {
                found = rs.getInt(1);
            }
        } catch (SQLException s) {
            log.error("Unable to read from DB");
            log.error(s);
            try {
                dbConn.rollback();
            } catch (SQLException sqle) {
                log.error("Unable to rollback on db. " + sqle);
            }
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        }
        long time = System.currentTimeMillis();
        Timestamp currentTime = new Timestamp(time);
        if (found > 0) {
            if (compareResult == InventoryMonitor.CONFIGURATION_underscoreCHANGED || compareResult == InventoryMonitor.FIRST_underscoreACTIVE_underscoreCONFIGURATION_underscoreDOWNLOAD) {
                try {
                    log.debug("FOUND=" + found + " row/s in configuration, UPDATE it");
                    PreparedStatement stmt = dbConn.prepareStatement(UPDATE_underscoreCONFIGURATION_underscoreTO_underscoreSTATUS_underscoreN);
                    stmt.setInt(1, nodeId);
                    stmt.setString(2, inventoryType);
                    stmt.executeUpdate();
                } catch (SQLException s) {
                    log.error("Unable to update DB" + s);
                    try {
                        dbConn.rollback();
                    } catch (SQLException sqle) {
                        log.error("Unable to rollback on db. " + sqle);
                    }
                    saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
                    return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
                }
                try {
                    PreparedStatement stmt = dbConn.prepareStatement(INSERT_underscoreIN_underscoreCONFIGURATION);
                    stmt.setInt(1, nodeId);
                    stmt.setTimestamp(2, currentTime);
                    stmt.setTimestamp(3, currentTime);
                    stmt.setString(4, inventoryType);
                    stmt.setString(5, newPathToFile);
                    stmt.execute();
                } catch (SQLException s) {
                    log.error("Unable to insert in DB");
                    log.error(s);
                    try {
                        dbConn.rollback();
                    } catch (SQLException sqle) {
                        log.error("Unable to rollback on db. " + sqle);
                    }
                    saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
                    return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
                }
            } else {
                try {
                    log.debug("FOUND=" + found + " row/s in configuration, UPDATE it");
                    PreparedStatement stmt = dbConn.prepareStatement(UPDATE_underscoreLASTPOLLTIME_underscorePATHTOFILE);
                    stmt.setTimestamp(1, currentTime);
                    stmt.setString(2, newPathToFile);
                    stmt.setInt(3, nodeId);
                    stmt.setString(4, inventoryType);
                    stmt.executeUpdate();
                    renameFile = true;
                } catch (SQLException s) {
                    log.error("Unable to update DB");
                    log.error(s);
                    try {
                        dbConn.rollback();
                    } catch (SQLException sqle) {
                        log.error("Unable to rollback on db. " + sqle);
                    }
                    saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
                    return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
                }
            }
            try {
                dbConn.commit();
            } catch (SQLException s) {
                log.error("Unable to commit to DB " + s);
                try {
                    dbConn.rollback();
                } catch (SQLException sqle) {
                    log.error("Unable to rollback on db. " + sqle);
                }
                return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
            }
        } else {
            try {
                PreparedStatement stmt = dbConn.prepareStatement(INSERT_underscoreIN_underscoreCONFIGURATION);
                stmt.setInt(1, nodeId);
                stmt.setTimestamp(2, currentTime);
                stmt.setTimestamp(3, currentTime);
                stmt.setString(4, inventoryType);
                stmt.setString(5, newPathToFile);
                stmt.execute();
            } catch (SQLException s) {
                log.error("Unable to insert in DB");
                log.error(s);
                try {
                    dbConn.rollback();
                } catch (SQLException sqle) {
                    log.error("Unable to rollback on db. " + sqle);
                }
                saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
                return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
            }
        }
        rs = null;
        found = -1;
        try {
            PreparedStatement stmt = dbConn.prepareStatement(COUNT_underscoreNODEID_underscoreIN_underscoreASSET);
            stmt.setInt(1, nodeId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                found = rs.getInt(1);
            }
        } catch (SQLException s) {
            log.error("Unable to write into DB");
            log.error(s);
            try {
                dbConn.rollback();
            } catch (SQLException sqle) {
                log.error("Unable to rollback on db. " + sqle);
            }
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        }
        time = System.currentTimeMillis();
        currentTime = new Timestamp(time);
        if (found > 0) {
            try {
                log.debug("Found " + found + " rows in table assets: UPDATE it");
                Iterator dbColumnIter = asset.keySet().iterator();
                String queryParam = "";
                int counter = 0;
                while (dbColumnIter.hasNext()) {
                    counter++;
                    String currDbColumn = (String) dbColumnIter.next();
                    String dataItem = (String) asset.get(currDbColumn);
                    dataItem = dataItem.replaceAll("[ \t]+", " ");
                    queryParam += currDbColumn + "='" + dataItem + "',";
                }
                if (counter > 0) {
                    String updateAssets = "UPDATE assets SET  " + queryParam + " lastmodifieddate=? WHERE nodeID =?";
                    PreparedStatement stmt = dbConn.prepareStatement(updateAssets);
                    stmt.setTimestamp(1, currentTime);
                    stmt.setInt(2, nodeId);
                    log.debug("UPDATEQUERY " + updateAssets);
                    stmt.executeUpdate();
                }
            } catch (SQLException s) {
                log.error("Unable to update DB" + s);
                try {
                    dbConn.rollback();
                } catch (SQLException sqle) {
                    log.error("Unable to rollback on db. " + sqle);
                }
                saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
                return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
            }
        } else {
            try {
                log.debug("row not found. INSERT into assets");
                InsertIntoAssets(dbConn);
            } catch (SQLException s) {
                log.error("Unable to insert in DB");
                log.error(s);
                try {
                    dbConn.rollback();
                } catch (SQLException sqle) {
                    log.error("Unable to rollback on db. " + sqle);
                }
                saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
                return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
            }
        }
        try {
            dbConn.commit();
        } catch (SQLException sqle) {
            log.error("Unable to save into DB" + sqle);
            try {
                dbConn.rollback();
            } catch (SQLException sqlex) {
                log.error("Unable to rollback on db. " + sqlex);
            }
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        }
        try {
            boolean exists = (new File(directory_underscorerepository)).exists();
            if (!exists) {
                log.warn("file-repository '" + directory_underscorerepository + "' not found: trying to create it.");
                boolean success = (new File(directory_underscorerepository)).mkdir();
                if (!success) {
                    log.error("Directory creation failed");
                    try {
                        dbConn.rollback();
                    } catch (SQLException s) {
                        log.error("Unable to rollback DB");
                    }
                    return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
                }
                log.warn("file-repository '" + directory_underscorerepository + "' created.");
            }
            exists = (new File(nodeDirectory_underscorerepository)).exists();
            if (!exists) {
                boolean success = (new File(nodeDirectory_underscorerepository)).mkdir();
                if (!success) {
                    log.error("Node Directory '" + nodeDirectory_underscorerepository + "' creation failed.");
                    saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
                    try {
                        dbConn.rollback();
                    } catch (SQLException s) {
                        log.error("Unable to rollback DB");
                    }
                    return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
                }
            }
            exists = (new File(oldPathToFile)).exists();
            log.debug(oldPathToFile + " exists=" + exists);
            if (!exists) {
                if (renameCorruptedFile) {
                    log.warn("renameCorruptedFile=true and old configuration doesn't exist.");
                }
                returnValue = InventoryMonitor.FIRST_underscoreACTIVE_underscoreCONFIGURATION_underscoreDOWNLOAD;
            }
            if (renameCorruptedFile) {
                File f = new File(oldPathToFile);
                String oldConfigurationFileDestination = oldPathToFile + "_underscoreCorrupted";
                File dest = new File(oldConfigurationFileDestination);
                dest.createNewFile();
                f.renameTo(dest);
                FileWriter fileout = new FileWriter(newPathToFile);
                BufferedWriter filebufwri = new BufferedWriter(fileout);
                PrintWriter printout = new PrintWriter(filebufwri);
                printout.println(newInventory);
                printout.close();
            } else {
                if (renameFile) {
                    File f = new File(oldPathToFile);
                    File dest = new File(newPathToFile);
                    dest.createNewFile();
                    f.renameTo(dest);
                } else {
                    FileWriter fileout = new FileWriter(newPathToFile);
                    BufferedWriter filebufwri = new BufferedWriter(fileout);
                    PrintWriter printout = new PrintWriter(filebufwri);
                    printout.print(newInventory);
                    printout.close();
                }
            }
        } catch (IOException ioex) {
            try {
                dbConn.rollback();
            } catch (SQLException s) {
                log.error("Unable to rollback DB");
            }
            log.error("Failed writing to file '" + newPathToFile + "'.");
            saveMessage = "Unable to save " + inventoryType + " configuration.<br>";
            return InventoryMonitor.CONFIGURATION_underscoreNOT_underscoreSAVED;
        } finally {
            try {
                dbConn.close();
            } catch (SQLException s) {
                log.error("Unable to close connection to DB");
            }
        }
        saveMessage = "inventory " + inventoryType + " success.<br>";
        log.debug("" + returnValue);
        return returnValue;
    }

