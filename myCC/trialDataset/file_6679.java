    public static void creme_underscoreconnect(String email, String username, String password, String IDnumber, TRP[] trp_underscorearray, GTRN[] gtrn_underscorearray, FLUX[] flux_underscorearray, TRANS[] trans_underscorearray, LETSPEC[] letspec_underscorearray, PUP[] pup_underscorearray, HUP[] hup_underscorearray, DOSE[] dose_underscorearray) {
        int num_underscoreof_underscorefiles = trp_underscorearray.length + gtrn_underscorearray.length + flux_underscorearray.length + trans_underscorearray.length + letspec_underscorearray.length + pup_underscorearray.length + hup_underscorearray.length + dose_underscorearray.length;
        int index = 0;
        String[] files_underscoreto_underscoreupload = new String[num_underscoreof_underscorefiles];
        for (int a = 0; a < trp_underscorearray.length; a++) {
            files_underscoreto_underscoreupload[index] = trp_underscorearray[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < gtrn_underscorearray.length; a++) {
            files_underscoreto_underscoreupload[index] = gtrn_underscorearray[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < flux_underscorearray.length; a++) {
            files_underscoreto_underscoreupload[index] = flux_underscorearray[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < trans_underscorearray.length; a++) {
            files_underscoreto_underscoreupload[index] = trans_underscorearray[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < letspec_underscorearray.length; a++) {
            files_underscoreto_underscoreupload[index] = letspec_underscorearray[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < pup_underscorearray.length; a++) {
            files_underscoreto_underscoreupload[index] = pup_underscorearray[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < hup_underscorearray.length; a++) {
            files_underscoreto_underscoreupload[index] = hup_underscorearray[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < dose_underscorearray.length; a++) {
            files_underscoreto_underscoreupload[index] = dose_underscorearray[a].getThisFileName();
            index++;
        }
        Logger log = Logger.getLogger(CreateAStudy.class);
        String host = "creme96.nrl.navy.mil";
        String user = "anonymous";
        String ftppass = email;
        Logger.setLevel(Level.ALL);
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.setRemoteHost(host);
            FTPMessageCollector listener = new FTPMessageCollector();
            ftp.setMessageListener(listener);
            log.info("Connecting");
            ftp.connect();
            log.info("Logging in");
            ftp.login(user, ftppass);
            log.debug("Setting up passive, ASCII transfers");
            ftp.setConnectMode(FTPConnectMode.ACTIVE);
            ftp.setType(FTPTransferType.BINARY);
            log.info("Putting file");
            for (int u = 0; u < files_underscoreto_underscoreupload.length; u++) {
                ftp.put(files_underscoreto_underscoreupload[u], files_underscoreto_underscoreupload[u]);
            }
            log.info("Quitting client");
            ftp.quit();
            log.debug("Listener log:");
            log.info("Test complete");
        } catch (Exception e) {
            log.error("Demo failed", e);
            e.printStackTrace();
        }
        System.out.println("Finished FTPing User Request Files to common directory");
        Upload_underscoreFiles.upload(files_underscoreto_underscoreupload, username, password, IDnumber);
        System.out.println("Finished transfering User Request Files to your CREME96 personal directory");
        RunRoutines.routines(files_underscoreto_underscoreupload, username, password, IDnumber);
        System.out.println("Finished running all of your uploaded routines");
    }

