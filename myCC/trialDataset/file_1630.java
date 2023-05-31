    public void run_underscoretwo() {
        System.out.println("Waiting to see if server has logged in");
        if (rept != null) {
            rept.post("Checking if RegServer is online..");
        }
        try {
            boolean waiting = true;
            while (waiting) {
                String con = "";
                String s;
                URL url = new URL(where);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                System.out.print("Checking for xRegistry..");
                if (rept != null) {
                    rept.post("Searching for xRegistryServer..");
                }
                while ((s = br.readLine()) != null) {
                    con = con + s;
                }
                err_underscorecatch = con;
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.print("..b_underscorer wouldn't close..");
                }
                servers = xls.split("SERVER", con);
                if (servers.length < 1) {
                    System.out.println("-->Server not available yet.");
                    if (rept != null) {
                        rept.post("-->Server currently unavailable..");
                    }
                    try {
                        Thread.sleep(read_underscoredelay);
                    } catch (Exception inter) {
                    }
                    if (read_underscoredelay < 20000) {
                        read_underscoredelay = read_underscoredelay * 2;
                        if (debug == true) {
                            System.out.println("Set read_underscoredelay to:" + read_underscoredelay);
                        }
                    } else {
                        read_underscoredelay = read_underscoredelay + 3000;
                    }
                    if (read_underscoredelay > 90000) {
                        waiting = false;
                    }
                }
                if (servers.length > 0) {
                    waiting = false;
                }
            }
            String[] regip = xls.split("IP", servers[0]);
            String[] regport = xls.split("PORT", servers[0]);
            int rp = 0;
            try {
                rp = Integer.parseInt(regport[0]);
            } catch (NumberFormatException nfe) {
                rp = 0;
            }
            System.out.println("Trying for socket on " + regip[0] + ", port:" + rp);
            if (rept != null) {
                rept.post("Connecting to RegServer");
            }
            int f = 0;
            Socket client = new Socket(InetAddress.getByName(regip[0]), rp);
            System.out.println("Socket connected to xRegistry");
            if (rept != null) {
                rept.post("Connected to RegServer");
            }
            xcc = new xClientConn(client);
            xcc.set_underscoreIP(ipad);
            xcc.set_underscorePort(port);
            xcc.setListen(this);
            new Thread(xcc).start();
        } catch (Exception e) {
            System.out.println("Failed client connection to registry, 'Java' errorcodes:\n" + e.toString() + "\n\nMessage from server?:\n" + err_underscorecatch);
            if (rept != null) {
                rept.post("Failed at registry connect!");
            }
            if (debug == true) {
                e.printStackTrace();
            }
            term();
        }
    }

