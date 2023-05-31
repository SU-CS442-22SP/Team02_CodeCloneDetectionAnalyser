    public final synchronized boolean isValidLicenseFile() throws LicenseNotSetupException {
        if (!isSetup()) {
            throw new LicenseNotSetupException();
        }
        boolean returnValue = false;
        Properties properties = getLicenseFile();
        logger.debug("isValidLicenseFile: License to validate:");
        logger.debug(properties);
        StringBuffer validationStringBuffer = new StringBuffer();
        validationStringBuffer.append(LICENSE_underscoreKEY_underscoreKEY + ":" + properties.getProperty(LICENSE_underscoreKEY_underscoreKEY) + ",");
        validationStringBuffer.append(LICENSE_underscoreFILE_underscoreSTATUS_underscoreKEY + ":" + properties.getProperty(LICENSE_underscoreFILE_underscoreSTATUS_underscoreKEY) + ",");
        validationStringBuffer.append(LICENSE_underscoreFILE_underscoreUSERS_underscoreKEY + ":" + properties.getProperty(LICENSE_underscoreFILE_underscoreUSERS_underscoreKEY) + ",");
        validationStringBuffer.append(LICENSE_underscoreFILE_underscoreMAC_underscoreKEY + ":" + properties.getProperty(LICENSE_underscoreFILE_underscoreMAC_underscoreKEY) + ",");
        validationStringBuffer.append(LICENSE_underscoreFILE_underscoreHOST_underscoreNAME_underscoreKEY + ":" + properties.getProperty(LICENSE_underscoreFILE_underscoreHOST_underscoreNAME_underscoreKEY) + ",");
        validationStringBuffer.append(LICENSE_underscoreFILE_underscoreOFFSET_underscoreKEY + ":" + properties.getProperty(LICENSE_underscoreFILE_underscoreOFFSET_underscoreKEY) + ",");
        validationStringBuffer.append(LICENSE_underscoreFILE_underscoreEXP_underscoreDATE_underscoreKEY + ":" + properties.getProperty(LICENSE_underscoreFILE_underscoreEXP_underscoreDATE_underscoreKEY) + ",");
        validationStringBuffer.append(LICENSE_underscoreEXPIRES_underscoreKEY + ":" + properties.getProperty(LICENSE_underscoreEXPIRES_underscoreKEY));
        logger.debug("isValidLicenseFile: Validation String Buffer: " + validationStringBuffer.toString());
        String validationKey = (String) properties.getProperty(LICENSE_underscoreFILE_underscoreSHA_underscoreKEY);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(validationStringBuffer.toString().getBytes());
            String newValidation = Base64.encode(messageDigest.digest());
            if (newValidation.equals(validationKey)) {
                if (getMACAddress().equals(Settings.getInstance().getMACAddress())) {
                    returnValue = true;
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception in LicenseInstanceVO.isValidLicenseFile");
        }
        return returnValue;
    }

