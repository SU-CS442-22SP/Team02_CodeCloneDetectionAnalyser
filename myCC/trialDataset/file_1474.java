    public DefaultMainControl(@NotNull final FileFilter scriptFileFilter, @NotNull final String scriptExtension, @NotNull final String scriptName, final int spellType, @Nullable final String spellFile, @NotNull final String scriptsDir, final ErrorView errorView, @NotNull final EditorFactory<G, A, R> editorFactory, final boolean forceReadFromFiles, @NotNull final GlobalSettings globalSettings, @NotNull final ConfigSourceFactory configSourceFactory, @NotNull final PathManager pathManager, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, @NotNull final AutojoinLists<G, A, R> autojoinLists, @NotNull final AbstractMapManager<G, A, R> mapManager, @NotNull final PluginModel<G, A, R> pluginModel, @NotNull final DelegatingMapValidator<G, A, R> validators, @NotNull final ScriptedEventEditor<G, A, R> scriptedEventEditor, @NotNull final AbstractResources<G, A, R> resources, @NotNull final Spells<NumberSpell> numberSpells, @NotNull final Spells<GameObjectSpell<G, A, R>> gameObjectSpells, @NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory, @NotNull final ValidatorPreferences validatorPreferences, @NotNull final MapWriter<G, A, R> mapWriter) {
        final XmlHelper xmlHelper;
        try {
            xmlHelper = new XmlHelper();
        } catch (final ParserConfigurationException ex) {
            log.fatal("Cannot create XML parser: " + ex.getMessage());
            throw new MissingResourceException("Cannot create XML parser: " + ex.getMessage(), null, null);
        }
        final AttributeRangeChecker<G, A, R> attributeRangeChecker = new AttributeRangeChecker<G, A, R>(validatorPreferences);
        final EnvironmentChecker<G, A, R> environmentChecker = new EnvironmentChecker<G, A, R>(validatorPreferences);
        final DocumentBuilder documentBuilder = xmlHelper.getDocumentBuilder();
        try {
            final URL url = IOUtils.getResource(globalSettings.getConfigurationDirectory(), "GameObjectMatchers.xml");
            final ErrorViewCollector gameObjectMatchersErrorViewCollector = new ErrorViewCollector(errorView, url);
            try {
                documentBuilder.setErrorHandler(new ErrorViewCollectorErrorHandler(gameObjectMatchersErrorViewCollector, ErrorViewCategory.GAMEOBJECTMATCHERS_underscoreFILE_underscoreINVALID));
                try {
                    final GameObjectMatchersParser gameObjectMatchersParser = new GameObjectMatchersParser(documentBuilder, xmlHelper.getXPath());
                    gameObjectMatchersParser.readGameObjectMatchers(url, gameObjectMatchers, gameObjectMatchersErrorViewCollector);
                } finally {
                    documentBuilder.setErrorHandler(null);
                }
            } catch (final IOException ex) {
                gameObjectMatchersErrorViewCollector.addWarning(ErrorViewCategory.GAMEOBJECTMATCHERS_underscoreFILE_underscoreINVALID, ex.getMessage());
            }
            final ValidatorFactory<G, A, R> validatorFactory = new ValidatorFactory<G, A, R>(validatorPreferences, gameObjectMatchers, globalSettings, mapWriter);
            loadValidators(validators, validatorFactory, errorView);
            editorFactory.initMapValidators(validators, gameObjectMatchersErrorViewCollector, globalSettings, gameObjectMatchers, attributeRangeChecker, validatorPreferences);
            validators.addValidator(attributeRangeChecker);
            validators.addValidator(environmentChecker);
        } catch (final FileNotFoundException ex) {
            errorView.addWarning(ErrorViewCategory.GAMEOBJECTMATCHERS_underscoreFILE_underscoreINVALID, "GameObjectMatchers.xml: " + ex.getMessage());
        }
        final GameObjectMatcher shopSquareMatcher = gameObjectMatchers.getMatcher("system_underscoreshop_underscoresquare", "shop_underscoresquare");
        if (shopSquareMatcher != null) {
            final GameObjectMatcher noSpellsMatcher = gameObjectMatchers.getMatcher("system_underscoreno_underscorespells", "no_underscorespells");
            if (noSpellsMatcher != null) {
                final GameObjectMatcher blockedMatcher = gameObjectMatchers.getMatcher("system_underscoreblocked", "blocked");
                validators.addValidator(new ShopSquareChecker<G, A, R>(validatorPreferences, shopSquareMatcher, noSpellsMatcher, blockedMatcher));
            }
            final GameObjectMatcher paidItemMatcher = gameObjectMatchers.getMatcher("system_underscorepaid_underscoreitem");
            if (paidItemMatcher != null) {
                validators.addValidator(new PaidItemShopSquareChecker<G, A, R>(validatorPreferences, shopSquareMatcher, paidItemMatcher));
            }
        }
        Map<String, TreasureTreeNode> specialTreasureLists;
        try {
            final URL url = IOUtils.getResource(globalSettings.getConfigurationDirectory(), "TreasureLists.xml");
            final ErrorViewCollector treasureListsErrorViewCollector = new ErrorViewCollector(errorView, url);
            try {
                final InputStream inputStream = url.openStream();
                try {
                    documentBuilder.setErrorHandler(new ErrorViewCollectorErrorHandler(treasureListsErrorViewCollector, ErrorViewCategory.TREASURES_underscoreFILE_underscoreINVALID));
                    try {
                        final Document specialTreasureListsDocument = documentBuilder.parse(new InputSource(inputStream));
                        specialTreasureLists = TreasureListsParser.parseTreasureLists(specialTreasureListsDocument);
                    } finally {
                        documentBuilder.setErrorHandler(null);
                    }
                } finally {
                    inputStream.close();
                }
            } catch (final IOException ex) {
                treasureListsErrorViewCollector.addWarning(ErrorViewCategory.TREASURES_underscoreFILE_underscoreINVALID, ex.getMessage());
                specialTreasureLists = Collections.emptyMap();
            } catch (final SAXException ex) {
                treasureListsErrorViewCollector.addWarning(ErrorViewCategory.TREASURES_underscoreFILE_underscoreINVALID, ex.getMessage());
                specialTreasureLists = Collections.emptyMap();
            }
        } catch (final FileNotFoundException ex) {
            errorView.addWarning(ErrorViewCategory.TREASURES_underscoreFILE_underscoreINVALID, "TreasureLists.xml: " + ex.getMessage());
            specialTreasureLists = Collections.emptyMap();
        }
        final ConfigSource configSource = forceReadFromFiles ? configSourceFactory.getFilesConfigSource() : configSourceFactory.getConfigSource(globalSettings.getConfigSourceName());
        treasureTree = TreasureLoader.parseTreasures(errorView, specialTreasureLists, configSource, globalSettings);
        final ArchetypeAttributeFactory archetypeAttributeFactory = new DefaultArchetypeAttributeFactory();
        final ArchetypeAttributeParser archetypeAttributeParser = new ArchetypeAttributeParser(archetypeAttributeFactory);
        final ArchetypeTypeParser archetypeTypeParser = new ArchetypeTypeParser(archetypeAttributeParser);
        ArchetypeTypeList eventTypeSet = null;
        try {
            final URL url = IOUtils.getResource(globalSettings.getConfigurationDirectory(), CommonConstants.TYPEDEF_underscoreFILE);
            final ErrorViewCollector typesErrorViewCollector = new ErrorViewCollector(errorView, url);
            documentBuilder.setErrorHandler(new ErrorViewCollectorErrorHandler(typesErrorViewCollector, ErrorViewCategory.GAMEOBJECTMATCHERS_underscoreFILE_underscoreINVALID));
            try {
                final ArchetypeTypeSetParser archetypeTypeSetParser = new ArchetypeTypeSetParser(documentBuilder, archetypeTypeSet, archetypeTypeParser);
                archetypeTypeSetParser.loadTypesFromXML(typesErrorViewCollector, new InputSource(url.toString()));
            } finally {
                documentBuilder.setErrorHandler(null);
            }
            final ArchetypeTypeList eventTypeSetTmp = archetypeTypeSet.getList("event");
            if (eventTypeSetTmp == null) {
                typesErrorViewCollector.addWarning(ErrorViewCategory.TYPES_underscoreENTRY_underscoreINVALID, "list 'list_underscoreevent' does not exist");
            } else {
                eventTypeSet = eventTypeSetTmp;
            }
        } catch (final FileNotFoundException ex) {
            errorView.addWarning(ErrorViewCategory.TYPES_underscoreFILE_underscoreINVALID, CommonConstants.TYPEDEF_underscoreFILE + ": " + ex.getMessage());
        }
        if (eventTypeSet == null) {
            eventTypeSet = new ArchetypeTypeList();
        }
        scriptArchUtils = editorFactory.newScriptArchUtils(eventTypeSet);
        final ScriptedEventFactory<G, A, R> scriptedEventFactory = editorFactory.newScriptedEventFactory(scriptArchUtils, gameObjectFactory, scriptedEventEditor, archetypeSet);
        scriptArchEditor = new DefaultScriptArchEditor<G, A, R>(scriptedEventFactory, scriptExtension, scriptName, scriptArchUtils, scriptFileFilter, globalSettings, mapManager, pathManager);
        scriptedEventEditor.setScriptArchEditor(scriptArchEditor);
        scriptArchData = editorFactory.newScriptArchData();
        scriptArchDataUtils = editorFactory.newScriptArchDataUtils(scriptArchUtils, scriptedEventFactory, scriptedEventEditor);
        final long timeStart = System.currentTimeMillis();
        if (log.isInfoEnabled()) {
            log.info("Start to load archetypes...");
        }
        configSource.read(globalSettings, resources, errorView);
        for (final R archetype : archetypeSet.getArchetypes()) {
            final CharSequence editorFolder = archetype.getEditorFolder();
            if (editorFolder != null && !editorFolder.equals(GameObject.EDITOR_underscoreFOLDER_underscoreINTERN)) {
                final String[] tmp = StringUtils.PATTERN_underscoreSLASH.split(editorFolder, 2);
                if (tmp.length == 2) {
                    final String panelName = tmp[0];
                    final String folderName = tmp[1];
                    archetypeChooserModel.addArchetype(panelName, folderName, archetype);
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Archetype loading took " + (double) (System.currentTimeMillis() - timeStart) / 1000.0 + " seconds.");
        }
        if (spellType != 0) {
            new ArchetypeSetSpellLoader<G, A, R>(gameObjectFactory).load(archetypeSet, spellType, gameObjectSpells);
            gameObjectSpells.sort();
        }
        if (spellFile != null) {
            try {
                final URL url = IOUtils.getResource(globalSettings.getConfigurationDirectory(), spellFile);
                final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, url);
                documentBuilder.setErrorHandler(new ErrorViewCollectorErrorHandler(errorViewCollector, ErrorViewCategory.SPELLS_underscoreFILE_underscoreINVALID));
                try {
                    XMLSpellLoader.load(errorViewCollector, url, xmlHelper.getDocumentBuilder(), numberSpells);
                } finally {
                    documentBuilder.setErrorHandler(null);
                }
            } catch (final FileNotFoundException ex) {
                errorView.addWarning(ErrorViewCategory.SPELLS_underscoreFILE_underscoreINVALID, spellFile + ": " + ex.getMessage());
            }
            numberSpells.sort();
        }
        final File scriptsFile = new File(globalSettings.getMapsDirectory(), scriptsDir);
        final PluginModelParser<G, A, R> pluginModelParser = new PluginModelParser<G, A, R>(pluginParameterFactory);
        new PluginModelLoader<G, A, R>(pluginModelParser).loadPlugins(errorView, scriptsFile, pluginModel);
        new AutojoinListsParser<G, A, R>(errorView, archetypeSet, autojoinLists).loadList(globalSettings.getConfigurationDirectory());
        ArchetypeTypeChecks.addChecks(archetypeTypeSet, attributeRangeChecker, environmentChecker);
    }

