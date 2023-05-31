    private ScrollingGraphicalViewer createGraphicalViewer(final Composite parent) {
        final ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(parent);
        _underscoreroot = new EditRootEditPart();
        viewer.setRootEditPart(_underscoreroot);
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        viewer.setEditPartFactory(getEditPartFactory());
        final KeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer) {

            @SuppressWarnings("unchecked")
            @Override
            public boolean keyPressed(final KeyEvent event) {
                if (event.stateMask == SWT.MOD1 && event.keyCode == SWT.DEL) {
                    final List<? extends EditorPart> objects = viewer.getSelectedEditParts();
                    if (objects == null || objects.isEmpty()) return true;
                    final GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_underscoreDELETE);
                    final CompoundCommand compoundCmd = new CompoundCommand("Delete");
                    for (int i = 0; i < objects.size(); i++) {
                        final EditPart object = (EditPart) objects.get(i);
                        deleteReq.setEditParts(object);
                        final Command cmd = object.getCommand(deleteReq);
                        if (cmd != null) compoundCmd.add(cmd);
                    }
                    getCommandStack().execute(compoundCmd);
                    return true;
                }
                if (event.stateMask == SWT.MOD3 && (event.keyCode == SWT.ARROW_underscoreDOWN || event.keyCode == SWT.ARROW_underscoreLEFT || event.keyCode == SWT.ARROW_underscoreRIGHT || event.keyCode == SWT.ARROW_underscoreUP)) {
                    final List<? extends EditorPart> objects = viewer.getSelectedEditParts();
                    if (objects == null || objects.isEmpty()) return true;
                    final GroupRequest moveReq = new ChangeBoundsRequest(RequestConstants.REQ_underscoreMOVE);
                    final CompoundCommand compoundCmd = new CompoundCommand("Move");
                    for (int i = 0; i < objects.size(); i++) {
                        final EditPart object = (EditPart) objects.get(i);
                        moveReq.setEditParts(object);
                        final LocationCommand cmd = (LocationCommand) object.getCommand(moveReq);
                        if (cmd != null) {
                            cmd.setLocation(new Point(event.keyCode == SWT.ARROW_underscoreLEFT ? -1 : event.keyCode == SWT.ARROW_underscoreRIGHT ? 1 : 0, event.keyCode == SWT.ARROW_underscoreDOWN ? 1 : event.keyCode == SWT.ARROW_underscoreUP ? -1 : 0));
                            cmd.setRelative(true);
                            compoundCmd.add(cmd);
                        }
                    }
                    getCommandStack().execute(compoundCmd);
                    return true;
                }
                return super.keyPressed(event);
            }
        };
        keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_underscoreEDIT));
        viewer.setKeyHandler(keyHandler);
        viewer.setContents(getEditorInput().getAdapter(NamedUuidEntity.class));
        viewer.addDropTargetListener(createTransferDropTargetListener(viewer));
        return viewer;
    }

