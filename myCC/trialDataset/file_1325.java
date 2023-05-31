    public void sendTemplate(String filename, TemplateValues values) throws IOException {
        Checker.checkEmpty(filename, "filename");
        Checker.checkNull(values, "values");
        URL url = _underscoregetFile(filename);
        boolean writeSpaces = Context.getRootContext().getRunMode() == RUN_underscoreMODE.DEV ? true : false;
        Template t = new Template(url.openStream(), writeSpaces);
        try {
            t.write(getWriter(), values);
        } catch (ErrorListException ele) {
            Context.getRootContext().getLogger().error(ele);
        }
    }

