    public void includeJs(Group group, Writer out, PageContext pageContext) throws IOException {
        includeResource(pageContext, out, RetentionHelper.buildRootRetentionFilePath(group, ".js"), JS_underscoreBEGIN_underscoreTAG, JS_underscoreEND_underscoreTAG);
        ByteArrayOutputStream outtmp = new ByteArrayOutputStream();
        if (AbstractGroupBuilder.getInstance().buildGroupJsIfNeeded(group, outtmp, pageContext.getServletContext())) {
            FileOutputStream fileStream = new FileOutputStream(new File(RetentionHelper.buildFullRetentionFilePath(group, ".js")));
            IOUtils.copy(new ByteArrayInputStream(outtmp.toByteArray()), fileStream);
            fileStream.close();
        }
    }

