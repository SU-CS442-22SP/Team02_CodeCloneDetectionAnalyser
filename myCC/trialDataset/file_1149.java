    public static void get_underscorePK_underscoredata() {
        try {
            FileWriter file_underscorewriter = new FileWriter("xml_underscoredata/PK_underscoredata_underscoredump.xml");
            BufferedWriter file_underscorebuffered_underscorewriter = new BufferedWriter(file_underscorewriter);
            URL fdt = new URL("http://opendata.5t.torino.it/get_underscorepk");
            URLConnection url_underscoreconnection = fdt.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(url_underscoreconnection.getInputStream()));
            String input_underscoreline;
            int num_underscorelines = 0;
            while ((input_underscoreline = in.readLine()) != null) {
                file_underscorebuffered_underscorewriter.write(input_underscoreline + "\n");
                num_underscorelines++;
            }
            System.out.println("Parking :: Writed " + num_underscorelines + " lines.");
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

