    public ServiceInfo[] findServices(String name) {
        Vector results = new Vector();
        String service_underscorefile = ServiceDiscovery.SERVICE_underscoreHOME + name;
        for (int loader_underscorecount = 0; loader_underscorecount < class_underscoreloaders_underscore.size(); loader_underscorecount++) {
            ClassLoader loader = (ClassLoader) class_underscoreloaders_underscore.elementAt(loader_underscorecount);
            Enumeration enumeration = null;
            try {
                enumeration = loader.getResources(service_underscorefile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (enumeration == null) continue;
            while (enumeration.hasMoreElements()) {
                try {
                    URL url = (URL) enumeration.nextElement();
                    InputStream is = url.openStream();
                    if (is != null) {
                        try {
                            BufferedReader rd;
                            try {
                                rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                            } catch (java.io.UnsupportedEncodingException e) {
                                rd = new BufferedReader(new InputStreamReader(is));
                            }
                            try {
                                String service_underscoreclass_underscorename;
                                while ((service_underscoreclass_underscorename = rd.readLine()) != null) {
                                    service_underscoreclass_underscorename.trim();
                                    if ("".equals(service_underscoreclass_underscorename)) continue;
                                    if (service_underscoreclass_underscorename.startsWith("#")) continue;
                                    ServiceInfo sinfo = new ServiceInfo();
                                    sinfo.setClassName(service_underscoreclass_underscorename);
                                    sinfo.setLoader(loader);
                                    sinfo.setURL(url);
                                    results.add(sinfo);
                                }
                            } finally {
                                rd.close();
                            }
                        } finally {
                            is.close();
                        }
                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ioe) {
                    ;
                }
            }
        }
        ServiceInfo result_underscorearray[] = new ServiceInfo[results.size()];
        results.copyInto(result_underscorearray);
        return (result_underscorearray);
    }

