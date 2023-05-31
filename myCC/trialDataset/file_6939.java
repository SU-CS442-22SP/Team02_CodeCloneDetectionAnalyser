    protected String doIt() throws java.lang.Exception {
        StringBuffer sql = null;
        int no = 0;
        String clientCheck = getWhereClause();
        if (m_underscoredeleteOldImported) {
            sql = new StringBuffer("DELETE I_underscoreBPartner " + "WHERE I_underscoreIsImported='Y'").append(clientCheck);
            no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
            log.fine("Delete Old Impored =" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET AD_underscoreClient_underscoreID = COALESCE (AD_underscoreClient_underscoreID, ").append(m_underscoreAD_underscoreClient_underscoreID).append(")," + " AD_underscoreOrg_underscoreID = COALESCE (AD_underscoreOrg_underscoreID, 0)," + " IsActive = COALESCE (IsActive, 'Y')," + " Created = COALESCE (Created, SysDate)," + " CreatedBy = COALESCE (CreatedBy, 0)," + " Updated = COALESCE (Updated, SysDate)," + " UpdatedBy = COALESCE (UpdatedBy, 0)," + " I_underscoreErrorMsg = ' '," + " I_underscoreIsImported = 'N' " + "WHERE I_underscoreIsImported<>'Y' OR I_underscoreIsImported IS NULL");
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Reset=" + no);
        ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_underscoreBEFORE_underscoreVALIDATE);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET GroupValue=(SELECT MAX(Value) FROM C_underscoreBP_underscoreGroup g WHERE g.IsDefault='Y'" + " AND g.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) ");
        sql.append("WHERE GroupValue IS NULL AND C_underscoreBP_underscoreGroup_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Set Group Default=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreBP_underscoreGroup_underscoreID=(SELECT C_underscoreBP_underscoreGroup_underscoreID FROM C_underscoreBP_underscoreGroup g" + " WHERE i.GroupValue=g.Value AND g.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) " + "WHERE C_underscoreBP_underscoreGroup_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Set Group=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Invalid Group, ' " + "WHERE C_underscoreBP_underscoreGroup_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.config("Invalid Group=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreCountry_underscoreID=(SELECT C_underscoreCountry_underscoreID FROM C_underscoreCountry c" + " WHERE i.CountryCode=c.CountryCode AND c.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID)) " + "WHERE C_underscoreCountry_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Set Country=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Invalid Country, ' " + "WHERE C_underscoreCountry_underscoreID IS NULL AND (City IS NOT NULL OR Address1 IS NOT NULL)" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.config("Invalid Country=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "Set RegionName=(SELECT MAX(Name) FROM C_underscoreRegion r" + " WHERE r.IsDefault='Y' AND r.C_underscoreCountry_underscoreID=i.C_underscoreCountry_underscoreID" + " AND r.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID)) ");
        sql.append("WHERE RegionName IS NULL AND C_underscoreRegion_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Set Region Default=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "Set C_underscoreRegion_underscoreID=(SELECT C_underscoreRegion_underscoreID FROM C_underscoreRegion r" + " WHERE r.Name=i.RegionName AND r.C_underscoreCountry_underscoreID=i.C_underscoreCountry_underscoreID" + " AND r.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID)) " + "WHERE C_underscoreRegion_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Set Region=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Invalid Region, ' " + "WHERE C_underscoreRegion_underscoreID IS NULL " + " AND EXISTS (SELECT * FROM C_underscoreCountry c" + " WHERE c.C_underscoreCountry_underscoreID=i.C_underscoreCountry_underscoreID AND c.HasRegion='Y')" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.config("Invalid Region=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreGreeting_underscoreID=(SELECT C_underscoreGreeting_underscoreID FROM C_underscoreGreeting g" + " WHERE i.BPContactGreeting=g.Name AND g.AD_underscoreClient_underscoreID IN (0, i.AD_underscoreClient_underscoreID)) " + "WHERE C_underscoreGreeting_underscoreID IS NULL AND BPContactGreeting IS NOT NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Set Greeting=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Invalid Greeting, ' " + "WHERE C_underscoreGreeting_underscoreID IS NULL AND BPContactGreeting IS NOT NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.config("Invalid Greeting=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET (C_underscoreBPartner_underscoreID,AD_underscoreUser_underscoreID)=" + "(SELECT C_underscoreBPartner_underscoreID,AD_underscoreUser_underscoreID FROM AD_underscoreUser u " + "WHERE i.EMail=u.EMail AND u.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) " + "WHERE i.EMail IS NOT NULL AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Found EMail User=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreBPartner_underscoreID=(SELECT C_underscoreBPartner_underscoreID FROM C_underscoreBPartner p" + " WHERE i.Value=p.Value AND p.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) " + "WHERE C_underscoreBPartner_underscoreID IS NULL AND Value IS NOT NULL" + " AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Found BPartner=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET AD_underscoreUser_underscoreID=(SELECT AD_underscoreUser_underscoreID FROM AD_underscoreUser c" + " WHERE i.ContactName=c.Name AND i.C_underscoreBPartner_underscoreID=c.C_underscoreBPartner_underscoreID AND c.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) " + "WHERE C_underscoreBPartner_underscoreID IS NOT NULL AND AD_underscoreUser_underscoreID IS NULL AND ContactName IS NOT NULL" + " AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Found Contact=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET C_underscoreBPartner_underscoreLocation_underscoreID=(SELECT C_underscoreBPartner_underscoreLocation_underscoreID" + " FROM C_underscoreBPartner_underscoreLocation bpl INNER JOIN C_underscoreLocation l ON (bpl.C_underscoreLocation_underscoreID=l.C_underscoreLocation_underscoreID)" + " WHERE i.C_underscoreBPartner_underscoreID=bpl.C_underscoreBPartner_underscoreID AND bpl.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID" + " AND (i.Address1=l.Address1 OR (i.Address1 IS NULL AND l.Address1 IS NULL))" + " AND (i.Address2=l.Address2 OR (i.Address2 IS NULL AND l.Address2 IS NULL))" + " AND (i.City=l.City OR (i.City IS NULL AND l.City IS NULL))" + " AND (i.Postal=l.Postal OR (i.Postal IS NULL AND l.Postal IS NULL))" + " AND (i.Postal_underscoreAdd=l.Postal_underscoreAdd OR (l.Postal_underscoreAdd IS NULL AND l.Postal_underscoreAdd IS NULL))" + " AND i.C_underscoreRegion_underscoreID=l.C_underscoreRegion_underscoreID AND i.C_underscoreCountry_underscoreID=l.C_underscoreCountry_underscoreID) " + "WHERE C_underscoreBPartner_underscoreID IS NOT NULL AND C_underscoreBPartner_underscoreLocation_underscoreID IS NULL" + " AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Found Location=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET R_underscoreInterestArea_underscoreID=(SELECT R_underscoreInterestArea_underscoreID FROM R_underscoreInterestArea ia " + "WHERE i.InterestAreaName=ia.Name AND ia.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) " + "WHERE R_underscoreInterestArea_underscoreID IS NULL AND InterestAreaName IS NOT NULL" + " AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.fine("Set Interest Area=" + no);
        sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Value is mandatory, ' " + "WHERE Value IS NULL " + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
        log.config("Value is mandatory=" + no);
        ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_underscoreAFTER_underscoreVALIDATE);
        commitEx();
        if (p_underscoreIsValidateOnly) {
            return "Validated";
        }
        int noInsert = 0;
        int noUpdate = 0;
        sql = new StringBuffer("SELECT * FROM I_underscoreBPartner " + "WHERE I_underscoreIsImported='N'").append(clientCheck);
        sql.append(" ORDER BY Value, I_underscoreBPartner_underscoreID");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), get_underscoreTrxName());
            rs = pstmt.executeQuery();
            String Old_underscoreBPValue = "";
            MBPartner bp = null;
            MBPartnerLocation bpl = null;
            while (rs.next()) {
                String New_underscoreBPValue = rs.getString("Value");
                X_underscoreI_underscoreBPartner impBP = new X_underscoreI_underscoreBPartner(getCtx(), rs, get_underscoreTrxName());
                log.fine("I_underscoreBPartner_underscoreID=" + impBP.getI_underscoreBPartner_underscoreID() + ", C_underscoreBPartner_underscoreID=" + impBP.getC_underscoreBPartner_underscoreID() + ", C_underscoreBPartner_underscoreLocation_underscoreID=" + impBP.getC_underscoreBPartner_underscoreLocation_underscoreID() + ", AD_underscoreUser_underscoreID=" + impBP.getAD_underscoreUser_underscoreID());
                if (!New_underscoreBPValue.equals(Old_underscoreBPValue)) {
                    bp = null;
                    if (impBP.getC_underscoreBPartner_underscoreID() == 0) {
                        if (impBP.getName() == null || impBP.getName().length() == 0) {
                            sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Invalid Name, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                            DB.executeUpdate(sql.toString(), get_underscoreTrxName());
                            continue;
                        }
                        bp = new MBPartner(impBP);
                        if (!impBP.get_underscoreValueAsString("AD_underscoreLanguage").equals("")) bp.set_underscoreValueOfColumn("AD_underscoreLanguage", impBP.get_underscoreValueAsString("AD_underscoreLanguage"));
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreBPTypeBR").equals("")) bp.set_underscoreValueOfColumn("lbr_underscoreBPTypeBR", impBP.get_underscoreValueAsString("lbr_underscoreBPTypeBR"));
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreCNPJ").equals("")) {
                            Boolean ok = org.adempierelbr.validator.ValidatorBPartner.validaCNPJ(impBP.get_underscoreValueAsString("lbr_underscoreCNPJ"));
                            if (ok) bp.set_underscoreValueOfColumn("lbr_underscoreCNPJ", impBP.get_underscoreValueAsString("lbr_underscoreCNPJ")); else {
                                sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Invalid CNPJ, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                                DB.executeUpdate(sql.toString(), get_underscoreTrxName());
                                continue;
                            }
                        }
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreCPF").equals("")) {
                            Boolean ok = org.adempierelbr.validator.ValidatorBPartner.validaCPF(impBP.get_underscoreValueAsString("lbr_underscoreCPF"));
                            if (ok) bp.set_underscoreValueOfColumn("lbr_underscoreCPF", impBP.get_underscoreValueAsString("lbr_underscoreCPF")); else {
                                sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Invalid CPF, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                                DB.executeUpdate(sql.toString(), get_underscoreTrxName());
                                continue;
                            }
                        }
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreIE").equals("")) {
                            bp.set_underscoreValueOfColumn("lbr_underscoreIE", impBP.get_underscoreValueAsString("lbr_underscoreIE"));
                            bp.set_underscoreValueOfColumn("lbr_underscoreIsIEExempt", false);
                        }
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreCCM").equals("")) bp.set_underscoreValueOfColumn("lbr_underscoreCCM", impBP.get_underscoreValueAsString("lbr_underscoreCCM"));
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreRG").equals("")) bp.set_underscoreValueOfColumn("lbr_underscoreRG", impBP.get_underscoreValueAsString("lbr_underscoreRG"));
                        if (!impBP.get_underscoreValueAsString("isVendor").equals("")) bp.set_underscoreValueOfColumn("isVendor", impBP.get_underscoreValueAsString("isVendor"));
                        if (!impBP.get_underscoreValueAsString("isCustomer").equals("")) bp.set_underscoreValueOfColumn("isCustomer", impBP.get_underscoreValueAsString("isCustomer"));
                        if (!impBP.get_underscoreValueAsString("isSalesRep").equals("")) bp.set_underscoreValueOfColumn("isSalesRep", impBP.get_underscoreValueAsString("isSalesRep"));
                        ModelValidationEngine.get().fireImportValidate(this, impBP, bp, ImportValidator.TIMING_underscoreAFTER_underscoreIMPORT);
                        setTypeOfBPartner(impBP, bp);
                        if (bp.save()) {
                            impBP.setC_underscoreBPartner_underscoreID(bp.getC_underscoreBPartner_underscoreID());
                            log.finest("Insert BPartner - " + bp.getC_underscoreBPartner_underscoreID());
                            noInsert++;
                        } else {
                            sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("(CASE WHEN (SELECT COUNT(*) FROM C_underscoreBPartner WHERE lbr_underscoreCNPJ IS NOT NULL AND lbr_underscoreCNPJ='").append(impBP.get_underscoreValueAsString("lbr_underscoreCNPJ")).append("') > 0 THEN 'CNPJ Duplicado, ' WHEN (SELECT COUNT(*) FROM C_underscoreBPartner WHERE lbr_underscoreCPF IS NOT NULL AND lbr_underscoreCPF='").append(impBP.get_underscoreValueAsString("lbr_underscoreCPF")).append("') > 0 THEN 'CPF Duplicado, ' END) ||").append("'Cannot Insert BPartner, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                            DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
                            continue;
                        }
                    } else {
                        bp = new MBPartner(getCtx(), impBP.getC_underscoreBPartner_underscoreID(), get_underscoreTrxName());
                        if (impBP.getName() != null) {
                            bp.setName(impBP.getName());
                            bp.setName2(impBP.getName2());
                        }
                        if (impBP.getDUNS() != null) bp.setDUNS(impBP.getDUNS());
                        if (impBP.getTaxID() != null) bp.setTaxID(impBP.getTaxID());
                        if (impBP.getNAICS() != null) bp.setNAICS(impBP.getNAICS());
                        if (impBP.getDescription() != null) bp.setDescription(impBP.getDescription());
                        if (impBP.getC_underscoreBP_underscoreGroup_underscoreID() != 0) bp.setC_underscoreBP_underscoreGroup_underscoreID(impBP.getC_underscoreBP_underscoreGroup_underscoreID());
                        ModelValidationEngine.get().fireImportValidate(this, impBP, bp, ImportValidator.TIMING_underscoreAFTER_underscoreIMPORT);
                        if (!impBP.get_underscoreValueAsString("AD_underscoreLanguage").equals("")) bp.set_underscoreValueOfColumn("AD_underscoreLanguage", impBP.get_underscoreValueAsString("AD_underscoreLanguage"));
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreBPTypeBR").equals("")) bp.set_underscoreValueOfColumn("lbr_underscoreBPTypeBR", impBP.get_underscoreValueAsString("lbr_underscoreBPTypeBR"));
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreCNPJ").equals("")) {
                            Boolean ok = org.adempierelbr.validator.ValidatorBPartner.validaCNPJ(impBP.get_underscoreValueAsString("lbr_underscoreCNPJ"));
                            if (ok) bp.set_underscoreValueOfColumn("lbr_underscoreCNPJ", impBP.get_underscoreValueAsString("lbr_underscoreCNPJ")); else {
                                sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Invalid CNPJ, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                                DB.executeUpdate(sql.toString(), get_underscoreTrxName());
                                continue;
                            }
                        }
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreCPF").equals("")) {
                            Boolean ok = org.adempierelbr.validator.ValidatorBPartner.validaCPF(impBP.get_underscoreValueAsString("lbr_underscoreCPF"));
                            if (ok) bp.set_underscoreValueOfColumn("lbr_underscoreCPF", impBP.get_underscoreValueAsString("lbr_underscoreCPF")); else {
                                sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Invalid CPF, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                                DB.executeUpdate(sql.toString(), get_underscoreTrxName());
                                continue;
                            }
                        }
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreIE").equals("")) bp.set_underscoreValueOfColumn("lbr_underscoreIE", impBP.get_underscoreValueAsString("lbr_underscoreIE"));
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreCCM").equals("")) bp.set_underscoreValueOfColumn("lbr_underscoreCCM", impBP.get_underscoreValueAsString("lbr_underscoreCCM"));
                        if (!impBP.get_underscoreValueAsString("lbr_underscoreRG").equals("")) bp.set_underscoreValueOfColumn("lbr_underscoreRG", impBP.get_underscoreValueAsString("lbr_underscoreRG"));
                        if (!impBP.get_underscoreValueAsString("isVendor").equals("")) bp.set_underscoreValueOfColumn("isVendor", impBP.get_underscoreValueAsString("isVendor"));
                        if (!impBP.get_underscoreValueAsString("isCustomer").equals("")) bp.set_underscoreValueOfColumn("isCustomer", impBP.get_underscoreValueAsString("isCustomer"));
                        if (!impBP.get_underscoreValueAsString("isSalesRep").equals("")) bp.set_underscoreValueOfColumn("isSalesRep", impBP.get_underscoreValueAsString("isSalesRep"));
                        setTypeOfBPartner(impBP, bp);
                        if (bp.save()) {
                            log.finest("Update BPartner - " + bp.getC_underscoreBPartner_underscoreID());
                            noUpdate++;
                        } else {
                            sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Cannot Update BPartner, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                            DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
                            continue;
                        }
                    }
                    bpl = null;
                    if (impBP.getC_underscoreBPartner_underscoreLocation_underscoreID() != 0) {
                        bpl = new MBPartnerLocation(getCtx(), impBP.getC_underscoreBPartner_underscoreLocation_underscoreID(), get_underscoreTrxName());
                        MLocation location = new MLocation(getCtx(), bpl.getC_underscoreLocation_underscoreID(), get_underscoreTrxName());
                        location.setC_underscoreCountry_underscoreID(impBP.getC_underscoreCountry_underscoreID());
                        location.setC_underscoreRegion_underscoreID(impBP.getC_underscoreRegion_underscoreID());
                        location.setCity(impBP.getCity());
                        location.setAddress1(impBP.getAddress1());
                        location.setAddress2(impBP.getAddress2());
                        location.setAddress3(impBP.get_underscoreValueAsString("Address3"));
                        location.setAddress4(impBP.get_underscoreValueAsString("Address4"));
                        location.setPostal(impBP.getPostal());
                        location.setPostal_underscoreAdd(impBP.getPostal_underscoreAdd());
                        if (!location.save()) log.warning("Location not updated"); else bpl.setC_underscoreLocation_underscoreID(location.getC_underscoreLocation_underscoreID());
                        if (impBP.getPhone() != null) bpl.setPhone(impBP.getPhone());
                        if (impBP.getPhone2() != null) bpl.setPhone2(impBP.getPhone2());
                        if (impBP.getFax() != null) bpl.setFax(impBP.getFax());
                        ModelValidationEngine.get().fireImportValidate(this, impBP, bpl, ImportValidator.TIMING_underscoreAFTER_underscoreIMPORT);
                        bpl.save();
                    } else if (impBP.getC_underscoreCountry_underscoreID() != 0 && impBP.getAddress1() != null && impBP.getCity() != null) {
                        MLocation location = new MLocation(getCtx(), impBP.getC_underscoreCountry_underscoreID(), impBP.getC_underscoreRegion_underscoreID(), impBP.getCity(), get_underscoreTrxName());
                        location.setAddress1(impBP.getAddress1());
                        location.setAddress2(impBP.getAddress2());
                        location.setAddress3(impBP.get_underscoreValueAsString("Address3"));
                        location.setAddress4(impBP.get_underscoreValueAsString("Address4"));
                        location.setPostal(impBP.getPostal());
                        location.setPostal_underscoreAdd(impBP.getPostal_underscoreAdd());
                        if (location.save()) log.finest("Insert Location - " + location.getC_underscoreLocation_underscoreID()); else {
                            rollback();
                            noInsert--;
                            sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Cannot Insert Location, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                            DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
                            continue;
                        }
                        bpl = new MBPartnerLocation(bp);
                        bpl.setC_underscoreLocation_underscoreID(location.getC_underscoreLocation_underscoreID());
                        bpl.setPhone(impBP.getPhone());
                        bpl.setPhone2(impBP.getPhone2());
                        bpl.setFax(impBP.getFax());
                        ModelValidationEngine.get().fireImportValidate(this, impBP, bpl, ImportValidator.TIMING_underscoreAFTER_underscoreIMPORT);
                        if (bpl.save()) {
                            log.finest("Insert BP Location - " + bpl.getC_underscoreBPartner_underscoreLocation_underscoreID());
                            impBP.setC_underscoreBPartner_underscoreLocation_underscoreID(bpl.getC_underscoreBPartner_underscoreLocation_underscoreID());
                        } else {
                            rollback();
                            noInsert--;
                            sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Cannot Insert BPLocation, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                            DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
                            continue;
                        }
                    }
                }
                Old_underscoreBPValue = New_underscoreBPValue;
                MUser user = null;
                if (impBP.getAD_underscoreUser_underscoreID() != 0) {
                    user = new MUser(getCtx(), impBP.getAD_underscoreUser_underscoreID(), get_underscoreTrxName());
                    if (user.getC_underscoreBPartner_underscoreID() == 0) user.setC_underscoreBPartner_underscoreID(bp.getC_underscoreBPartner_underscoreID()); else if (user.getC_underscoreBPartner_underscoreID() != bp.getC_underscoreBPartner_underscoreID()) {
                        rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'BP of User <> BP, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                        DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
                        continue;
                    }
                    if (impBP.getC_underscoreGreeting_underscoreID() != 0) user.setC_underscoreGreeting_underscoreID(impBP.getC_underscoreGreeting_underscoreID());
                    String name = impBP.getContactName();
                    if (name == null || name.length() == 0) name = impBP.getEMail();
                    user.setName(name);
                    if (impBP.getTitle() != null) user.setTitle(impBP.getTitle());
                    if (impBP.getContactDescription() != null) user.setDescription(impBP.getContactDescription());
                    if (impBP.getComments() != null) user.setComments(impBP.getComments());
                    if (impBP.getPhone() != null) user.setPhone(impBP.getPhone());
                    if (impBP.getPhone2() != null) user.setPhone2(impBP.getPhone2());
                    if (impBP.getFax() != null) user.setFax(impBP.getFax());
                    if (impBP.getEMail() != null) user.setEMail(impBP.getEMail());
                    if (impBP.getBirthday() != null) user.setBirthday(impBP.getBirthday());
                    if (bpl != null) user.setC_underscoreBPartner_underscoreLocation_underscoreID(bpl.getC_underscoreBPartner_underscoreLocation_underscoreID());
                    ModelValidationEngine.get().fireImportValidate(this, impBP, user, ImportValidator.TIMING_underscoreAFTER_underscoreIMPORT);
                    if (user.save()) {
                        log.finest("Update BP Contact - " + user.getAD_underscoreUser_underscoreID());
                    } else {
                        rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Cannot Update BP Contact, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                        DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
                        continue;
                    }
                } else if (impBP.getContactName() != null || impBP.getEMail() != null) {
                    user = new MUser(bp);
                    if (impBP.getC_underscoreGreeting_underscoreID() != 0) user.setC_underscoreGreeting_underscoreID(impBP.getC_underscoreGreeting_underscoreID());
                    String name = impBP.getContactName();
                    if (name == null || name.length() == 0) name = impBP.getEMail();
                    user.setName(name);
                    user.setTitle(impBP.getTitle());
                    user.setDescription(impBP.getContactDescription());
                    user.setComments(impBP.getComments());
                    user.setPhone(impBP.getPhone());
                    user.setPhone2(impBP.getPhone2());
                    user.setFax(impBP.getFax());
                    user.setEMail(impBP.getEMail());
                    user.setBirthday(impBP.getBirthday());
                    if (bpl != null) user.setC_underscoreBPartner_underscoreLocation_underscoreID(bpl.getC_underscoreBPartner_underscoreLocation_underscoreID());
                    ModelValidationEngine.get().fireImportValidate(this, impBP, user, ImportValidator.TIMING_underscoreAFTER_underscoreIMPORT);
                    if (user.save()) {
                        log.finest("Insert BP Contact - " + user.getAD_underscoreUser_underscoreID());
                        impBP.setAD_underscoreUser_underscoreID(user.getAD_underscoreUser_underscoreID());
                    } else {
                        rollback();
                        noInsert--;
                        sql = new StringBuffer("UPDATE I_underscoreBPartner i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append("'Cannot Insert BPContact, ' ").append("WHERE I_underscoreBPartner_underscoreID=").append(impBP.getI_underscoreBPartner_underscoreID());
                        DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
                        continue;
                    }
                }
                if (impBP.getR_underscoreInterestArea_underscoreID() != 0 && user != null) {
                    MContactInterest ci = MContactInterest.get(getCtx(), impBP.getR_underscoreInterestArea_underscoreID(), user.getAD_underscoreUser_underscoreID(), true, get_underscoreTrxName());
                    ci.save();
                }
                impBP.setI_underscoreIsImported(true);
                impBP.setProcessed(true);
                impBP.setProcessing(false);
                impBP.saveEx();
                commitEx();
            }
            DB.close(rs, pstmt);
        } catch (SQLException e) {
            rollback();
            throw new DBException(e, sql.toString());
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
            sql = new StringBuffer("UPDATE I_underscoreBPartner " + "SET I_underscoreIsImported='N', Updated=SysDate " + "WHERE I_underscoreIsImported<>'Y'").append(clientCheck);
            no = DB.executeUpdateEx(sql.toString(), get_underscoreTrxName());
            addLog(0, null, new BigDecimal(no), "@Errors@");
            addLog(0, null, new BigDecimal(noInsert), "@C_underscoreBPartner_underscoreID@: @Inserted@");
            addLog(0, null, new BigDecimal(noUpdate), "@C_underscoreBPartner_underscoreID@: @Updated@");
        }
        return "";
    }

