    public void open(int mode) throws MessagingException {
        final int ALL_underscoreOPTIONS = READ_underscoreONLY | READ_underscoreWRITE | MODE_underscoreMBOX | MODE_underscoreBLOB;
        if (DebugFile.trace) {
            DebugFile.writeln("DBFolder.open(" + String.valueOf(mode) + ")");
            DebugFile.incIdent();
        }
        if ((0 == (mode & READ_underscoreONLY)) && (0 == (mode & READ_underscoreWRITE))) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new MessagingException("Folder must be opened in either READ_underscoreONLY or READ_underscoreWRITE mode");
        } else if (ALL_underscoreOPTIONS != (mode | ALL_underscoreOPTIONS)) {
            if (DebugFile.trace) DebugFile.decIdent();
            throw new MessagingException("Invalid DBFolder open() option mode");
        } else {
            if ((0 == (mode & MODE_underscoreMBOX)) && (0 == (mode & MODE_underscoreBLOB))) mode = mode | MODE_underscoreMBOX;
            iOpenMode = mode;
            oConn = ((DBStore) getStore()).getConnection();
            if ((iOpenMode & MODE_underscoreMBOX) != 0) {
                String sFolderUrl;
                try {
                    sFolderUrl = Gadgets.chomp(getStore().getURLName().getFile(), File.separator) + oCatg.getPath(oConn);
                    if (DebugFile.trace) DebugFile.writeln("mail folder directory is " + sFolderUrl);
                    if (sFolderUrl.startsWith("file://")) sFolderDir = sFolderUrl.substring(7); else sFolderDir = sFolderUrl;
                } catch (SQLException sqle) {
                    iOpenMode = 0;
                    oConn = null;
                    if (DebugFile.trace) DebugFile.decIdent();
                    throw new MessagingException(sqle.getMessage(), sqle);
                }
                try {
                    File oDir = new File(sFolderDir);
                    if (!oDir.exists()) {
                        FileSystem oFS = new FileSystem();
                        oFS.mkdirs(sFolderUrl);
                    }
                } catch (IOException ioe) {
                    iOpenMode = 0;
                    oConn = null;
                    if (DebugFile.trace) DebugFile.decIdent();
                    throw new MessagingException(ioe.getMessage(), ioe);
                } catch (SecurityException se) {
                    iOpenMode = 0;
                    oConn = null;
                    if (DebugFile.trace) DebugFile.decIdent();
                    throw new MessagingException(se.getMessage(), se);
                } catch (Exception je) {
                    iOpenMode = 0;
                    oConn = null;
                    if (DebugFile.trace) DebugFile.decIdent();
                    throw new MessagingException(je.getMessage(), je);
                }
                JDCConnection oConn = getConnection();
                PreparedStatement oStmt = null;
                ResultSet oRSet = null;
                boolean bHasFilePointer;
                try {
                    oStmt = oConn.prepareStatement("SELECT NULL FROM " + DB.k_underscorex_underscorecat_underscoreobjs + " WHERE " + DB.gu_underscorecategory + "=? AND " + DB.id_underscoreclass + "=15", ResultSet.TYPE_underscoreFORWARD_underscoreONLY, ResultSet.CONCUR_underscoreREAD_underscoreONLY);
                    oStmt.setString(1, getCategory().getString(DB.gu_underscorecategory));
                    oRSet = oStmt.executeQuery();
                    bHasFilePointer = oRSet.next();
                    oRSet.close();
                    oRSet = null;
                    oStmt.close();
                    oStmt = null;
                    if (!bHasFilePointer) {
                        oConn.setAutoCommit(false);
                        Product oProd = new Product();
                        oProd.put(DB.gu_underscoreowner, oCatg.getString(DB.gu_underscoreowner));
                        oProd.put(DB.nm_underscoreproduct, oCatg.getString(DB.nm_underscorecategory));
                        oProd.store(oConn);
                        ProductLocation oLoca = new ProductLocation();
                        oLoca.put(DB.gu_underscoreproduct, oProd.getString(DB.gu_underscoreproduct));
                        oLoca.put(DB.gu_underscoreowner, oCatg.getString(DB.gu_underscoreowner));
                        oLoca.put(DB.pg_underscoreprod_underscorelocat, 1);
                        oLoca.put(DB.id_underscorecont_underscoretype, 1);
                        oLoca.put(DB.id_underscoreprod_underscoretype, "MBOX");
                        oLoca.put(DB.len_underscorefile, 0);
                        oLoca.put(DB.xprotocol, "file://");
                        oLoca.put(DB.xhost, "localhost");
                        oLoca.put(DB.xpath, Gadgets.chomp(sFolderDir, File.separator));
                        oLoca.put(DB.xfile, oCatg.getString(DB.nm_underscorecategory) + ".mbox");
                        oLoca.put(DB.xoriginalfile, oCatg.getString(DB.nm_underscorecategory) + ".mbox");
                        oLoca.store(oConn);
                        oStmt = oConn.prepareStatement("INSERT INTO " + DB.k_underscorex_underscorecat_underscoreobjs + " (" + DB.gu_underscorecategory + "," + DB.gu_underscoreobject + "," + DB.id_underscoreclass + ") VALUES (?,?,15)");
                        oStmt.setString(1, oCatg.getString(DB.gu_underscorecategory));
                        oStmt.setString(2, oProd.getString(DB.gu_underscoreproduct));
                        oStmt.executeUpdate();
                        oStmt.close();
                        oStmt = null;
                        oConn.commit();
                    }
                } catch (SQLException sqle) {
                    if (DebugFile.trace) {
                        DebugFile.writeln("SQLException " + sqle.getMessage());
                        DebugFile.decIdent();
                    }
                    if (oStmt != null) {
                        try {
                            oStmt.close();
                        } catch (SQLException ignore) {
                        }
                    }
                    if (oConn != null) {
                        try {
                            oConn.rollback();
                        } catch (SQLException ignore) {
                        }
                    }
                    throw new MessagingException(sqle.getMessage(), sqle);
                }
            } else {
                sFolderDir = null;
            }
            if (DebugFile.trace) {
                DebugFile.decIdent();
                String sMode = "";
                if ((iOpenMode & READ_underscoreWRITE) != 0) sMode += " READ_underscoreWRITE ";
                if ((iOpenMode & READ_underscoreONLY) != 0) sMode += " READ_underscoreONLY ";
                if ((iOpenMode & MODE_underscoreBLOB) != 0) sMode += " MODE_underscoreBLOB ";
                if ((iOpenMode & MODE_underscoreMBOX) != 0) sMode += " MODE_underscoreMBOX ";
                DebugFile.writeln("End DBFolder.open() :");
            }
        }
    }

