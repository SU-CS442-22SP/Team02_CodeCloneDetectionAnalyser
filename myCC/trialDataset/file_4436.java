    protected String doIt() throws java.lang.Exception {
        StringBuffer sql = null;
        int no = 0;
        String clientCheck = " AND AD_underscoreClient_underscoreID=" + m_underscoreAD_underscoreClient_underscoreID;
        if (m_underscoredeleteOldImported) {
            sql = new StringBuffer("DELETE I_underscoreBPartner " + "WHERE I_underscoreIsImported='Y'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            log.fine("Delete Old Impored =" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET AD_underscoreClient_underscoreID = COALESCE(AD_underscoreClient_underscoreID, ").append(m_underscoreAD_underscoreClient_underscoreID).append(")," + " AD_underscoreOrg_underscoreID = COALESCE(AD_underscoreOrg_underscoreID, 0)," + " IsActive = COALESCE(IsActive, 'Y')," + " Created = COALESCE(Created, current_underscoretimestamp)," + " CreatedBy = COALESCE(CreatedBy, 0)," + " Updated = COALESCE(Updated, current_underscoretimestamp)," + " UpdatedBy = COALESCE(UpdatedBy, 0)," + " I_underscoreErrorMsg = ''," + " I_underscoreIsImported = 'N' " + "WHERE I_underscoreIsImported<>'Y' OR I_underscoreIsImported IS NULL");
        no = DB.executeUpdate(sql.toString());
        log.fine("Reset=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET GroupValue=(SELECT Value FROM C_underscoreBP_underscoreGroup g WHERE g.IsDefault='Y'" + " AND g.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID AND ROWNUM=1) " + "WHERE GroupValue IS NULL AND C_underscoreBP_underscoreGroup_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Group Default=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreBP_underscoreGroup_underscoreID=(SELECT C_underscoreBP_underscoreGroup_underscoreID FROM C_underscoreBP_underscoreGroup g" + " WHERE i.GroupValue=g.Value AND g.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID ORDER BY g.IsDefault DESC LIMIT 1) " + "WHERE C_underscoreBP_underscoreGroup_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Group=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'" + getMsg("ImportBPInvalidGroup") + ". ' " + "WHERE C_underscoreBP_underscoreGroup_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.config("Invalid Group=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET CountryCode=(SELECT CountryCode FROM C_underscoreCountry c WHERE c.isactive='Y'" + " AND c.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID) AND ROWNUM=1) " + "WHERE CountryCode IS NULL AND C_underscoreCountry_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Country Default=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreCountry_underscoreID=(SELECT C_underscoreCountry_underscoreID FROM C_underscoreCountry c" + " WHERE i.CountryCode=c.CountryCode AND c.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID)) " + "WHERE C_underscoreCountry_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Country=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'" + getMsg("ImportBPInvalidCountry") + ". ' " + "WHERE C_underscoreCountry_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.config("Invalid Country=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "Set RegionName=(SELECT Name FROM C_underscoreRegion r" + " WHERE r.IsDefault='Y' AND r.C_underscoreCountry_underscoreID=i.C_underscoreCountry_underscoreID" + " AND r.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID) AND ROWNUM=1) " + "WHERE RegionName IS NULL AND C_underscoreRegion_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Region Default=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "Set C_underscoreRegion_underscoreID=(SELECT C_underscoreRegion_underscoreID FROM C_underscoreRegion r" + " WHERE r.Name=i.RegionName AND r.C_underscoreCountry_underscoreID=i.C_underscoreCountry_underscoreID" + " AND r.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID)) " + "WHERE C_underscoreRegion_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Region=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'" + getMsg("ImportBPInvalidRegion") + ". ' " + " WHERE C_underscoreRegion_underscoreID IS NULL " + " AND EXISTS (SELECT * FROM C_underscoreCountry c" + " WHERE c.C_underscoreCountry_underscoreID=i.C_underscoreCountry_underscoreID AND c.HasRegion='Y')" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.config("Invalid Region=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET BPContactGreeting=NULL WHERE C_underscoreGreeting_underscoreID IS NULL AND char_underscorelength(trim(BPContactGreeting)) = 0 AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Reset Greeting=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreGreeting_underscoreID=(SELECT C_underscoreGreeting_underscoreID FROM C_underscoreGreeting g" + " WHERE i.BPContactGreeting=g.Name AND g.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID)) " + "WHERE C_underscoreGreeting_underscoreID IS NULL AND BPContactGreeting IS NOT NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Greeting=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'" + getMsg("ImportBPInvalidGreeting") + ". ' " + "WHERE C_underscoreGreeting_underscoreID IS NULL AND BPContactGreeting IS NOT NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.config("Invalid Greeting=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreBPartner_underscoreID=(SELECT C_underscoreBPartner_underscoreID FROM C_underscoreBPartner p" + " WHERE i.Value=p.Value AND p.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) " + "WHERE C_underscoreBPartner_underscoreID IS NULL AND Value IS NOT NULL" + " AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Found BPartner=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET AD_underscoreUser_underscoreID=(SELECT AD_underscoreUser_underscoreID FROM AD_underscoreUser c" + " WHERE i.ContactName=c.Name AND i.C_underscoreBPartner_underscoreID=c.C_underscoreBPartner_underscoreID AND c.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) " + "WHERE C_underscoreBPartner_underscoreID IS NOT NULL AND AD_underscoreUser_underscoreID IS NULL AND ContactName IS NOT NULL" + " AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Found Contact=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreBPartner_underscoreLocation_underscoreID=(SELECT C_underscoreBPartner_underscoreLocation_underscoreID" + " FROM C_underscoreBPartner_underscoreLocation bpl INNER JOIN C_underscoreLocation l ON (bpl.C_underscoreLocation_underscoreID=l.C_underscoreLocation_underscoreID)" + " WHERE i.C_underscoreBPartner_underscoreID=bpl.C_underscoreBPartner_underscoreID AND bpl.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID" + " AND DUMP(i.Address1)=DUMP(l.Address1) AND DUMP(i.Address2)=DUMP(l.Address2)" + " AND DUMP(i.City)=DUMP(l.City) AND DUMP(i.Postal)=DUMP(l.Postal) AND DUMP(i.Postal_underscoreAdd)=DUMP(l.Postal_underscoreAdd)" + " AND DUMP(i.C_underscoreRegion_underscoreID)=DUMP(l.C_underscoreRegion_underscoreID) AND DUMP(i.C_underscoreCountry_underscoreID)=DUMP(l.C_underscoreCountry_underscoreID)) " + "WHERE C_underscoreBPartner_underscoreID IS NOT NULL AND C_underscoreBPartner_underscoreLocation_underscoreID IS NULL" + " AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Found Location=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreCategoria_underscoreIVA_underscoreCodigo=" + MCategoriaIva.CONSUMIDOR_underscoreFINAL + " WHERE (C_underscoreCategoria_underscoreIVA_underscoreCodigo IS NULL OR C_underscoreCategoria_underscoreIVA_underscoreCodigo = 0) " + "  AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreCategoria_underscoreIVA_underscoreID= " + " (SELECT C_underscoreCategoria_underscoreIVA_underscoreID " + "  FROM C_underscoreCategoria_underscoreIVA c " + "  WHERE i.C_underscoreCategoria_underscoreIVA_underscoreCodigo=c.Codigo AND c.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID " + " ) " + "WHERE C_underscoreCategoria_underscoreIVA_underscoreID IS NULL " + "  AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET SalesRep_underscoreID=(SELECT AD_underscoreUser_underscoreID " + "FROM AD_underscoreUser u " + "WHERE u.Name = i.SalesRep_underscoreName AND u.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID)) " + "WHERE SalesRep_underscoreID IS NULL " + "AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("Set Country=" + no);
        int noInsert = 0;
        int noUpdate = 0;
        sql = new StringBuffer("SELECT I_underscoreBPartner_underscoreID, C_underscoreBPartner_underscoreID," + "C_underscoreBPartner_underscoreLocation_underscoreID,COALESCE(Address1,Address2,City,RegionName,CountryCode)," + "AD_underscoreUser_underscoreID,ContactName " + "FROM I_underscoreBPartner " + "WHERE I_underscoreIsImported='N'").append(clientCheck);
        Connection conn = DB.createConnection(false, Connection.TRANSACTION_underscoreREAD_underscoreCOMMITTED);
        try {
            log.info("En importBPartbner antes de hacer el update en c_underscoreBPartner");
            PreparedStatement pstmt_underscoreupdateBPartner = conn.prepareStatement("UPDATE C_underscoreBPartner " + "SET Value=aux.Value" + ",Name=aux.Name" + ",Name2=aux.Name2" + ",Description=aux.Description" + ",DUNS=aux.DUNS" + ",TaxID=aux.TaxID" + ",NAICS=aux.NAICS" + ",C_underscoreBP_underscoreGroup_underscoreID=aux.C_underscoreBP_underscoreGroup_underscoreID" + ",Updated=current_underscoretimestamp" + ",UpdatedBy=aux.UpdatedBy" + ",IIBB=aux.IIBB" + " from (SELECT Value,Name,Name2,Description,DUNS,TaxID,NAICS,C_underscoreBP_underscoreGroup_underscoreID,UpdatedBy,IIBB FROM I_underscoreBPartner WHERE I_underscoreBPartner_underscoreID=?) as aux" + " WHERE C_underscoreBPartner_underscoreID=?");
            log.info("En importBPartbner despues de hacer el update en c_underscoreBPartner");
            PreparedStatement pstmt_underscoreinsertLocation = conn.prepareStatement("INSERT INTO C_underscoreLocation (C_underscoreLocation_underscoreID," + "AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,IsActive,Created,CreatedBy,Updated,UpdatedBy," + "Address1,Address2,City,Postal,Postal_underscoreAdd,C_underscoreCountry_underscoreID,C_underscoreRegion_underscoreID) " + "SELECT ?," + "AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,'Y',current_underscoretimestamp,CreatedBy,current_underscoretimestamp,UpdatedBy," + "Address1,Address2,City,Postal,Postal_underscoreAdd,C_underscoreCountry_underscoreID,C_underscoreRegion_underscoreID " + "FROM I_underscoreBPartner " + "WHERE I_underscoreBPartner_underscoreID=?");
            PreparedStatement pstmt_underscoreinsertBPLocation = conn.prepareStatement("INSERT INTO C_underscoreBPartner_underscoreLocation ( " + "	C_underscoreBPartner_underscoreLocation_underscoreID," + "	AD_underscoreClient_underscoreID," + "	AD_underscoreOrg_underscoreID," + "	IsActive," + "	Created," + "	CreatedBy," + "	Updated," + "	UpdatedBy," + "	Name," + "	IsBillTo," + "	IsShipTo," + "	IsPayFrom," + "	IsRemitTo," + "	Phone," + "	Phone2," + "	Fax," + "	C_underscoreBPartner_underscoreID," + "	C_underscoreLocation_underscoreID) " + "SELECT ?,AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,'Y',current_underscoretimestamp,CreatedBy,current_underscoretimestamp,UpdatedBy," + "CASE WHEN char_underscorelength(trim(coalesce(address1,''))) > 0 THEN address1 " + "     WHEN char_underscorelength(trim(coalesce(city,''))) > 0 THEN city " + "     WHEN char_underscorelength(trim(coalesce(regionname,''))) > 0 THEN regionname " + "     ELSE name " + "END," + "'Y','Y','Y','Y'," + "Phone,Phone2,Fax, ?,? " + "FROM I_underscoreBPartner " + "WHERE I_underscoreBPartner_underscoreID=?");
            PreparedStatement pstmt_underscoreinsertBPContact = conn.prepareStatement("INSERT INTO AD_underscoreUser (AD_underscoreUser_underscoreID," + "AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,IsActive,Created,CreatedBy,Updated,UpdatedBy," + "C_underscoreBPartner_underscoreID,C_underscoreBPartner_underscoreLocation_underscoreID,C_underscoreGreeting_underscoreID," + "Name,Title,Description,Comments,Phone,Phone2,Fax,EMail,Birthday) " + "SELECT ?," + "AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,'Y',current_underscoretimestamp,CreatedBy,current_underscoretimestamp,UpdatedBy," + "?,?,C_underscoreGreeting_underscoreID," + "ContactName,Title,ContactDescription,Comments,Phone,Phone2,Fax,EMail,Birthday " + "FROM I_underscoreBPartner " + " WHERE I_underscoreBPartner_underscoreID=?");
            PreparedStatement pstmt_underscoreupdateBPContact = conn.prepareStatement("UPDATE AD_underscoreUser " + "SET C_underscoreGreeting_underscoreID=aux1.C_underscoreGreeting_underscoreID" + ",Name=aux1.Name" + ",Title=aux1.Title" + ",Description=aux1.Description" + ",Comments=aux1.Commets" + ",Phone=aux1.Phone" + ",Phone2=aux1.Phone2" + ",Fax=aux1.Fax" + ",EMail=aux1.EMail" + ",Birthday=aux1.Birthaday" + ",Updated=current_underscoretimestamp" + ",UpdatedBy=aux1.UpdatedBy" + " from (SELECT C_underscoreGreeting_underscoreID,ContactName,Title,ContactDescription,Comments,Phone,Phone2,Fax,EMail,Birthday,UpdatedBy FROM I_underscoreBPartner WHERE I_underscoreBPartner_underscoreID=?) as aux1" + " WHERE AD_underscoreUser_underscoreID=?");
            PreparedStatement pstmt_underscoresetImported = conn.prepareStatement("UPDATE I_underscoreBPartner SET I_underscoreIsImported='Y'," + " C_underscoreBPartner_underscoreID=?, C_underscoreBPartner_underscoreLocation_underscoreID=?, AD_underscoreUser_underscoreID=?, " + " Updated=current_underscoretimestamp, Processed='Y' WHERE I_underscoreBPartner_underscoreID=?");
            PreparedStatement pstmt = DB.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int I_underscoreBPartner_underscoreID = rs.getInt(1);
                int C_underscoreBPartner_underscoreID = rs.getInt(2);
                boolean newBPartner = C_underscoreBPartner_underscoreID == 0;
                int C_underscoreBPartner_underscoreLocation_underscoreID = rs.getInt(3);
                String newLocali = rs.getString(4);
                boolean newLocation = rs.getString(4) != null;
                int AD_underscoreUser_underscoreID = rs.getInt(5);
                boolean newContact = rs.getString(6) != null;
                log.fine("I_underscoreBPartner_underscoreID=" + I_underscoreBPartner_underscoreID + ", C_underscoreBPartner_underscoreID=" + C_underscoreBPartner_underscoreID + ", C_underscoreBPartner_underscoreLocation_underscoreID=" + C_underscoreBPartner_underscoreLocation_underscoreID + " create=" + newLocation + ", AD_underscoreUser_underscoreID=" + AD_underscoreUser_underscoreID + " create=" + newContact);
                if (newBPartner) {
                    X_underscoreI_underscoreBPartner iBP = new X_underscoreI_underscoreBPartner(getCtx(), I_underscoreBPartner_underscoreID, null);
                    MBPartner bp = new MBPartner(iBP);
                    if (bp.save()) {
                        C_underscoreBPartner_underscoreID = bp.getC_underscoreBPartner_underscoreID();
                        log.finest("Insert BPartner");
                        noInsert++;
                    } else {
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Insert BPartner failed: " + CLogger.retrieveErrorAsString())).append(" WHERE I_underscoreBPartner_underscoreID=").append(I_underscoreBPartner_underscoreID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                } else {
                    pstmt_underscoreupdateBPartner.setInt(1, I_underscoreBPartner_underscoreID);
                    pstmt_underscoreupdateBPartner.setInt(2, C_underscoreBPartner_underscoreID);
                    try {
                        no = pstmt_underscoreupdateBPartner.executeUpdate();
                        log.finest("Update BPartner = " + no);
                        noUpdate++;
                    } catch (SQLException ex) {
                        log.finest("Update BPartner -- " + ex.toString());
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Update BPartner: " + ex.toString())).append(" WHERE I_underscoreBPartner_underscoreID=").append(I_underscoreBPartner_underscoreID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                }
                if (C_underscoreBPartner_underscoreLocation_underscoreID != 0) {
                } else if (newLocation) {
                    int C_underscoreLocation_underscoreID = 0;
                    try {
                        C_underscoreLocation_underscoreID = DB.getNextID(m_underscoreAD_underscoreClient_underscoreID, "C_underscoreLocation", null);
                        if (C_underscoreLocation_underscoreID <= 0) {
                            throw new DBException("No NextID (" + C_underscoreLocation_underscoreID + ")");
                        }
                        pstmt_underscoreinsertLocation.setInt(1, C_underscoreLocation_underscoreID);
                        pstmt_underscoreinsertLocation.setInt(2, I_underscoreBPartner_underscoreID);
                        no = pstmt_underscoreinsertLocation.executeUpdate();
                        log.finest("Insert Location = " + no);
                    } catch (SQLException ex) {
                        log.finest("Insert Location - " + ex.toString());
                        conn.rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Insert Location: " + ex.toString())).append(" WHERE I_underscoreBPartner_underscoreID=").append(I_underscoreBPartner_underscoreID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                    try {
                        C_underscoreBPartner_underscoreLocation_underscoreID = (DB.getNextID(m_underscoreAD_underscoreClient_underscoreID, "C_underscoreBPartner_underscoreLocation", null));
                        log.finest("C_underscoreBPartner_underscoreLocation_underscoreID es : " + C_underscoreBPartner_underscoreLocation_underscoreID);
                        if (C_underscoreBPartner_underscoreLocation_underscoreID <= 0) {
                            throw new DBException("No NextID (" + C_underscoreBPartner_underscoreLocation_underscoreID + ")");
                        }
                        pstmt_underscoreinsertBPLocation.setInt(1, C_underscoreBPartner_underscoreLocation_underscoreID);
                        pstmt_underscoreinsertBPLocation.setInt(2, C_underscoreBPartner_underscoreID);
                        pstmt_underscoreinsertBPLocation.setInt(3, C_underscoreLocation_underscoreID);
                        pstmt_underscoreinsertBPLocation.setInt(4, I_underscoreBPartner_underscoreID);
                        no = pstmt_underscoreinsertBPLocation.executeUpdate();
                        log.finest("Insert BP Location = " + no);
                    } catch (Exception ex) {
                        log.finest("Insert BPLocation - " + ex.toString());
                        conn.rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Insert BPLocation: " + ex.toString())).append(" WHERE I_underscoreBPartner_underscoreID=").append(I_underscoreBPartner_underscoreID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                }
                if (AD_underscoreUser_underscoreID != 0) {
                    pstmt_underscoreupdateBPContact.setInt(1, I_underscoreBPartner_underscoreID);
                    pstmt_underscoreupdateBPContact.setInt(2, AD_underscoreUser_underscoreID);
                    try {
                        no = pstmt_underscoreupdateBPContact.executeUpdate();
                        log.finest("Update BP Contact = " + no);
                    } catch (SQLException ex) {
                        log.finest("Update BP Contact - " + ex.toString());
                        conn.rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Update BP Contact: " + ex.toString())).append(" WHERE I_underscoreBPartner_underscoreID=").append(I_underscoreBPartner_underscoreID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                } else if (newContact) {
                    try {
                        AD_underscoreUser_underscoreID = DB.getNextID(m_underscoreAD_underscoreClient_underscoreID, "AD_underscoreUser", null);
                        if (AD_underscoreUser_underscoreID <= 0) {
                            throw new DBException("No NextID (" + AD_underscoreUser_underscoreID + ")");
                        }
                        pstmt_underscoreinsertBPContact.setInt(1, AD_underscoreUser_underscoreID);
                        pstmt_underscoreinsertBPContact.setInt(2, C_underscoreBPartner_underscoreID);
                        if (C_underscoreBPartner_underscoreLocation_underscoreID == 0) {
                            pstmt_underscoreinsertBPContact.setNull(3, Types.NUMERIC);
                        } else {
                            pstmt_underscoreinsertBPContact.setInt(3, C_underscoreBPartner_underscoreLocation_underscoreID);
                        }
                        pstmt_underscoreinsertBPContact.setInt(4, I_underscoreBPartner_underscoreID);
                        no = pstmt_underscoreinsertBPContact.executeUpdate();
                        log.finest("Insert BP Contact = " + no);
                    } catch (Exception ex) {
                        log.finest("Insert BPContact - " + ex.toString());
                        conn.rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Insert BPContact: " + ex.toString())).append(" WHERE I_underscoreBPartner_underscoreID=").append(I_underscoreBPartner_underscoreID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                }
                pstmt_underscoresetImported.setInt(1, C_underscoreBPartner_underscoreID);
                if (C_underscoreBPartner_underscoreLocation_underscoreID == 0) {
                    pstmt_underscoresetImported.setNull(2, Types.NUMERIC);
                } else {
                    pstmt_underscoresetImported.setInt(2, C_underscoreBPartner_underscoreLocation_underscoreID);
                }
                if (AD_underscoreUser_underscoreID == 0) {
                    pstmt_underscoresetImported.setNull(3, Types.NUMERIC);
                } else {
                    pstmt_underscoresetImported.setInt(3, AD_underscoreUser_underscoreID);
                }
                pstmt_underscoresetImported.setInt(4, I_underscoreBPartner_underscoreID);
                no = pstmt_underscoresetImported.executeUpdate();
                conn.commit();
            }
            rs.close();
            pstmt.close();
            pstmt_underscoreupdateBPartner.close();
            pstmt_underscoreinsertLocation.close();
            pstmt_underscoreinsertBPLocation.close();
            pstmt_underscoreinsertBPContact.close();
            pstmt_underscoreupdateBPContact.close();
            pstmt_underscoresetImported.close();
            conn.close();
            conn = null;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.close();
                }
                conn = null;
            } catch (SQLException ex) {
            }
            throw new Exception("ImportBPartner.doIt", e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            conn = null;
        }
        sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET I_underscoreIsImported='N', Updated=current_underscoretimestamp " + "WHERE I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        addLog(0, null, new BigDecimal(no), "@Errors@");
        addLog(0, null, new BigDecimal(noInsert), "@C_underscoreBPartner_underscoreID@: @Inserted@");
        addLog(0, null, new BigDecimal(noUpdate), "@C_underscoreBPartner_underscoreID@: @Updated@");
        return "";
    }

