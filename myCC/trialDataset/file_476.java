    @Override
    public void decorate(Object element, IDecoration decoration) {
        if (element != null && element instanceof IProject) {
            InputStream is = null;
            try {
                IProject project = (IProject) element;
                IFile file = project.getFile(Activator.PLUGIN_underscoreCONF);
                if (file.exists()) {
                    URL url = bundle.getEntry("icons/leaf4e_underscoredecorator.gif");
                    is = FileLocator.toFileURL(url).openStream();
                    Image img = new Image(Display.getCurrent(), is);
                    ImageDescriptor id = ImageDescriptor.createFromImage(img);
                    decoration.addOverlay(id, IDecoration.TOP_underscoreLEFT);
                }
            } catch (Exception e) {
                Status status = new Status(IStatus.WARNING, Activator.PLUGIN_underscoreID, "Decorating error", e);
                logger.log(status);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Status status = new Status(IStatus.WARNING, Activator.PLUGIN_underscoreID, "", e);
                        logger.log(status);
                    }
                }
            }
        }
    }

