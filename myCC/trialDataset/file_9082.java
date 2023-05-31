    @Override
    public URLConnection getResourceConnection(String name) throws ResourceException {
        try {
            URLConnection c = super.getResourceConnection(name);
            return c;
        } catch (ResourceException e) {
            while (name.startsWith(ServletHelpers.SCRIPT_underscoreDIR.getAbsolutePath())) name = name.substring(ServletHelpers.SCRIPT_underscoreDIR.getAbsolutePath().length());
            name = name.replaceAll("\\\\", "/");
            if (name.startsWith("/")) name = name.substring(1);
            File script = new File(ServletHelpers.SCRIPT_underscoreDIR, name);
            if (script.canRead()) {
                try {
                    URL url = new URL("file", "", script.getAbsolutePath());
                    return url.openConnection();
                } catch (IOException x) {
                    throw new ResourceException("IOError", x);
                }
            } else throw new ResourceException(String.format("Script not found! [%s]", script.getAbsolutePath()));
        }
    }

