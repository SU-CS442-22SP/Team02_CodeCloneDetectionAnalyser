    public void readScalarpvviewerDocument(URL url) {
        try {
            String xmlData = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            boolean cont = true;
            while (cont) {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.length() > 0 && line.charAt(0) != '%') {
                    xmlData = xmlData + line + System.getProperty("line.separator");
                }
                if (line.length() > 1 && line.charAt(0) == '%' && line.charAt(1) == '=') {
                    cont = false;
                }
            }
            XmlDataAdaptor readAdp = null;
            readAdp = XmlDataAdaptor.adaptorForString(xmlData, false);
            if (readAdp != null) {
                XmlDataAdaptor scalarpvviewerData_underscoreAdaptor = readAdp.childAdaptor(dataRootName);
                if (scalarpvviewerData_underscoreAdaptor != null) {
                    cleanUp();
                    setTitle(scalarpvviewerData_underscoreAdaptor.stringValue("title"));
                    XmlDataAdaptor params_underscorefont = scalarpvviewerData_underscoreAdaptor.childAdaptor("font");
                    int font_underscoresize = params_underscorefont.intValue("size");
                    int style = params_underscorefont.intValue("style");
                    String font_underscoreFamily = params_underscorefont.stringValue("name");
                    globalFont = new Font(font_underscoreFamily, style, font_underscoresize);
                    fontSize_underscorePrefPanel_underscoreSpinner.setValue(new Integer(font_underscoresize));
                    setFontForAll(globalFont);
                    XmlDataAdaptor params_underscorepts = scalarpvviewerData_underscoreAdaptor.childAdaptor("Panels_underscoretitles");
                    viewValuesPanel.setTitle(params_underscorepts.stringValue("values_underscorepanel_underscoretitle"));
                    viewChartsPanel.setTitle(params_underscorepts.stringValue("charts_underscorepanel_underscoretitle"));
                    XmlDataAdaptor params_underscoredata = scalarpvviewerData_underscoreAdaptor.childAdaptor("PARAMETERS");
                    if (params_underscoredata != null) {
                        viewValuesPanel.setLastMemorizingTime(params_underscoredata.stringValue("lastMemorizingTime"));
                    } else {
                        viewValuesPanel.setLastMemorizingTime("No Info. See time of file modification.");
                    }
                    XmlDataAdaptor params_underscoreuc = scalarpvviewerData_underscoreAdaptor.childAdaptor("UpdateController");
                    double updateTime = params_underscoreuc.doubleValue("updateTime");
                    updatingController.setUpdateTime(updateTime);
                    double chartUpdateTime = params_underscoreuc.doubleValue("ChartUpdateTime");
                    viewChartsPanel.setTimeStep(chartUpdateTime);
                    viewValuesPanel.listenModeOn(params_underscoreuc.booleanValue("listenToEPICS"));
                    viewChartsPanel.recordOn(params_underscoreuc.booleanValue("recordChartFromEPICS"));
                    java.util.Iterator<XmlDataAdaptor> pvIt = scalarpvviewerData_underscoreAdaptor.childAdaptorIterator("ScalarPV");
                    while (pvIt.hasNext()) {
                        XmlDataAdaptor pvDA = pvIt.next();
                        String pvName = pvDA.stringValue("pvName");
                        double refVal = pvDA.doubleValue("referenceValue");
                        double val = 0.;
                        if (pvDA.hasAttribute("value")) {
                            val = pvDA.doubleValue("value");
                        }
                        spvs.addScalarPV(pvName, refVal);
                        ScalarPV spv = spvs.getScalarPV(spvs.getSize() - 1);
                        spv.setValue(val);
                        spv.showValueChart(pvDA.booleanValue("showValueChart"));
                        spv.showRefChart(pvDA.booleanValue("showRefChart"));
                        spv.showDifChart(pvDA.booleanValue("showDifChart"));
                        spv.showDif(pvDA.booleanValue("showDif"));
                        spv.showValue(pvDA.booleanValue("showValue"));
                        spv.showRef(pvDA.booleanValue("showRef"));
                    }
                }
            }
            spvs.readChart(in);
            in.close();
            updatingController.setStop(false);
            viewValuesPanel.updateGraph();
            viewChartsPanel.updateGraph();
        } catch (IOException exception) {
            messageTextLocal.setText(null);
            messageTextLocal.setText("Fatal error. Something wrong with input file. Stop.");
        }
    }

