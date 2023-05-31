    public static void _underscoresave(PortletRequest req, PortletResponse res, PortletConfig config, ActionForm form) throws Exception {
        try {
            String filePath = getUserManagerConfigPath() + "user_underscoremanager_underscoreconfig.properties";
            String tmpFilePath = UtilMethods.getTemporaryDirPath() + "user_underscoremanager_underscoreconfig_underscoreproperties.tmp";
            File from = new java.io.File(tmpFilePath);
            from.createNewFile();
            File to = new java.io.File(filePath);
            to.createNewFile();
            FileChannel srcChannel = new FileInputStream(from).getChannel();
            FileChannel dstChannel = new FileOutputStream(to).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (NonWritableChannelException we) {
        } catch (IOException e) {
            Logger.error(UserManagerPropertiesFactory.class, "Property File save Failed " + e, e);
        }
        SessionMessages.add(req, "message", "message.usermanager.display.save");
    }

