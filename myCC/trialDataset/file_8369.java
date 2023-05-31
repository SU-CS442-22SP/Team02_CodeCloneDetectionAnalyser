    public void run() {
        BufferedReader inp = null;
        try {
            String urlString = "http://www.hubtracker.com/query.php?action=add&username=" + user + "&password=" + pass + "&email=" + e_underscoremail + "&address=" + Vars.Hub_underscoreHost;
            URL url = new URL(urlString);
            URLConnection conn;
            if (!Vars.Proxy_underscoreHost.equals("")) conn = url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Vars.Proxy_underscoreHost, Vars.Proxy_underscorePort))); else conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            inp = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String xx;
            while ((xx = inp.readLine()) != null) PluginMain.result += "\n" + xx;
            if (curCmd != null) this.curCmd.cur_underscoreclient.sendFromBot("[hubtracker:] " + PluginMain.result); else PluginMain.curFrame.showMsg();
            inp.close();
            inp = null;
        } catch (MalformedURLException ue) {
            PluginMain.result = ue.toString();
        } catch (Exception e) {
            PluginMain.result = e.toString();
        }
        done = true;
    }

