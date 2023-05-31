    public void init(String[] arguments) {
        if (arguments.length < 1) {
            printHelp();
            return;
        }
        String[] valid_underscoreargs = new String[] { "device*", "d*", "help", "h", "speed#", "s#", "file*", "f*", "gpsd*", "nmea", "n", "garmin", "g", "sirf", "i", "rawdata", "downloadtracks", "downloadwaypoints", "downloadroutes", "deviceinfo", "printposonce", "printpos", "p", "printalt", "printspeed", "printheading", "printsat", "template*", "outfile*", "screenshot*", "printdefaulttemplate", "helptemplate", "nmealogfile*", "l", "uploadtracks", "uploadroutes", "uploadwaypoints", "infile*" };
        CommandArguments args = null;
        try {
            args = new CommandArguments(arguments, valid_underscoreargs);
        } catch (CommandArgumentException cae) {
            System.err.println("Invalid arguments: " + cae.getMessage());
            printHelp();
            return;
        }
        String filename = null;
        String serial_underscoreport_underscorename = null;
        boolean gpsd = false;
        String gpsd_underscorehost = "localhost";
        int gpsd_underscoreport = 2947;
        int serial_underscoreport_underscorespeed = -1;
        GPSDataProcessor gps_underscoredata_underscoreprocessor;
        String nmea_underscorelog_underscorefile = null;
        if (args.isSet("help") || (args.isSet("h"))) {
            printHelp();
            return;
        }
        if (args.isSet("helptemplate")) {
            printHelpTemplate();
        }
        if (args.isSet("printdefaulttemplate")) {
            System.out.println(DEFAULT_underscoreTEMPLATE);
        }
        if (args.isSet("device")) {
            serial_underscoreport_underscorename = (String) args.getValue("device");
        } else if (args.isSet("d")) {
            serial_underscoreport_underscorename = (String) args.getValue("d");
        }
        if (args.isSet("speed")) {
            serial_underscoreport_underscorespeed = ((Integer) args.getValue("speed")).intValue();
        } else if (args.isSet("s")) {
            serial_underscoreport_underscorespeed = ((Integer) args.getValue("s")).intValue();
        }
        if (args.isSet("file")) {
            filename = (String) args.getValue("file");
        } else if (args.isSet("f")) {
            filename = (String) args.getValue("f");
        }
        if (args.isSet("gpsd")) {
            gpsd = true;
            String gpsd_underscorehost_underscoreport = (String) args.getValue("gpsd");
            if (gpsd_underscorehost_underscoreport != null && gpsd_underscorehost_underscoreport.length() > 0) {
                String[] params = gpsd_underscorehost_underscoreport.split(":");
                gpsd_underscorehost = params[0];
                if (params.length > 0) {
                    gpsd_underscoreport = Integer.parseInt(params[1]);
                }
            }
        }
        if (args.isSet("garmin") || args.isSet("g")) {
            gps_underscoredata_underscoreprocessor = new GPSGarminDataProcessor();
            serial_underscoreport_underscorespeed = 9600;
            if (filename != null) {
                System.err.println("ERROR: Cannot read garmin data from file, only serial port supported!");
                return;
            }
        } else if (args.isSet("sirf") || args.isSet("i")) {
            gps_underscoredata_underscoreprocessor = new GPSSirfDataProcessor();
            serial_underscoreport_underscorespeed = 19200;
            if (filename != null) {
                System.err.println("ERROR: Cannot read sirf data from file, only serial port supported!");
                return;
            }
        } else {
            gps_underscoredata_underscoreprocessor = new GPSNmeaDataProcessor();
            serial_underscoreport_underscorespeed = 4800;
        }
        if (args.isSet("nmealogfile") || (args.isSet("l"))) {
            if (args.isSet("nmealogfile")) nmea_underscorelog_underscorefile = args.getStringValue("nmealogfile"); else nmea_underscorelog_underscorefile = args.getStringValue("l");
        }
        if (args.isSet("rawdata")) {
            gps_underscoredata_underscoreprocessor.addGPSRawDataListener(new GPSRawDataListener() {

                public void gpsRawDataReceived(char[] data, int offset, int length) {
                    System.out.println("RAWLOG: " + new String(data, offset, length));
                }
            });
        }
        GPSDevice gps_underscoredevice;
        Hashtable environment = new Hashtable();
        if (filename != null) {
            environment.put(GPSFileDevice.PATH_underscoreNAME_underscoreKEY, filename);
            gps_underscoredevice = new GPSFileDevice();
        } else if (gpsd) {
            environment.put(GPSNetworkGpsdDevice.GPSD_underscoreHOST_underscoreKEY, gpsd_underscorehost);
            environment.put(GPSNetworkGpsdDevice.GPSD_underscorePORT_underscoreKEY, new Integer(gpsd_underscoreport));
            gps_underscoredevice = new GPSNetworkGpsdDevice();
        } else {
            if (serial_underscoreport_underscorename != null) environment.put(GPSSerialDevice.PORT_underscoreNAME_underscoreKEY, serial_underscoreport_underscorename);
            if (serial_underscoreport_underscorespeed > -1) environment.put(GPSSerialDevice.PORT_underscoreSPEED_underscoreKEY, new Integer(serial_underscoreport_underscorespeed));
            gps_underscoredevice = new GPSSerialDevice();
        }
        try {
            gps_underscoredevice.init(environment);
            gps_underscoredata_underscoreprocessor.setGPSDevice(gps_underscoredevice);
            gps_underscoredata_underscoreprocessor.open();
            gps_underscoredata_underscoreprocessor.addProgressListener(this);
            if ((nmea_underscorelog_underscorefile != null) && (nmea_underscorelog_underscorefile.length() > 0)) {
                gps_underscoredata_underscoreprocessor.addGPSRawDataListener(new GPSRawDataFileLogger(nmea_underscorelog_underscorefile));
            }
            if (args.isSet("deviceinfo")) {
                System.out.println("GPSInfo:");
                String[] infos = gps_underscoredata_underscoreprocessor.getGPSInfo();
                for (int index = 0; index < infos.length; index++) {
                    System.out.println(infos[index]);
                }
            }
            if (args.isSet("screenshot")) {
                FileOutputStream out = new FileOutputStream((String) args.getValue("screenshot"));
                BufferedImage image = gps_underscoredata_underscoreprocessor.getScreenShot();
                ImageIO.write(image, "PNG", out);
            }
            boolean print_underscorewaypoints = args.isSet("downloadwaypoints");
            boolean print_underscoreroutes = args.isSet("downloadroutes");
            boolean print_underscoretracks = args.isSet("downloadtracks");
            if (print_underscorewaypoints || print_underscoreroutes || print_underscoretracks) {
                VelocityContext context = new VelocityContext();
                if (print_underscorewaypoints) {
                    List waypoints = gps_underscoredata_underscoreprocessor.getWaypoints();
                    if (waypoints != null) context.put("waypoints", waypoints); else print_underscorewaypoints = false;
                }
                if (print_underscoretracks) {
                    List tracks = gps_underscoredata_underscoreprocessor.getTracks();
                    if (tracks != null) context.put("tracks", tracks); else print_underscoretracks = false;
                }
                if (print_underscoreroutes) {
                    List routes = gps_underscoredata_underscoreprocessor.getRoutes();
                    if (routes != null) context.put("routes", routes); else print_underscoreroutes = false;
                }
                context.put("printwaypoints", new Boolean(print_underscorewaypoints));
                context.put("printtracks", new Boolean(print_underscoretracks));
                context.put("printroutes", new Boolean(print_underscoreroutes));
                Writer writer;
                Reader reader;
                if (args.isSet("template")) {
                    String template_underscorefile = (String) args.getValue("template");
                    reader = new FileReader(template_underscorefile);
                } else {
                    reader = new StringReader(DEFAULT_underscoreTEMPLATE);
                }
                if (args.isSet("outfile")) writer = new FileWriter((String) args.getValue("outfile")); else writer = new OutputStreamWriter(System.out);
                addDefaultValuesToContext(context);
                boolean result = printTemplate(context, reader, writer);
            }
            boolean read_underscorewaypoints = (args.isSet("uploadwaypoints") && args.isSet("infile"));
            boolean read_underscoreroutes = (args.isSet("uploadroutes") && args.isSet("infile"));
            boolean read_underscoretracks = (args.isSet("uploadtracks") && args.isSet("infile"));
            if (read_underscorewaypoints || read_underscoreroutes || read_underscoretracks) {
                ReadGPX reader = new ReadGPX();
                String in_underscorefile = (String) args.getValue("infile");
                reader.parseFile(in_underscorefile);
                if (read_underscorewaypoints) gps_underscoredata_underscoreprocessor.setWaypoints(reader.getWaypoints());
                if (read_underscoreroutes) gps_underscoredata_underscoreprocessor.setRoutes(reader.getRoutes());
                if (read_underscoretracks) gps_underscoredata_underscoreprocessor.setTracks(reader.getTracks());
            }
            if (args.isSet("printposonce")) {
                GPSPosition pos = gps_underscoredata_underscoreprocessor.getGPSPosition();
                System.out.println("Current Position: " + pos);
            }
            if (args.isSet("printpos") || args.isSet("p")) {
                gps_underscoredata_underscoreprocessor.addGPSDataChangeListener(GPSDataProcessor.LOCATION, this);
            }
            if (args.isSet("printalt")) {
                gps_underscoredata_underscoreprocessor.addGPSDataChangeListener(GPSDataProcessor.ALTITUDE, this);
            }
            if (args.isSet("printspeed")) {
                gps_underscoredata_underscoreprocessor.addGPSDataChangeListener(GPSDataProcessor.SPEED, this);
            }
            if (args.isSet("printheading")) {
                gps_underscoredata_underscoreprocessor.addGPSDataChangeListener(GPSDataProcessor.HEADING, this);
            }
            if (args.isSet("printsat")) {
                gps_underscoredata_underscoreprocessor.addGPSDataChangeListener(GPSDataProcessor.NUMBER_underscoreSATELLITES, this);
                gps_underscoredata_underscoreprocessor.addGPSDataChangeListener(GPSDataProcessor.SATELLITE_underscoreINFO, this);
            }
            if (args.isSet("printpos") || args.isSet("p") || args.isSet("printalt") || args.isSet("printsat") || args.isSet("printspeed") || args.isSet("printheading")) {
                gps_underscoredata_underscoreprocessor.startSendPositionPeriodically(1000L);
                try {
                    System.in.read();
                } catch (IOException ignore) {
                }
            }
            gps_underscoredata_underscoreprocessor.close();
        } catch (GPSException e) {
            e.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            System.err.println("ERROR: File not found: " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println("ERROR: I/O Error: " + ioe.getMessage());
        }
    }

