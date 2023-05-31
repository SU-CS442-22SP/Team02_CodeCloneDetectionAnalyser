    protected String doIt() throws java.lang.Exception {
        StringBuffer sql = null;
        int no = 0;
        String clientCheck = " AND AD_underscoreClient_underscoreID=" + m_underscoreAD_underscoreClient_underscoreID;
        String[] strFields = new String[] { "Value", "Name", "Description", "DocumentNote", "Help", "UPC", "SKU", "Classification", "ProductType", "Discontinued", "DiscontinuedBy", "ImageURL", "DescriptionURL" };
        for (int i = 0; i < strFields.length; i++) {
            sql = new StringBuffer("UPDATE I_underscorePRODUCT i " + "SET ").append(strFields[i]).append(" = (SELECT ").append(strFields[i]).append(" FROM M_underscoreProduct p" + " WHERE i.M_underscoreProduct_underscoreID=p.M_underscoreProduct_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID) " + "WHERE M_underscoreProduct_underscoreID IS NOT NULL" + " AND EXISTS (SELECT * FROM M_underscoreProduct p WHERE " + strFields[i] + " IS NOT NULL AND p.M_underscoreProduct_underscoreID = i.M_underscoreProduct_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID)" + " AND I_underscoreIsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + strFields[i] + " - default from existing Product=" + no);
            }
        }
        String[] numFields = new String[] { "C_underscoreUOM_underscoreID", "M_underscoreProduct_underscoreCategory_underscoreID", "Volume", "Weight", "ShelfWidth", "ShelfHeight", "ShelfDepth", "UnitsPerPallet", "M_underscoreProduct_underscoreFamily_underscoreID" };
        for (int i = 0; i < numFields.length; i++) {
            sql = new StringBuffer("UPDATE I_underscorePRODUCT i " + "SET ").append(numFields[i]).append(" = (SELECT ").append(numFields[i]).append(" FROM M_underscoreProduct p" + " WHERE i.M_underscoreProduct_underscoreID=p.M_underscoreProduct_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID) " + "WHERE M_underscoreProduct_underscoreID IS NOT NULL" + " AND EXISTS (SELECT * FROM M_underscoreProduct p WHERE " + numFields[i] + " IS NOT NULL AND p.M_underscoreProduct_underscoreID = i.M_underscoreProduct_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID)" + " AND I_underscoreIsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + numFields[i] + " default from existing Product=" + no);
            }
        }
        String[] strFieldsPO = new String[] { "UPC", "PriceEffective", "VendorProductNo", "VendorCategory", "Manufacturer", "Discontinued", "DiscontinuedBy" };
        for (int i = 0; i < strFieldsPO.length; i++) {
            sql = new StringBuffer("UPDATE I_underscorePRODUCT i " + "SET ").append(strFieldsPO[i]).append(" = (SELECT ").append(strFieldsPO[i]).append(" FROM M_underscoreProduct_underscorePO p" + " WHERE i.M_underscoreProduct_underscoreID=p.M_underscoreProduct_underscoreID AND i.C_underscoreBPartner_underscoreID=p.C_underscoreBPartner_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID) " + "WHERE M_underscoreProduct_underscoreID IS NOT NULL AND C_underscoreBPartner_underscoreID IS NOT NULL" + " AND EXISTS (SELECT * FROM M_underscoreProduct_underscorePO p WHERE " + strFieldsPO[i] + " IS NOT NULL AND p.M_underscoreProduct_underscoreID = i.M_underscoreProduct_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID)" + " AND I_underscoreIsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + strFieldsPO[i] + " default from existing Product PO=" + no);
            }
        }
        String[] numFieldsPO = new String[] { "C_underscoreUOM_underscoreID", "C_underscoreCurrency_underscoreID", "RoyaltyAmt", "Order_underscoreMin", "Order_underscorePack", "CostPerOrder", "DeliveryTime_underscorePromised" };
        for (int i = 0; i < numFieldsPO.length; i++) {
            sql = new StringBuffer("UPDATE I_underscorePRODUCT i " + "SET ").append(numFieldsPO[i]).append(" = (SELECT ").append(numFieldsPO[i]).append(" FROM M_underscoreProduct_underscorePO p" + " WHERE i.M_underscoreProduct_underscoreID=p.M_underscoreProduct_underscoreID AND i.C_underscoreBPartner_underscoreID=p.C_underscoreBPartner_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID) " + "WHERE M_underscoreProduct_underscoreID IS NOT NULL AND C_underscoreBPartner_underscoreID IS NOT NULL" + " AND EXISTS (SELECT * FROM M_underscoreProduct_underscorePO p WHERE " + numFieldsPO[i] + " IS NOT NULL AND p.M_underscoreProduct_underscoreID = i.M_underscoreProduct_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID)" + " AND I_underscoreIsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + numFieldsPO[i] + " default from existing Product PO=" + no);
            }
        }
        numFieldsPO = new String[] { "PriceList", "PricePO" };
        for (int i = 0; i < numFieldsPO.length; i++) {
            sql = new StringBuffer("UPDATE I_underscorePRODUCT i " + "SET ").append(numFieldsPO[i]).append(" = (SELECT ").append(numFieldsPO[i]).append(" FROM M_underscoreProduct_underscorePO p" + " WHERE i.M_underscoreProduct_underscoreID=p.M_underscoreProduct_underscoreID AND i.C_underscoreBPartner_underscoreID=p.C_underscoreBPartner_underscoreID AND i.AD_underscoreClient_underscoreID=p.AD_underscoreClient_underscoreID) " + "WHERE M_underscoreProduct_underscoreID IS NOT NULL AND C_underscoreBPartner_underscoreID IS NOT NULL" + " AND (").append(numFieldsPO[i]).append(" IS NULL OR ").append(numFieldsPO[i]).append("=0)" + " AND I_underscoreIsImported='N'").append(clientCheck);
            no = DB.executeUpdate(sql.toString());
            if (no != 0) {
                log.fine("doIt - " + numFieldsPO[i] + " default from existing Product PO=" + no);
            }
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET X12DE355 = " + "(SELECT X12DE355 FROM C_underscoreUOM u WHERE u.IsDefault='Y' AND u.AD_underscoreClient_underscoreID IN (0,i.AD_underscoreClient_underscoreID) AND ROWNUM=1) " + "WHERE X12DE355 IS NULL AND C_underscoreUOM_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("doIt - Set UOM Default=" + no);
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET C_underscoreUOM_underscoreID = (SELECT C_underscoreUOM_underscoreID FROM C_underscoreUOM u WHERE u.X12DE355=i.X12DE355 AND u.AD_underscoreClient_underscoreID IN (0,i.AD_underscoreClient_underscoreID)) " + "WHERE C_underscoreUOM_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.info("doIt - Set UOM=" + no);
        sql = new StringBuffer("UPDATE I_underscoreProduct " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Invalid UOM, ' " + "WHERE C_underscoreUOM_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.fine("doIt - Invalid UOM=" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct " + "SET ProductCategory_underscoreValue=(SELECT Value FROM M_underscoreProduct_underscoreCategory" + " WHERE IsDefault='Y' AND AD_underscoreClient_underscoreID=").append(m_underscoreAD_underscoreClient_underscoreID).append(" AND ROWNUM=1) " + "WHERE ProductCategory_underscoreValue IS NULL AND M_underscoreProduct_underscoreCategory_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("doIt - Set Category Default=" + no);
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET M_underscoreProduct_underscoreCategory_underscoreID=(SELECT M_underscoreProduct_underscoreCategory_underscoreID FROM M_underscoreProduct_underscoreCategory c" + " WHERE i.ProductCategory_underscoreValue=c.Value AND i.AD_underscoreClient_underscoreID=c.AD_underscoreClient_underscoreID) " + "WHERE M_underscoreProduct_underscoreCategory_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.info("doIt - Set Category=" + no);
        sql = new StringBuffer("UPDATE I_underscoreProduct " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Invalid ProdCategorty,' " + "WHERE M_underscoreProduct_underscoreCategory_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.fine("doIt - Invalid Category=" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET ISO_underscoreCode=(SELECT ISO_underscoreCode FROM C_underscoreCurrency c" + " INNER JOIN C_underscoreAcctSchema a ON (a.C_underscoreCurrency_underscoreID=c.C_underscoreCurrency_underscoreID)" + " INNER JOIN AD_underscoreClientInfo fo ON (a.C_underscoreAcctSchema_underscoreID=fo.C_underscoreAcctSchema1_underscoreID)" + " WHERE fo.AD_underscoreClient_underscoreID=i.AD_underscoreClient_underscoreID) " + "WHERE C_underscoreCurrency_underscoreID IS NULL AND ISO_underscoreCode IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.fine("doIt - Set Currency Default=" + no);
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET C_underscoreCurrency_underscoreID=(SELECT C_underscoreCurrency_underscoreID FROM C_underscoreCurrency c" + " WHERE i.ISO_underscoreCode=c.ISO_underscoreCode AND c.AD_underscoreClient_underscoreID IN (0,i.AD_underscoreClient_underscoreID)) " + "WHERE C_underscoreCurrency_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.info("doIt- Set Currency=" + no);
        sql = new StringBuffer("UPDATE I_underscoreProduct " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Currency,' " + "WHERE C_underscoreCurrency_underscoreID IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.fine("doIt - Invalid Currency=" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Invalid ProductType,' " + "WHERE ProductType NOT IN ('I','S')" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.fine("doIt - Invalid ProductType=" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=Value not unique,' " + "WHERE I_underscoreIsImported<>'Y'" + " AND Value IN (SELECT Value FROM I_underscoreProduct pr WHERE i.AD_underscoreClient_underscoreID=pr.AD_underscoreClient_underscoreID GROUP BY Value HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.warning("doIt - Not Unique Value=" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=UPC not unique,' " + "WHERE I_underscoreIsImported<>'Y'" + " AND UPC IN (SELECT UPC FROM I_underscoreProduct pr WHERE i.AD_underscoreClient_underscoreID=pr.AD_underscoreClient_underscoreID GROUP BY UPC HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.warning("doIt - Not Unique UPC=" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=No Mandatory Value,' " + "WHERE Value IS NULL" + " AND I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.warning("doIt - No Mandatory Value=" + no);
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct " + "SET VendorProductNo=Value " + "WHERE C_underscoreBPartner_underscoreID IS NOT NULL AND VendorProductNo IS NULL" + " AND I_underscoreIsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        log.info("doIt - VendorProductNo Set to Value=" + no);
        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||'ERR=VendorProductNo not unique,' " + "WHERE I_underscoreIsImported<>'Y'" + " AND C_underscoreBPartner_underscoreID IS NOT NULL" + " AND (C_underscoreBPartner_underscoreID, VendorProductNo) IN " + " (SELECT C_underscoreBPartner_underscoreID, VendorProductNo FROM I_underscoreProduct pr WHERE i.AD_underscoreClient_underscoreID=pr.AD_underscoreClient_underscoreID GROUP BY C_underscoreBPartner_underscoreID, VendorProductNo HAVING COUNT(*) > 1)").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        if (no != 0) {
            log.warning("doIt - Not Unique VendorProductNo=" + no);
        }
        int C_underscoreTaxCategory_underscoreID = 0;
        try {
            PreparedStatement pstmt = DB.prepareStatement("SELECT C_underscoreTaxCategory_underscoreID FROM C_underscoreTaxCategory WHERE IsDefault='Y'" + clientCheck);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                C_underscoreTaxCategory_underscoreID = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new Exception("doIt - TaxCategory", e);
        }
        log.fine("doIt - C_underscoreTaxCategory_underscoreID=" + C_underscoreTaxCategory_underscoreID);
        int noInsert = 0;
        int noUpdate = 0;
        int noInsertPO = 0;
        int noUpdatePO = 0;
        log.fine("doIt - start inserting/updating ...");
        sql = new StringBuffer("SELECT I_underscoreProduct_underscoreID, M_underscoreProduct_underscoreID, C_underscoreBPartner_underscoreID " + "FROM I_underscoreProduct WHERE I_underscoreIsImported='N'").append(clientCheck);
        Connection conn = DB.createConnection(false, Connection.TRANSACTION_underscoreREAD_underscoreCOMMITTED);
        try {
            PreparedStatement pstmt_underscoreinsertProduct = conn.prepareStatement("INSERT INTO M_underscoreProduct (M_underscoreProduct_underscoreID," + "AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,IsActive,Created,CreatedBy,Updated,UpdatedBy," + "Value,Name,Description,DocumentNote,Help," + "UPC,SKU,C_underscoreUOM_underscoreID,IsSummary,M_underscoreProduct_underscoreCategory_underscoreID,C_underscoreTaxCategory_underscoreID," + "ProductType,ImageURL,DescriptionURL,M_underscoreProduct_underscoreFamily_underscoreID) " + "SELECT ?," + "AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,'Y',CURRENT_underscoreTIMESTAMP,CreatedBy,CURRENT_underscoreTIMESTAMP,UpdatedBy," + "Value,Name,Description,DocumentNote,Help," + "UPC,SKU,C_underscoreUOM_underscoreID,'N',M_underscoreProduct_underscoreCategory_underscoreID," + C_underscoreTaxCategory_underscoreID + "," + "ProductType,ImageURL,DescriptionURL,M_underscoreProduct_underscoreCategory_underscoreID " + "FROM I_underscoreProduct " + "WHERE I_underscoreProduct_underscoreID=?");
            PreparedStatement pstmt_underscoreupdateProduct = conn.prepareStatement("UPDATE M_underscorePRODUCT " + "SET Value=aux.value" + ",Name=aux.Name" + ",Description=aux.Description" + ",DocumentNote=aux.DocumentNote" + ",Help=aux.Help" + ",UPC=aux.UPC" + ",SKU=aux.SKU" + ",C_underscoreUOM_underscoreID=aux.C_underscoreUOM_underscoreID" + ",M_underscoreProduct_underscoreCategory_underscoreID=aux.M_underscoreProduct_underscoreCategory_underscoreID" + ",Classification=aux.Classification" + ",ProductType=aux.ProductType" + ",Volume=aux.Volume" + ",Weight=aux.Weight" + ",ShelfWidth=aux.ShelfWidth" + ",ShelfHeight=aux.ShelfHeight" + ",ShelfDepth=aux.ShelfDepth" + ",UnitsPerPallet=aux.UnitsPerPallet" + ",Discontinued=aux.Discontinued" + ",DiscontinuedBy=aux.DiscontinuedBy" + ",Updated=current_underscoretimestamp" + ",UpdatedBy=aux.UpdatedBy" + " from (SELECT Value,Name,Description,DocumentNote,Help,UPC,SKU,C_underscoreUOM_underscoreID,M_underscoreProduct_underscoreCategory_underscoreID,Classification,ProductType,Volume,Weight,ShelfWidth,ShelfHeight,ShelfDepth,UnitsPerPallet,Discontinued,DiscontinuedBy,UpdatedBy FROM I_underscoreProduct WHERE I_underscoreProduct_underscoreID=?) as aux" + " WHERE M_underscoreProduct_underscoreID=?");
            PreparedStatement pstmt_underscoreupdateProductPO = conn.prepareStatement("UPDATE M_underscoreProduct_underscorePO " + "SET IsCurrentVendor='Y'" + ",C_underscoreUOM_underscoreID=aux1.C_underscoreUOM_underscoreID" + ",C_underscoreCurrency_underscoreID=aux1.C_underscoreCurrency_underscoreID" + ",UPC=aux1.UPC" + ",PriceList=aux1.PriceList" + ",PricePO=aux1.PricePO" + ",RoyaltyAmt=aux1.RoyaltyAmt" + ",PriceEffective=aux1.PriceEffective" + ",VendorProductNo=aux1.VendorProductNo" + ",VendorCategory=aux1.VendorCategory" + ",Manufacturer=aux1.Manufacturer" + ",Discontinued=aux1.Discontinued" + ",DiscontinuedBy=aux1.DiscontinuedBy" + ",Order_underscoreMin=aux1.Order_underscoreMin" + ",Order_underscorePack=aux1.Order_underscorePack" + ",CostPerOrder=aux1.CostPerOrder" + ",DeliveryTime_underscorePromised=aux1.DeliveryTime_underscorePromised" + ",Updated=current_underscoretimestamp" + ",UpdatedBy=aux1.UpdatedBy" + " from (SELECT 'Y',C_underscoreUOM_underscoreID,C_underscoreCurrency_underscoreID,UPC,PriceList,PricePO,RoyaltyAmt,PriceEffective,VendorProductNo,VendorCategory,Manufacturer,Discontinued,DiscontinuedBy,Order_underscoreMin,Order_underscorePack,CostPerOrder,DeliveryTime_underscorePromised,UpdatedBy FROM I_underscoreProduct WHERE I_underscoreProduct_underscoreID=?) as aux1" + " WHERE M_underscoreProduct_underscoreID=? AND C_underscoreBPartner_underscoreID=?");
            PreparedStatement pstmt_underscoreinsertProductPO = conn.prepareStatement("INSERT INTO M_underscoreProduct_underscorePO (M_underscoreProduct_underscoreID,C_underscoreBPartner_underscoreID, " + "AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,IsActive,Created,CreatedBy,Updated,UpdatedBy," + "IsCurrentVendor,C_underscoreUOM_underscoreID,C_underscoreCurrency_underscoreID,UPC," + "PriceList,PricePO,RoyaltyAmt,PriceEffective," + "VendorProductNo,VendorCategory,Manufacturer," + "Discontinued,DiscontinuedBy,Order_underscoreMin,Order_underscorePack," + "CostPerOrder,DeliveryTime_underscorePromised) " + "SELECT ?,?, " + "AD_underscoreClient_underscoreID,AD_underscoreOrg_underscoreID,'Y',d,CreatedBy,CURRENT_underscoreTIMESTAMP,UpdatedBy," + "'Y',C_underscoreUOM_underscoreID,C_underscoreCurrency_underscoreID,UPC," + "PriceList,PricePO,RoyaltyAmt,PriceEffective," + "VendorProductNo,VendorCategory,Manufacturer," + "Discontinued,DiscontinuedBy,Order_underscoreMin,Order_underscorePack," + "CostPerOrder,DeliveryTime_underscorePromised " + "FROM I_underscoreProduct " + "WHERE I_underscoreProduct_underscoreID=?");
            PreparedStatement pstmt_underscoresetImported = conn.prepareStatement("UPDATE I_underscoreProduct SET I_underscoreIsImported='Y', M_underscoreProduct_underscoreID=?, " + "Updated=CURRENT_underscoreTIMESTAMP, Processed='Y' WHERE I_underscoreProduct_underscoreID=?");
            PreparedStatement pstmt = DB.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int I_underscoreProduct_underscoreID = rs.getInt(1);
                int M_underscoreProduct_underscoreID = rs.getInt(2);
                int C_underscoreBPartner_underscoreID = rs.getInt(3);
                boolean newProduct = M_underscoreProduct_underscoreID == 0;
                log.fine("I_underscoreProduct_underscoreID=" + I_underscoreProduct_underscoreID + ", M_underscoreProduct_underscoreID=" + M_underscoreProduct_underscoreID + ", C_underscoreBPartner_underscoreID=" + C_underscoreBPartner_underscoreID);
                if (newProduct) {
                    M_underscoreProduct_underscoreID = DB.getNextID(m_underscoreAD_underscoreClient_underscoreID, "M_underscoreProduct", null);
                    pstmt_underscoreinsertProduct.setInt(1, M_underscoreProduct_underscoreID);
                    pstmt_underscoreinsertProduct.setInt(2, I_underscoreProduct_underscoreID);
                    try {
                        no = pstmt_underscoreinsertProduct.executeUpdate();
                        log.finer("Insert Product = " + no);
                        noInsert++;
                    } catch (SQLException ex) {
                        log.warning("Insert Product - " + ex.toString());
                        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Insert Product: " + ex.toString())).append("WHERE I_underscoreProduct_underscoreID=").append(I_underscoreProduct_underscoreID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                } else {
                    pstmt_underscoreupdateProduct.setInt(1, I_underscoreProduct_underscoreID);
                    pstmt_underscoreupdateProduct.setInt(2, M_underscoreProduct_underscoreID);
                    try {
                        no = pstmt_underscoreupdateProduct.executeUpdate();
                        log.finer("Update Product = " + no);
                        noUpdate++;
                    } catch (SQLException ex) {
                        log.warning("Update Product - " + ex.toString());
                        sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Update Product: " + ex.toString())).append("WHERE I_underscoreProduct_underscoreID=").append(I_underscoreProduct_underscoreID);
                        DB.executeUpdate(sql.toString());
                        continue;
                    }
                }
                if (C_underscoreBPartner_underscoreID != 0) {
                    no = 0;
                    if (!newProduct) {
                        pstmt_underscoreupdateProductPO.setInt(1, I_underscoreProduct_underscoreID);
                        pstmt_underscoreupdateProductPO.setInt(2, M_underscoreProduct_underscoreID);
                        pstmt_underscoreupdateProductPO.setInt(3, C_underscoreBPartner_underscoreID);
                        try {
                            no = pstmt_underscoreupdateProductPO.executeUpdate();
                            log.finer("Update Product_underscorePO = " + no);
                            noUpdatePO++;
                        } catch (SQLException ex) {
                            log.warning("Update Product_underscorePO - " + ex.toString());
                            noUpdate--;
                            conn.rollback();
                            sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Update Product_underscorePO: " + ex.toString())).append("WHERE I_underscoreProduct_underscoreID=").append(I_underscoreProduct_underscoreID);
                            DB.executeUpdate(sql.toString());
                            continue;
                        }
                    }
                    if (no == 0) {
                        pstmt_underscoreinsertProductPO.setInt(1, M_underscoreProduct_underscoreID);
                        pstmt_underscoreinsertProductPO.setInt(2, C_underscoreBPartner_underscoreID);
                        pstmt_underscoreinsertProductPO.setInt(3, I_underscoreProduct_underscoreID);
                        try {
                            no = pstmt_underscoreinsertProductPO.executeUpdate();
                            log.finer("Insert Product_underscorePO = " + no);
                            noInsertPO++;
                        } catch (SQLException ex) {
                            log.warning("Insert Product_underscorePO - " + ex.toString());
                            noInsert--;
                            conn.rollback();
                            sql = new StringBuffer("UPDATE I_underscoreProduct i " + "SET I_underscoreIsImported='E', I_underscoreErrorMsg=I_underscoreErrorMsg||").append(DB.TO_underscoreSTRING("Insert Product_underscorePO: " + ex.toString())).append("WHERE I_underscoreProduct_underscoreID=").append(I_underscoreProduct_underscoreID);
                            DB.executeUpdate(sql.toString());
                            continue;
                        }
                    }
                }
                pstmt_underscoresetImported.setInt(1, M_underscoreProduct_underscoreID);
                pstmt_underscoresetImported.setInt(2, I_underscoreProduct_underscoreID);
                no = pstmt_underscoresetImported.executeUpdate();
                conn.commit();
            }
            rs.close();
            pstmt.close();
            pstmt_underscoreinsertProduct.close();
            pstmt_underscoreupdateProduct.close();
            pstmt_underscoreinsertProductPO.close();
            pstmt_underscoreupdateProductPO.close();
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
            log.log(Level.SEVERE, "doIt", e);
            throw new Exception("doIt", e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            conn = null;
        }
        sql = new StringBuffer("UPDATE I_underscoreProduct " + "SET I_underscoreIsImported='N', Updated=CURRENT_underscoreTIMESTAMP " + "WHERE I_underscoreIsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString());
        StringBuffer infoReturn = new StringBuffer("");
        infoReturn.append("<tr><td>@Errors@</td><td>").append(no).append("</td></tr>");
        infoReturn.append("<tr><td>@M_underscoreProduct_underscoreID@: @Inserted@</td><td>").append(noInsert).append("</td></tr>");
        infoReturn.append("<tr><td>@M_underscoreProduct_underscoreID@: @Updated@</td><td>").append(noUpdate).append("</td></tr>");
        infoReturn.append("<tr><td>@M_underscoreProduct_underscoreID@ @Purchase@: @Inserted@</td><td>").append(noInsertPO).append("</td></tr>");
        infoReturn.append("<tr><td>@M_underscoreProduct_underscoreID@ @Purchase@: @Updated@</td><td>").append(noUpdatePO).append("</td></tr>");
        return infoReturn.toString();
    }

