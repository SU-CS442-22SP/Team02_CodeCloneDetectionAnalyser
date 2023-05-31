    public void readMESHDescriptorFileIntoFiles(String outfiledir) {
        String inputLine, ins;
        String filename = getMESHdescriptorfilename();
        String uid = "";
        String name = "";
        String description = "";
        String element_underscoreof = "";
        Vector treenr = new Vector();
        Vector related = new Vector();
        Vector synonyms = new Vector();
        Vector actions = new Vector();
        Vector chemicals = new Vector();
        Vector allCASchemicals = new Vector();
        Set CAS = new TreeSet();
        Map treenr2uid = new TreeMap();
        Map uid2name = new TreeMap();
        String cut1, cut2;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String outfile = outfiledir + "\\mesh";
            BufferedWriter out_underscoreconcept = new BufferedWriter(new FileWriter(outfile + "_underscoreconcept.txt"));
            BufferedWriter out_underscoreconcept_underscorename = new BufferedWriter(new FileWriter(outfile + "_underscoreconcept_underscorename.txt"));
            BufferedWriter out_underscorerelation = new BufferedWriter(new FileWriter(outfile + "_underscorerelation.txt"));
            BufferedWriter cas_underscoremapping = new BufferedWriter(new FileWriter(outfile + "to_underscorecas_underscoremapping.txt"));
            BufferedWriter ec_underscoremapping = new BufferedWriter(new FileWriter(outfile + "to_underscoreec_underscoremapping.txt"));
            Connection db = tools.openDB("kb");
            String query = "SELECT hierarchy_underscorecomplete,uid FROM mesh_underscoretree, mesh_underscoregraph_underscoreuid_underscorename WHERE term=name";
            ResultSet rs = tools.executeQuery(db, query);
            while (rs.next()) {
                String db_underscoretreenr = rs.getString("hierarchy_underscorecomplete");
                String db_underscoreuid = rs.getString("uid");
                treenr2uid.put(db_underscoretreenr, db_underscoreuid);
            }
            db.close();
            System.out.println("Reading in the DUIDs ...");
            BufferedReader in_underscorefor_underscoremapping = new BufferedReader(new FileReader(filename));
            inputLine = getNextLine(in_underscorefor_underscoremapping);
            boolean leave = false;
            while ((in_underscorefor_underscoremapping != null) && (inputLine != null)) {
                if (inputLine.startsWith("<DescriptorRecord DescriptorClass")) {
                    inputLine = getNextLine(in_underscorefor_underscoremapping);
                    cut1 = "<DescriptorUI>";
                    cut2 = "</DescriptorUI>";
                    String mesh_underscoreuid = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                    if (mesh_underscoreuid.compareTo("D041441") == 0) leave = true;
                    inputLine = getNextLine(in_underscorefor_underscoremapping);
                    inputLine = getNextLine(in_underscorefor_underscoremapping);
                    cut1 = "<String>";
                    cut2 = "</String>";
                    String mesh_underscorename = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                    uid2name.put(mesh_underscoreuid, mesh_underscorename);
                }
                inputLine = getNextLine(in_underscorefor_underscoremapping);
            }
            in_underscorefor_underscoremapping.close();
            BufferedReader in_underscoreec_underscorenumbers = new BufferedReader(new FileReader("e:\\projects\\ondex\\ec_underscoreconcept_underscoreacc.txt"));
            Set ec_underscorenumbers = new TreeSet();
            String ec_underscoreline = in_underscoreec_underscorenumbers.readLine();
            while (in_underscoreec_underscorenumbers.ready()) {
                StringTokenizer st = new StringTokenizer(ec_underscoreline);
                st.nextToken();
                ec_underscorenumbers.add(st.nextToken());
                ec_underscoreline = in_underscoreec_underscorenumbers.readLine();
            }
            in_underscoreec_underscorenumbers.close();
            tools.printDate();
            inputLine = getNextLine(in);
            while (inputLine != null) {
                if (inputLine.startsWith("<DescriptorRecord DescriptorClass")) {
                    treenr.clear();
                    related.clear();
                    synonyms.clear();
                    actions.clear();
                    chemicals.clear();
                    boolean id_underscoreready = false;
                    boolean line_underscoreread = false;
                    while ((inputLine != null) && (!inputLine.startsWith("</DescriptorRecord>"))) {
                        line_underscoreread = false;
                        if ((inputLine.startsWith("<DescriptorUI>")) && (!id_underscoreready)) {
                            cut1 = "<DescriptorUI>";
                            cut2 = "</DescriptorUI>";
                            uid = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                            inputLine = getNextLine(in);
                            inputLine = getNextLine(in);
                            cut1 = "<String>";
                            cut2 = "</String>";
                            name = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                            id_underscoreready = true;
                        }
                        if (inputLine.compareTo("<SeeRelatedList>") == 0) {
                            while ((inputLine != null) && (inputLine.indexOf("</SeeRelatedList>") == -1)) {
                                if (inputLine.startsWith("<DescriptorUI>")) {
                                    cut1 = "<DescriptorUI>";
                                    cut2 = "</DescriptorUI>";
                                    String id = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                                    related.add(id);
                                }
                                inputLine = getNextLine(in);
                                line_underscoreread = true;
                            }
                        }
                        if (inputLine.compareTo("<TreeNumberList>") == 0) {
                            while ((inputLine != null) && (inputLine.indexOf("</TreeNumberList>") == -1)) {
                                if (inputLine.startsWith("<TreeNumber>")) {
                                    cut1 = "<TreeNumber>";
                                    cut2 = "</TreeNumber>";
                                    String id = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                                    treenr.add(id);
                                }
                                inputLine = getNextLine(in);
                                line_underscoreread = true;
                            }
                        }
                        if (inputLine.startsWith("<Concept PreferredConceptYN")) {
                            boolean prefConcept = false;
                            if (inputLine.compareTo("<Concept PreferredConceptYN=\"Y\">") == 0) prefConcept = true;
                            while ((inputLine != null) && (inputLine.indexOf("</Concept>") == -1)) {
                                if (inputLine.startsWith("<CASN1Name>") && prefConcept) {
                                    cut1 = "<CASN1Name>";
                                    cut2 = "</CASN1Name>";
                                    String casn1 = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                                    String chem_underscorename = casn1;
                                    String chem_underscoredescription = "";
                                    if (casn1.length() > chem_underscorename.length() + 2) chem_underscoredescription = casn1.substring(chem_underscorename.length() + 2, casn1.length());
                                    String reg_underscorenumber = "";
                                    inputLine = getNextLine(in);
                                    if (inputLine.startsWith("<RegistryNumber>")) {
                                        cut1 = "<RegistryNumber>";
                                        cut2 = "</RegistryNumber>";
                                        reg_underscorenumber = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                                    }
                                    Vector chemical = new Vector();
                                    String type = "";
                                    if (reg_underscorenumber.startsWith("EC")) {
                                        type = "EC";
                                        reg_underscorenumber = reg_underscorenumber.substring(3, reg_underscorenumber.length());
                                    } else {
                                        type = "CAS";
                                    }
                                    chemical.add(type);
                                    chemical.add(reg_underscorenumber);
                                    chemical.add(chem_underscorename);
                                    chemical.add(chem_underscoredescription);
                                    chemicals.add(chemical);
                                    if (type.compareTo("CAS") == 0) {
                                        if (!CAS.contains(reg_underscorenumber)) {
                                            CAS.add(reg_underscorenumber);
                                            allCASchemicals.add(chemical);
                                        }
                                    }
                                }
                                if (inputLine.startsWith("<ScopeNote>") && prefConcept) {
                                    cut1 = "<ScopeNote>";
                                    description = inputLine.substring(cut1.length(), inputLine.length());
                                }
                                if (inputLine.startsWith("<TermUI>")) {
                                    inputLine = getNextLine(in);
                                    cut1 = "<String>";
                                    cut2 = "</String>";
                                    String syn = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                                    if (syn.indexOf("&amp;") != -1) {
                                        String syn1 = syn.substring(0, syn.indexOf("&amp;"));
                                        String syn2 = syn.substring(syn.indexOf("amp;") + 4, syn.length());
                                        syn = syn1 + " & " + syn2;
                                    }
                                    if (name.compareTo(syn) != 0) synonyms.add(syn);
                                }
                                if (inputLine.startsWith("<PharmacologicalAction>")) {
                                    inputLine = getNextLine(in);
                                    inputLine = getNextLine(in);
                                    cut1 = "<DescriptorUI>";
                                    cut2 = "</DescriptorUI>";
                                    String act_underscoreui = inputLine.substring(cut1.length(), inputLine.indexOf(cut2));
                                    actions.add(act_underscoreui);
                                }
                                inputLine = getNextLine(in);
                                line_underscoreread = true;
                            }
                        }
                        if (!line_underscoreread) inputLine = getNextLine(in);
                    }
                    String pos_underscoretag = "";
                    element_underscoreof = "MESHD";
                    String is_underscoreprimary = "0";
                    out_underscoreconcept.write(uid + "\t" + pos_underscoretag + "\t" + description + "\t" + element_underscoreof + "\t");
                    out_underscoreconcept.write(is_underscoreprimary + "\n");
                    String name_underscorestemmed = "";
                    String name_underscoretagged = "";
                    element_underscoreof = "MESHD";
                    String is_underscoreunique = "0";
                    int is_underscorepreferred = 1;
                    String original_underscorename = name;
                    String is_underscorenot_underscoresubstring = "0";
                    out_underscoreconcept_underscorename.write(uid + "\t" + name + "\t" + name_underscorestemmed + "\t");
                    out_underscoreconcept_underscorename.write(name_underscoretagged + "\t" + element_underscoreof + "\t");
                    out_underscoreconcept_underscorename.write(is_underscoreunique + "\t" + is_underscorepreferred + "\t");
                    out_underscoreconcept_underscorename.write(original_underscorename + "\t" + is_underscorenot_underscoresubstring + "\n");
                    is_underscorepreferred = 0;
                    for (int i = 0; i < synonyms.size(); i++) {
                        name = (String) synonyms.get(i);
                        original_underscorename = name;
                        out_underscoreconcept_underscorename.write(uid + "\t" + name + "\t" + name_underscorestemmed + "\t");
                        out_underscoreconcept_underscorename.write(name_underscoretagged + "\t" + element_underscoreof + "\t");
                        out_underscoreconcept_underscorename.write(is_underscoreunique + "\t" + is_underscorepreferred + "\t");
                        out_underscoreconcept_underscorename.write(original_underscorename + "\t" + is_underscorenot_underscoresubstring + "\n");
                    }
                    String rel_underscoretype = "is_underscorer";
                    element_underscoreof = "MESHD";
                    String from_underscorename = name;
                    for (int i = 0; i < related.size(); i++) {
                        String to_underscoreuid = (String) related.get(i);
                        String to_underscorename = (String) uid2name.get(to_underscoreuid);
                        out_underscorerelation.write(uid + "\t" + to_underscoreuid + "\t");
                        out_underscorerelation.write(rel_underscoretype + "\t" + element_underscoreof + "\t");
                        out_underscorerelation.write(from_underscorename + "\t" + to_underscorename + "\n");
                    }
                    rel_underscoretype = "is_underscorea";
                    element_underscoreof = "MESHD";
                    related.clear();
                    for (int i = 0; i < treenr.size(); i++) {
                        String tnr = (String) treenr.get(i);
                        if (tnr.length() > 3) tnr = tnr.substring(0, tnr.lastIndexOf("."));
                        String rel_underscoreuid = (String) treenr2uid.get(tnr);
                        if (rel_underscoreuid != null) related.add(rel_underscoreuid); else System.out.println(uid + ": No DUI found for " + tnr);
                    }
                    for (int i = 0; i < related.size(); i++) {
                        String to_underscoreuid = (String) related.get(i);
                        String to_underscorename = (String) uid2name.get(to_underscoreuid);
                        out_underscorerelation.write(uid + "\t" + to_underscoreuid + "\t");
                        out_underscorerelation.write(rel_underscoretype + "\t" + element_underscoreof + "\t");
                        out_underscorerelation.write(from_underscorename + "\t" + to_underscorename + "\n");
                    }
                    if (related.size() == 0) System.out.println(uid + ": No is_underscorea relations");
                    rel_underscoretype = "act";
                    element_underscoreof = "MESHD";
                    for (int i = 0; i < actions.size(); i++) {
                        String to_underscoreuid = (String) actions.get(i);
                        String to_underscorename = (String) uid2name.get(to_underscoreuid);
                        out_underscorerelation.write(uid + "\t" + to_underscoreuid + "\t");
                        out_underscorerelation.write(rel_underscoretype + "\t" + element_underscoreof + "\t");
                        out_underscorerelation.write(from_underscorename + "\t" + to_underscorename + "\n");
                    }
                    String method = "IMPM";
                    String score = "1.0";
                    for (int i = 0; i < chemicals.size(); i++) {
                        Vector chemical = (Vector) chemicals.get(i);
                        String type = (String) chemical.get(0);
                        String chem = (String) chemical.get(1);
                        if (!ec_underscorenumbers.contains(chem) && (type.compareTo("EC") == 0)) {
                            if (chem.compareTo("1.14.-") == 0) chem = "1.14.-.-"; else System.out.println("MISSING EC: " + chem);
                        }
                        String id = type + ":" + chem;
                        String entry = uid + "\t" + id + "\t" + method + "\t" + score + "\n";
                        if (type.compareTo("CAS") == 0) cas_underscoremapping.write(entry); else ec_underscoremapping.write(entry);
                    }
                } else inputLine = getNextLine(in);
            }
            System.out.println("End import descriptors");
            tools.printDate();
            in.close();
            out_underscoreconcept.close();
            out_underscoreconcept_underscorename.close();
            out_underscorerelation.close();
            cas_underscoremapping.close();
            ec_underscoremapping.close();
            outfile = outfiledir + "\\cas";
            out_underscoreconcept = new BufferedWriter(new FileWriter(outfile + "_underscoreconcept.txt"));
            out_underscoreconcept_underscorename = new BufferedWriter(new FileWriter(outfile + "_underscoreconcept_underscorename.txt"));
            BufferedWriter out_underscoreconcept_underscoreacc = new BufferedWriter(new FileWriter(outfile + "_underscoreconcept_underscoreacc.txt"));
            for (int i = 0; i < allCASchemicals.size(); i++) {
                Vector chemical = (Vector) allCASchemicals.get(i);
                String cas_underscoreid = "CAS:" + (String) chemical.get(1);
                String cas_underscorename = (String) chemical.get(2);
                String cas_underscorepos_underscoretag = "";
                String cas_underscoredescription = (String) chemical.get(3);
                String cas_underscoreelement_underscoreof = "CAS";
                String cas_underscoreis_underscoreprimary = "0";
                out_underscoreconcept.write(cas_underscoreid + "\t" + cas_underscorepos_underscoretag + "\t" + cas_underscoredescription + "\t");
                out_underscoreconcept.write(cas_underscoreelement_underscoreof + "\t" + cas_underscoreis_underscoreprimary + "\n");
                String cas_underscorename_underscorestemmed = "";
                String cas_underscorename_underscoretagged = "";
                String cas_underscoreis_underscoreunique = "0";
                String cas_underscoreis_underscorepreferred = "0";
                String cas_underscoreoriginal_underscorename = cas_underscorename;
                String cas_underscoreis_underscorenot_underscoresubstring = "0";
                out_underscoreconcept_underscorename.write(cas_underscoreid + "\t" + cas_underscorename + "\t" + cas_underscorename_underscorestemmed + "\t");
                out_underscoreconcept_underscorename.write(cas_underscorename_underscoretagged + "\t" + cas_underscoreelement_underscoreof + "\t");
                out_underscoreconcept_underscorename.write(cas_underscoreis_underscoreunique + "\t" + cas_underscoreis_underscorepreferred + "\t");
                out_underscoreconcept_underscorename.write(cas_underscoreoriginal_underscorename + "\t" + cas_underscoreis_underscorenot_underscoresubstring + "\n");
                out_underscoreconcept_underscoreacc.write(cas_underscoreid + "\t" + (String) chemical.get(1) + "\t");
                out_underscoreconcept_underscoreacc.write(cas_underscoreelement_underscoreof + "\n");
            }
            out_underscoreconcept.close();
            out_underscoreconcept_underscorename.close();
            out_underscoreconcept_underscoreacc.close();
        } catch (Exception e) {
            settings.writeLog("Error while reading MESH descriptor file: " + e.getMessage());
        }
    }

