    @Override
    public void run() {
        EventType type = event.getEventType();
        IBaseObject field = event.getField();
        log.info("select----->" + field.getAttribute(IDatafield.URL));
        try {
            IParent parent = field.getParent();
            String name = field.getName();
            if (type == EventType.ON_underscoreBTN_underscoreCLICK) {
                invoke(parent, "eventRule_underscore" + name);
                Object value = event.get(Event.ARG_underscoreVALUE);
                if (value != null && value instanceof String[]) {
                    String[] args = (String[]) value;
                    for (String arg : args) log.info("argument data: " + arg);
                }
            } else if (type == EventType.ON_underscoreBEFORE_underscoreDOWNLOAD) invoke(parent, "eventRule_underscore" + name); else if (type == EventType.ON_underscoreVALUE_underscoreCHANGE) {
                String pattern = (String) event.get(Event.ARG_underscorePATTERN);
                Object value = event.get(Event.ARG_underscoreVALUE);
                Class cls = field.getDataType();
                if (cls == null || value == null || value.getClass().equals(cls)) field.setValue(value); else if (pattern == null) field.setValue(ConvertUtils.convert(value.toString(), cls)); else if (Date.class.isAssignableFrom(cls)) field.setValue(new SimpleDateFormat(pattern).parse((String) value)); else if (Number.class.isAssignableFrom(cls)) field.setValue(new DecimalFormat(pattern).parse((String) value)); else field.setValue(new MessageFormat(pattern).parse((String) value));
                invoke(parent, "checkRule_underscore" + name);
                invoke(parent, "defaultRule_underscore" + name);
            } else if (type == EventType.ON_underscoreROW_underscoreSELECTED) {
                log.info("table row selected.");
                Object selected = event.get(Event.ARG_underscoreROW_underscoreINDEX);
                if (selected instanceof Integer) presentation.setSelectedRowIndex((IModuleList) field, (Integer) selected); else if (selected instanceof List) {
                    String s = "";
                    String conn = "";
                    for (Integer item : (List<Integer>) selected) {
                        s = s + conn + item;
                        conn = ",";
                    }
                    log.info("row " + s + " line(s) been selected.");
                }
            } else if (type == EventType.ON_underscoreROW_underscoreDBLCLICK) {
                log.info("table row double-clicked.");
                presentation.setSelectedRowIndex((IModuleList) field, (Integer) event.get(Event.ARG_underscoreROW_underscoreINDEX));
            } else if (type == EventType.ON_underscoreROW_underscoreINSERT) {
                log.info("table row inserted.");
                listAdd((IModuleList) field, (Integer) event.get(Event.ARG_underscoreROW_underscoreINDEX));
            } else if (type == EventType.ON_underscoreROW_underscoreREMOVE) {
                log.info("table row removed.");
                listRemove((IModuleList) field, (Integer) event.get(Event.ARG_underscoreROW_underscoreINDEX));
            } else if (type == EventType.ON_underscoreFILE_underscoreUPLOAD) {
                log.info("file uploaded.");
                InputStream is = (InputStream) event.get(Event.ARG_underscoreVALUE);
                String uploadFileName = (String) event.get(Event.ARG_underscoreFILE_underscoreNAME);
                log.info("<-----file name:" + uploadFileName);
                OutputStream os = (OutputStream) field.getValue();
                IOUtils.copy(is, os);
                is.close();
                os.close();
            }
        } catch (Exception e) {
            if (field != null) log.info("field type is :" + field.getDataType().getName());
            log.info("select", e);
        }
    }

