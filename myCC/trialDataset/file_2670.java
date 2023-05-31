    private void loadServers() {
        try {
            URL url = new URL(VirtualDeckConfig.SERVERS_underscoreURL);
            cmbServer.addItem("Local");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            if (in.readLine().equals("[list]")) {
                while ((str = in.readLine()) != null) {
                    String[] host_underscoreline = str.split(";");
                    Host h = new Host();
                    h.setIp(host_underscoreline[0]);
                    h.setPort(Integer.parseInt(host_underscoreline[1]));
                    h.setName(host_underscoreline[2]);
                    getServers().add(h);
                    cmbServer.addItem(h.getName());
                }
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }

