    private InputStream getInputStream(String item) {
        InputStream is = null;
        URLConnection urlc = null;
        try {
            URL url = new URL(item);
            urlc = url.openConnection();
            is = urlc.getInputStream();
            current_underscoresource = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + url.getFile();
        } catch (Exception ee) {
            System.err.println(ee);
        }
        int i = 0;
        udp_underscoreport = -1;
        udp_underscorebaddress = null;
        while (urlc != null) {
            String s = urlc.getHeaderField(i);
            String t = urlc.getHeaderFieldKey(i);
            if (s == null) {
                break;
            }
            i++;
            if ("udp-port".equals(t)) {
                try {
                    udp_underscoreport = Integer.parseInt(s);
                } catch (Exception e) {
                }
            } else if ("udp-broadcast-address".equals(t)) {
                udp_underscorebaddress = s;
            }
        }
        return is;
    }

