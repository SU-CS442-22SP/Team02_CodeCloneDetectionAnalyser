    protected void createGraphicalViewer(Composite parent) {
        final RulerComposite rc = new RulerComposite(parent, SWT.NONE);
        viewer = new ScrollingGraphicalViewer();
        viewer.createControl(rc);
        editDomain.addViewer(viewer);
        rc.setGraphicalViewer(viewer);
        viewer.getControl().setBackground(ColorConstants.white);
        viewer.setEditPartFactory(new EditPartFactory() {

            public EditPart createEditPart(EditPart context, Object model) {
                return new RecorderEditPart(TopLevelModel.getRecorderModel());
            }
        });
        viewer.setContents(TopLevelModel.getRecorderModel());
        Control control = viewer.getControl();
        System.out.println("widget: " + control);
        DropTarget dt = new DropTarget(control, DND.DROP_underscoreMOVE | DND.DROP_underscoreCOPY | DND.DROP_underscoreDEFAULT);
        dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
        dt.addDropListener(new SensorTransferDropTargetListener(viewer));
    }

