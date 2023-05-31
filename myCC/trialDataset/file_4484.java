    @Override
    protected Control createDialogArea(final Composite parent) {
        this.area = new Composite((Composite) super.createDialogArea(parent), SWT.NONE);
        this.area.setLayout(new FillLayout());
        this.area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.scrolledComposite = new ScrolledComposite(this.area, SWT.V_underscoreSCROLL | SWT.H_underscoreSCROLL);
        this.scrolledComposite.setLayout(new FillLayout());
        this.scrolledComposite.setExpandVertical(true);
        this.scrolledComposite.setExpandHorizontal(true);
        ViewForm vf = new ViewForm(this.scrolledComposite, SWT.BORDER | SWT.FLAT);
        vf.horizontalSpacing = 0;
        vf.verticalSpacing = 0;
        ToolBarManager tbm = new ToolBarManager(SWT.FLAT | SWT.HORIZONTAL | SWT.RIGHT);
        long width = InformationUtil.getChildByType(this.image, ImagePlugin.NODE_underscoreNAME_underscoreWIDTH).getLongValue();
        long height = InformationUtil.getChildByType(this.image, ImagePlugin.NODE_underscoreNAME_underscoreHEIGHT).getLongValue();
        this.graphicalViewer = new ScrollingGraphicalViewer();
        ScalableRootEditPart root = new ScalableRootEditPart();
        this.graphicalViewer.setRootEditPart(root);
        this.graphicalViewer.setEditDomain(new EditDomain());
        this.graphicalViewer.setEditPartFactory(new ImageLinkEditPartFactory(this.editingDomain));
        this.canvas = (FigureCanvas) this.graphicalViewer.createControl(vf);
        this.canvas.getHorizontalBar().setVisible(true);
        this.canvas.getVerticalBar().setVisible(true);
        this.graphicalViewer.setContents(this.image);
        DeleteCommentAction deleteLinkAction = new DeleteCommentAction(this.image);
        deleteLinkAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_underscoreETOOL_underscoreDELETE));
        CreateCommentAction createLinkAction = new CreateCommentAction(this.image);
        createLinkAction.setEditingDomain(this.editingDomain);
        deleteLinkAction.setEditingDomain(this.editingDomain);
        tbm.add(createLinkAction);
        tbm.add(deleteLinkAction);
        this.scrolledComposite.setContent(vf);
        this.graphicalViewer.addSelectionChangedListener(deleteLinkAction);
        vf.setTopLeft(tbm.createControl(vf));
        vf.setContent(this.canvas);
        GridData gd = new GridData(SWT.BEGINNING, SWT.TOP);
        gd.widthHint = (int) width;
        gd.heightHint = (int) height;
        this.canvas.setLayoutData(gd);
        vf.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                CommentImageDialog.this.canvas.redraw();
                super.controlResized(e);
            }
        });
        setTitle(Messages.CommentImageDialog_underscoreTitle);
        setMessage(Messages.CommentImageDialog_underscoreMessage);
        setTitleImage(ResourceManager.getPluginImage(ImagePlugin.getDefault(), "icons/iconexperience/comment_underscorewizard_underscoretitle.png"));
        return this.area;
    }

