                public void run() {
                    try {
                        exists_underscore = true;
                        URL url = getContentURL();
                        URLConnection cnx = url.openConnection();
                        cnx.connect();
                        lastModified_underscore = cnx.getLastModified();
                        length_underscore = cnx.getContentLength();
                        type_underscore = cnx.getContentType();
                        if (isDirectory()) {
                            InputStream in = cnx.getInputStream();
                            BufferedReader nr = new BufferedReader(new InputStreamReader(in));
                            FuVectorString v = readList(nr);
                            nr.close();
                            v.sort();
                            v.uniq();
                            list_underscore = v.toArray();
                        }
                    } catch (Exception ex) {
                        exists_underscore = false;
                    }
                    done[0] = true;
                }

