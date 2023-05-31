    protected void shutdown(final boolean unexpected) {
        ControlerState oldState = this.state;
        this.state = ControlerState.Shutdown;
        if (oldState == ControlerState.Running) {
            if (unexpected) {
                log.warn("S H U T D O W N   ---   received unexpected shutdown request.");
            } else {
                log.info("S H U T D O W N   ---   start regular shutdown.");
            }
            if (this.uncaughtException != null) {
                log.warn("Shutdown probably caused by the following Exception.", this.uncaughtException);
            }
            this.controlerListenerManager.fireControlerShutdownEvent(unexpected);
            if (this.dumpDataAtEnd) {
                Knowledges kk;
                if (this.config.scenario().isUseKnowledges()) {
                    kk = (this.getScenario()).getKnowledges();
                } else {
                    kk = this.getScenario().retrieveNotEnabledKnowledges();
                }
                new PopulationWriter(this.population, this.network, kk).write(this.controlerIO.getOutputFilename(FILENAME_underscorePOPULATION));
                new NetworkWriter(this.network).write(this.controlerIO.getOutputFilename(FILENAME_underscoreNETWORK));
                new ConfigWriter(this.config).write(this.controlerIO.getOutputFilename(FILENAME_underscoreCONFIG));
                ActivityFacilities facilities = this.getFacilities();
                if (facilities != null) {
                    new FacilitiesWriter((ActivityFacilitiesImpl) facilities).write(this.controlerIO.getOutputFilename("output_underscorefacilities.xml.gz"));
                }
                if (((NetworkFactoryImpl) this.network.getFactory()).isTimeVariant()) {
                    new NetworkChangeEventsWriter().write(this.controlerIO.getOutputFilename("output_underscorechange_underscoreevents.xml.gz"), ((NetworkImpl) this.network).getNetworkChangeEvents());
                }
                if (this.config.scenario().isUseHouseholds()) {
                    new HouseholdsWriterV10(this.scenarioData.getHouseholds()).writeFile(this.controlerIO.getOutputFilename(FILENAME_underscoreHOUSEHOLDS));
                }
                if (this.config.scenario().isUseLanes()) {
                    new LaneDefinitionsWriter20(this.scenarioData.getScenarioElement(LaneDefinitions20.class)).write(this.controlerIO.getOutputFilename(FILENAME_underscoreLANES));
                }
                if (!unexpected && this.getConfig().vspExperimental().isWritingOutputEvents()) {
                    File toFile = new File(this.controlerIO.getOutputFilename("output_underscoreevents.xml.gz"));
                    File fromFile = new File(this.controlerIO.getIterationFilename(this.getLastIteration(), "events.xml.gz"));
                    IOUtils.copyFile(fromFile, toFile);
                }
            }
            if (unexpected) {
                log.info("S H U T D O W N   ---   unexpected shutdown request completed.");
            } else {
                log.info("S H U T D O W N   ---   regular shutdown completed.");
            }
            try {
                Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
            } catch (IllegalStateException e) {
                log.info("Cannot remove shutdown hook. " + e.getMessage());
            }
            this.shutdownHook = null;
            this.collectLogMessagesAppender = null;
            IOUtils.closeOutputDirLogging();
        }
    }

