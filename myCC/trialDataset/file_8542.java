    public static void main(String[] args) {
        String email = "josh888@byu.net";
        String username = "josh8573";
        String password = "josh8573";
        String IDnumber = "3030";
        double[] apogee = { 1000 };
        double[] perigee = apogee;
        double[] inclination = { 58.0 };
        int[] trp_underscoresolmax = { 0, 1, 2 };
        double[] init_underscorelong_underscoreascend = { 0 };
        double[] init_underscoredispl_underscoreascend = { 0 };
        double[] displ_underscoreperigee_underscoreascend = { 0 };
        double[] orbit_underscoresect = null;
        boolean[] gtrn_underscoreweather = { false, true };
        boolean print_underscorealtitude = true;
        boolean print_underscoreinclination = false;
        boolean print_underscoregtrn_underscoreweather = true;
        boolean print_underscoreita = false;
        boolean print_underscoreida = false;
        boolean print_underscoredpa = false;
        ORBIT[] orbit_underscorearray;
        orbit_underscorearray = ORBIT.CreateOrbits(apogee, perigee, inclination, gtrn_underscoreweather, trp_underscoresolmax, init_underscorelong_underscoreascend, init_underscoredispl_underscoreascend, displ_underscoreperigee_underscoreascend, orbit_underscoresect, print_underscorealtitude, print_underscoreinclination, print_underscoregtrn_underscoreweather, print_underscoreita, print_underscoreida, print_underscoredpa);
        TRP[] trp_underscorearray = {};
        GTRN[] gtrn_underscorearray = {};
        if (orbit_underscorearray != null) {
            Vector trp_underscorevector = new Vector();
            for (int i = 0; i < orbit_underscorearray.length; i++) {
                TRP temp_underscoret = orbit_underscorearray[i].getTRP();
                if (temp_underscoret != null) {
                    trp_underscorevector.add(temp_underscoret);
                }
            }
            if (trp_underscorevector.size() != 0) {
                TRP[] trp_underscoreto_underscoreconvert = new TRP[trp_underscorevector.size()];
                trp_underscorearray = (TRP[]) trp_underscorevector.toArray(trp_underscoreto_underscoreconvert);
            }
            Vector gtrn_underscorevector = new Vector();
            for (int i = 0; i < orbit_underscorearray.length; i++) {
                GTRN temp_underscoreg = orbit_underscorearray[i].getGTRN();
                if (temp_underscoreg != null) {
                    gtrn_underscorevector.add(temp_underscoreg);
                }
            }
            if (gtrn_underscorevector.size() != 0) {
                GTRN[] gtrn_underscoreto_underscoreconvert = new GTRN[gtrn_underscorevector.size()];
                gtrn_underscorearray = (GTRN[]) gtrn_underscorevector.toArray(gtrn_underscoreto_underscoreconvert);
            }
        }
        int[] flux_underscoremin_underscoreelement = { 1 };
        int[] flux_underscoremax_underscoreelement = { 92 };
        int[] weather_underscoreflux = { 00, 01, 11, 12, 13 };
        boolean print_underscoreweather = true;
        boolean print_underscoremin_underscoreelem = false;
        boolean print_underscoremax_underscoreelem = false;
        ORBIT[] orbit_underscorearray_underscoreinto_underscoreflux = orbit_underscorearray;
        FLUX[] flux_underscorearray;
        flux_underscorearray = FLUX.CreateFLUX_underscoreURF(flux_underscoremin_underscoreelement, flux_underscoremax_underscoreelement, weather_underscoreflux, orbit_underscorearray_underscoreinto_underscoreflux, print_underscoreweather, print_underscoremin_underscoreelem, print_underscoremax_underscoreelem);
        FLUX[] flx_underscoreobjects_underscoreinto_underscoretrans = flux_underscorearray;
        int[] units = { 1 };
        double[] thickness = { 100 };
        boolean print_underscoreshielding = false;
        TRANS[] trans_underscorearray;
        trans_underscorearray = TRANS.CreateTRANS_underscoreURF(flx_underscoreobjects_underscoreinto_underscoretrans, units, thickness, print_underscoreshielding);
        URFInterface[] input_underscorefiles_underscorefor_underscoreletspec = trans_underscorearray;
        int[] letspec_underscoremin_underscoreelement = { 2 };
        int[] letspec_underscoremax_underscoreelement = { 0 };
        double[] min_underscoreenergy_underscorevalue = { .1 };
        boolean[] diff_underscorespect = { false };
        boolean print_underscoremin_underscoreenergy = false;
        LETSPEC[] letspec_underscorearray;
        letspec_underscorearray = LETSPEC.CreateLETSPEC_underscoreURF(input_underscorefiles_underscorefor_underscoreletspec, letspec_underscoremin_underscoreelement, letspec_underscoremax_underscoreelement, min_underscoreenergy_underscorevalue, diff_underscorespect, print_underscoremin_underscoreenergy);
        URFInterface[] input_underscorefiles_underscorefor_underscorepup = trans_underscorearray;
        double[] pup_underscoreparams = { 20, 4, 0.5, .0153 };
        PUP_underscoreDevice[][] pup_underscoredevice_underscorearray = { { new PUP_underscoreDevice("sample", null, null, 50648448, 4, pup_underscoreparams) } };
        boolean print_underscorebits_underscorein_underscoredevice_underscorepup = false;
        boolean print_underscoreweibull_underscoreonset_underscorepup = false;
        boolean print_underscoreweibull_underscorewidth_underscorepup = false;
        boolean print_underscoreweibull_underscoreexponent_underscorepup = false;
        boolean print_underscoreweibull_underscorecross_underscoresect_underscorepup = false;
        PUP[] pup_underscorearray;
        pup_underscorearray = PUP.CreatePUP_underscoreURF(input_underscorefiles_underscorefor_underscorepup, pup_underscoredevice_underscorearray, print_underscorebits_underscorein_underscoredevice_underscorepup, print_underscoreweibull_underscoreonset_underscorepup, print_underscoreweibull_underscorewidth_underscorepup, print_underscoreweibull_underscoreexponent_underscorepup, print_underscoreweibull_underscorecross_underscoresect_underscorepup);
        LETSPEC[] let_underscoreobjects_underscoreinto_underscorehup = letspec_underscorearray;
        double[][] weib_underscoreparams = { { 9.74, 30.25, 2.5, 22600 }, { 9.74, 30.25, 2.5, 2260 }, { 9.74, 30.25, 2.5, 226 }, { 9.74, 30.25, 2.5, 22.6 }, { 9.74, 30.25, 2.5, 2.26 }, { 9.74, 30.25, 2.5, .226 }, { 9.74, 30.25, 2.5, .0226 } };
        HUP_underscoreDevice[][] hup_underscoredevice_underscorearray = new HUP_underscoreDevice[7][1];
        double z_underscoredepth = (float) 0.01;
        for (int i = 0; i < 7; i++) {
            hup_underscoredevice_underscorearray[i][0] = new HUP_underscoreDevice("sample", null, null, 0, 0, (Math.sqrt(weib_underscoreparams[i][3]) / 100), 0, (int) Math.pow(10, i), 4, weib_underscoreparams[i]);
            z_underscoredepth += .01;
        }
        boolean print_underscorelabel = false;
        boolean print_underscorecommenta = false;
        boolean print_underscorecommentb = false;
        boolean print_underscoreRPP_underscorex = false;
        boolean print_underscoreRPP_underscorey = false;
        boolean print_underscoreRPP_underscorez = false;
        boolean print_underscorefunnel_underscorelength = false;
        boolean print_underscorebits_underscorein_underscoredevice_underscorehup = true;
        boolean print_underscoreweibull_underscoreonset_underscorehup = false;
        boolean print_underscoreweibull_underscorewidth_underscorehup = false;
        boolean print_underscoreweibull_underscoreexponent_underscorehup = false;
        boolean print_underscoreweibull_underscorecross_underscoresect_underscorehup = false;
        HUP[] hup_underscorearray;
        hup_underscorearray = HUP.CreateHUP_underscoreURF(let_underscoreobjects_underscoreinto_underscorehup, hup_underscoredevice_underscorearray, print_underscorelabel, print_underscorecommenta, print_underscorecommentb, print_underscoreRPP_underscorex, print_underscoreRPP_underscorey, print_underscoreRPP_underscorez, print_underscorefunnel_underscorelength, print_underscorebits_underscorein_underscoredevice_underscorehup, print_underscoreweibull_underscoreonset_underscorehup, print_underscoreweibull_underscorewidth_underscorehup, print_underscoreweibull_underscoreexponent_underscorehup, print_underscoreweibull_underscorecross_underscoresect_underscorehup);
        System.out.println("Finished creating User Request Files");
        int num_underscoreof_underscorefiles = trp_underscorearray.length + gtrn_underscorearray.length + flux_underscorearray.length + trans_underscorearray.length + letspec_underscorearray.length + pup_underscorearray.length + hup_underscorearray.length;
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

