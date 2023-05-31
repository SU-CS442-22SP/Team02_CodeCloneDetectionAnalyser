    @Override
    protected void setUp() throws Exception {
        this.logger = new ConsoleLogger(ConsoleLogger.LEVEL_underscoreWARN);
        File repoFolder = new File("target/repository");
        removeRepository(repoFolder);
        InputStream repoConfigIn = getClass().getResourceAsStream(REPO_underscoreCONFIG_underscoreFILE);
        File tempRepoConfigFile = File.createTempFile("repository", "xml");
        tempRepoConfigFile.deleteOnExit();
        OutputStream tempRepoConfigOut = new FileOutputStream(tempRepoConfigFile);
        try {
            IOUtils.copy(repoConfigIn, tempRepoConfigOut);
        } finally {
            repoConfigIn.close();
            tempRepoConfigOut.close();
        }
        Repository repo = new TransientRepository(tempRepoConfigFile.getAbsolutePath(), "target/repository");
        ServerAdapterFactory factory = new ServerAdapterFactory();
        RemoteRepository remoteRepo = factory.getRemoteRepository(repo);
        reg = LocateRegistry.createRegistry(Registry.REGISTRY_underscorePORT);
        reg.rebind(REMOTE_underscoreREPO_underscoreNAME, remoteRepo);
        session = repo.login(new SimpleCredentials(LOGIN, PWD.toCharArray()), WORKSPACE);
        InputStream nodeTypeDefIn = getClass().getResourceAsStream(MQ_underscoreJCR_underscoreXML_underscoreNODETYPES_underscoreFILE);
        JackrabbitInitializerHelper.setupRepository(session, new InputStreamReader(nodeTypeDefIn), "");
    }

