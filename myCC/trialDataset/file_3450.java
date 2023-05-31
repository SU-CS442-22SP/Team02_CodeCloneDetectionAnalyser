    public void runDynusT(final boolean cleanUp) {
        final String[] exeFiles = new String[] { "DynusT.exe", "DLL_underscoreramp.dll", "Ramp_underscoreMeter_underscoreFixed_underscoreCDLL.dll", "Ramp_underscoreMeter_underscoreFeedback_underscoreCDLL.dll", "Ramp_underscoreMeter_underscoreFeedback_underscoreFDLL.dll", "libifcoremd.dll", "libmmd.dll", "Ramp_underscoreMeter_underscoreFixed_underscoreFDLL.dll", "libiomp5md.dll" };
        final String[] modelFiles = new String[] { "network.dat", "scenario.dat", "control.dat", "ramp.dat", "incident.dat", "movement.dat", "vms.dat", "origin.dat", "destination.dat", "StopCap4Way.dat", "StopCap2Way.dat", "YieldCap.dat", "WorkZone.dat", "GradeLengthPCE.dat", "leftcap.dat", "system.dat", "output_underscoreoption.dat", "bg_underscoredemand_underscoreadjust.dat", "xy.dat", "TrafficFlowModel.dat", "parameter.dat" };
        log.info("Creating iteration-directory...");
        File iterDir = new File(this.tmpDir);
        if (!iterDir.exists()) {
            iterDir.mkdir();
        }
        log.info("Copying application files to iteration-directory...");
        for (String filename : exeFiles) {
            log.info("  Copying " + filename);
            IOUtils.copyFile(new File(this.dynusTDir + "/" + filename), new File(this.tmpDir + "/" + filename));
        }
        log.info("Copying model files to iteration-directory...");
        for (String filename : modelFiles) {
            log.info("  Copying " + filename);
            IOUtils.copyFile(new File(this.modelDir + "/" + filename), new File(this.tmpDir + "/" + filename));
        }
        for (File f : new File(this.modelDir).listFiles()) {
            if (f.getName().toLowerCase(Locale.ROOT).endsWith(".dws")) {
                log.info("  Copying " + f.getName());
                IOUtils.copyFile(f, new File(this.tmpDir + "/" + f.getName()));
            }
        }
        String logfileName = this.tmpDir + "/dynus-t.log";
        String cmd = this.tmpDir + "/DynusT.exe";
        log.info("running command: " + cmd + " in directory " + this.tmpDir);
        int timeout = 7200;
        int exitcode = ExeRunner.run(cmd, logfileName, timeout, this.tmpDir);
        if (exitcode != 0) {
            throw new RuntimeException("There was a problem running Dynus-T. exit code: " + exitcode);
        }
        if (cleanUp) {
            for (String filename : exeFiles) {
                log.info("  Deleting " + filename);
                new File(this.tmpDir + "/" + filename).delete();
            }
        }
    }

