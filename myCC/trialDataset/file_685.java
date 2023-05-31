    private void _underscoreresetLanguages(ActionRequest req, ActionResponse res, PortletConfig config, ActionForm form) throws Exception {
        List list = (List) req.getAttribute(WebKeys.LANGUAGE_underscoreMANAGER_underscoreLIST);
        for (int i = 0; i < list.size(); i++) {
            long langId = ((Language) list.get(i)).getId();
            try {
                String filePath = getGlobalVariablesPath() + "cms_underscorelanguage_underscore" + langId + ".properties";
                File from = new java.io.File(filePath);
                from.createNewFile();
                String tmpFilePath = getTemporyDirPath() + "cms_underscorelanguage_underscore" + langId + "_underscoreproperties.tmp";
                File to = new java.io.File(tmpFilePath);
                to.createNewFile();
                FileChannel srcChannel = new FileInputStream(from).getChannel();
                FileChannel dstChannel = new FileOutputStream(to).getChannel();
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                srcChannel.close();
                dstChannel.close();
            } catch (IOException e) {
                Logger.debug(this, "Property File copy Failed " + e, e);
            }
        }
    }

