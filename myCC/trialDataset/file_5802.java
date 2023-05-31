    private static void _underscorecheckConfigFile() throws Exception {
        try {
            String filePath = getUserManagerConfigPath() + "user_underscoremanager_underscoreconfig.properties";
            boolean copy = false;
            File from = new java.io.File(filePath);
            if (!from.exists()) {
                Properties properties = new Properties();
                properties.put(Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreMIDDLE_underscoreNAME_underscorePROPNAME"), Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreMIDDLE_underscoreNAME_underscoreVISIBILITY"));
                properties.put(Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreDATE_underscoreOF_underscoreBIRTH_underscorePROPNAME"), Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreDATE_underscoreOF_underscoreBIRTH_underscoreVISIBILITY"));
                properties.put(Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreCELL_underscorePROPNAME"), Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreCELL_underscoreVISIBILITY"));
                properties.put(Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreCATEGORIES_underscorePROPNAME"), Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreCATEGORIES_underscoreVISIBILITY"));
                Company comp = PublicCompanyFactory.getDefaultCompany();
                int numberGenericVariables = Config.getIntProperty("MAX_underscoreNUMBER_underscoreVARIABLES_underscoreTO_underscoreSHOW");
                for (int i = 1; i <= numberGenericVariables; i++) {
                    properties.put(LanguageUtil.get(comp.getCompanyId(), comp.getLocale(), "user.profile.var" + i).replace(" ", "_underscore"), Config.getStringProperty("ADDITIONAL_underscoreINFO_underscoreDEFAULT_underscoreVISIBILITY"));
                }
                try {
                    properties.store(new java.io.FileOutputStream(filePath), null);
                } catch (Exception e) {
                    Logger.error(UserManagerPropertiesFactory.class, e.getMessage(), e);
                }
                from = new java.io.File(filePath);
                copy = true;
            }
            String tmpFilePath = UtilMethods.getTemporaryDirPath() + "user_underscoremanager_underscoreconfig_underscoreproperties.tmp";
            File to = new java.io.File(tmpFilePath);
            if (!to.exists()) {
                to.createNewFile();
                copy = true;
            }
            if (copy) {
                FileChannel srcChannel = new FileInputStream(from).getChannel();
                FileChannel dstChannel = new FileOutputStream(to).getChannel();
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                srcChannel.close();
                dstChannel.close();
            }
        } catch (IOException e) {
            Logger.error(UserManagerPropertiesFactory.class, "_underscorecheckLanguagesFiles:Property File Copy Failed " + e, e);
        }
    }

