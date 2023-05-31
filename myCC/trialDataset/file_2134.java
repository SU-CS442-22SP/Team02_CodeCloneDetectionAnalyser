    public boolean isValid(WizardContext context) {
        if (serviceSelection < 0) {
            return false;
        }
        ServiceReference selection = (ServiceReference) serviceList.getElementAt(serviceSelection);
        if (selection == null) {
            return false;
        }
        String function = (String) context.getAttribute(ServiceWizard.ATTRIBUTE_underscoreFUNCTION);
        context.setAttribute(ServiceWizard.ATTRIBUTE_underscoreSERVICE_underscoreREFERENCE, selection);
        URL url = selection.getResourceURL();
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            InputSource inputSource = new InputSource(inputStream);
            JdbcService service = ServiceDigester.parseService(inputSource, IsqlToolkit.getSharedEntityResolver());
            context.setAttribute(ServiceWizard.ATTRIBUTE_underscoreSERVICE, service);
            return true;
        } catch (IOException error) {
            if (!ServiceWizard.FUNCTION_underscoreDELETE.equals(function)) {
                String loc = url.toExternalForm();
                String message = messages.format("SelectServiceStep.failed_underscoreto_underscoreload_underscoreservice_underscorefrom_underscoreurl", loc);
                context.showErrorDialog(error, message);
            } else {
                return true;
            }
        } catch (Exception error) {
            String message = messages.format("SelectServiceStep.service_underscoreload_underscoreerror", url.toExternalForm());
            context.showErrorDialog(error, message);
        }
        return false;
    }

