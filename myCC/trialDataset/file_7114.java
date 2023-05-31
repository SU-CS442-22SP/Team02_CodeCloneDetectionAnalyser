    private static void checkClients() {
        try {
            sendMultiListEntry('l');
        } catch (Exception e) {
            if (Util.getDebugLevel() > 90) e.printStackTrace();
        }
        try {
            if (CANT_underscoreCHECK_underscoreCLIENTS != null) KeyboardHero.removeStatus(CANT_underscoreCHECK_underscoreCLIENTS);
            URL url = new URL(URL_underscoreSTR + "?req=clients" + (server != null ? "&port=" + server.getLocalPort() : ""));
            URLConnection connection = url.openConnection(getProxy());
            connection.setRequestProperty("User-Agent", USER_underscoreAGENT);
            BufferedReader bufferedRdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String ln;
            if (Util.getDebugLevel() > 30) Util.debug("URL: " + url);
            while ((ln = bufferedRdr.readLine()) != null) {
                String[] parts = ln.split(":", 2);
                if (parts.length < 2) {
                    Util.debug(12, "Line read in checkClients: " + ln);
                    continue;
                }
                try {
                    InetSocketAddress address = new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
                    boolean notFound = true;
                    if (Util.getDebugLevel() > 25) Util.debug("NEW Address: " + address.toString());
                    synchronized (clients) {
                        Iterator<Client> iterator = clients.iterator();
                        while (iterator.hasNext()) {
                            final Client client = iterator.next();
                            if (client.socket.isClosed()) {
                                iterator.remove();
                                continue;
                            }
                            if (Util.getDebugLevel() > 26 && client.address != null) Util.debug("Address: " + client.address.toString());
                            if (address.equals(client.address)) {
                                notFound = false;
                                break;
                            }
                        }
                    }
                    if (notFound) {
                        connectClient(address);
                    }
                } catch (NumberFormatException e) {
                }
            }
            bufferedRdr.close();
        } catch (MalformedURLException e) {
            Util.conditionalError(PORT_underscoreIN_underscoreUSE, "Err_underscorePortInUse");
            Util.error(Util.getMsg("Err_underscoreCantCheckClients"));
        } catch (FileNotFoundException e) {
            Util.error(Util.getMsg("Err_underscoreCantCheckClients_underscoreProxy"), Util.getMsg("Err_underscoreFileNotFound"));
        } catch (SocketException e) {
            Util.error(Util.getMsg("Err_underscoreCantCheckClients_underscoreProxy"), e.getLocalizedMessage());
        } catch (Exception e) {
            CANT_underscoreCHECK_underscoreCLIENTS.setException(e.toString());
            KeyboardHero.addStatus(CANT_underscoreCHECK_underscoreCLIENTS);
        }
    }

