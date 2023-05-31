    private static File createTempWebXml(Class portletClass, File portletDir, String appName, String portletName) throws IOException, FileNotFoundException {
        File pathToWebInf = new File(portletDir, "WEB-INF");
        File tempWebXml = File.createTempFile("web", ".xml", pathToWebInf);
        tempWebXml.deleteOnExit();
        OutputStream webOutputStream = new FileOutputStream(tempWebXml);
        PortletUnitWebXmlStream streamSource = WEB_underscoreXML_underscoreSTREAM_underscoreFACTORY;
        IOUtils.copy(streamSource.createStream(portletClass, appName, portletName), webOutputStream);
        webOutputStream.close();
        return tempWebXml;
    }

