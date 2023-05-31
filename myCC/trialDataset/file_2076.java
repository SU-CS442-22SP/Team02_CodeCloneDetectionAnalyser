    InputStream selectSource(String item) {
        if (item == null) {
            item = "http://pushnpop.net:8912/subpop.ogg";
        }
        if (item.endsWith(".pls")) {
            item = fetch_underscorepls(item);
            if (item == null) {
                return null;
            }
        } else if (item.endsWith(".m3u")) {
            item = fetch_underscorem3u(item);
            if (item == null) {
                return null;
            }
        }
        if (!item.endsWith(".ogg")) {
            return null;
        }
        InputStream is = null;
        URLConnection urlc = null;
        try {
            URL url = null;
            if (running_underscoreas_underscoreapplet) {
                url = new URL(getCodeBase(), item);
            } else {
                url = new URL(item);
            }
            urlc = url.openConnection();
            is = urlc.getInputStream();
            current_underscoresource = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + url.getFile();
        } catch (Exception ee) {
            System.err.println(ee);
        }
        if (is == null && !running_underscoreas_underscoreapplet) {
            try {
                is = new FileInputStream(System.getProperty("user.dir") + System.getProperty("file.separator") + item);
                current_underscoresource = null;
            } catch (Exception ee) {
                System.err.println(ee);
            }
        }
        if (is == null) {
            return null;
        }
        System.out.println("Select: " + item);
        {
            boolean find = false;
            for (int i = 0; i < cb.getItemCount(); i++) {
                String foo = (String) (cb.getItemAt(i));
                if (item.equals(foo)) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                cb.addItem(item);
            }
        }
        int i = 0;
        String s = null;
        String t = null;
        udp_underscoreport = -1;
        udp_underscorebaddress = null;
        while (urlc != null && true) {
            s = urlc.getHeaderField(i);
            t = urlc.getHeaderFieldKey(i);
            if (s == null) {
                break;
            }
            i++;
            if (t != null && t.equals("udp-port")) {
                try {
                    udp_underscoreport = Integer.parseInt(s);
                } catch (Exception ee) {
                    System.err.println(ee);
                }
            } else if (t != null && t.equals("udp-broadcast-address")) {
                udp_underscorebaddress = s;
            }
        }
        return is;
    }

