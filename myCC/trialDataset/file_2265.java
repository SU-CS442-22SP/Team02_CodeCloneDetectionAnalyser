    public boolean initFile(String filename) {
        showStatus("Loading the file, please wait...");
        x_underscoreunits = "?";
        y_underscoreunits = "ARBITRARY";
        Datatype = "UNKNOWN";
        if (filename.toLowerCase().endsWith(".spc")) {
            try {
                URL url = new URL(getDocumentBase(), filename);
                InputStream stream = url.openStream();
                DataInputStream fichier = new DataInputStream(stream);
                byte ftflgs = fichier.readByte();
                byte fversn = fichier.readByte();
                if (((ftflgs != 0) && (ftflgs != 0x20)) || (fversn != 0x4B)) {
                    Current_underscoreError = ", support only Evenly Spaced new version 4B";
                    return false;
                }
                byte fexp = fichier.readByte();
                if (fexp != 0x80) YFactor = Math.pow(2, fexp) / Math.pow(2, 32);
                Nbpoints = NumericDataUtils.convToIntelInt(fichier.readInt());
                if (Firstx == shitty_underscorestarting_underscoreconstant) {
                    Firstx = NumericDataUtils.convToIntelDouble(fichier.readLong());
                    Lastx = NumericDataUtils.convToIntelDouble(fichier.readLong());
                }
                byte fxtype = fichier.readByte();
                switch(fxtype) {
                    case 0:
                        x_underscoreunits = "Arbitrary";
                        break;
                    case 1:
                        x_underscoreunits = "Wavenumber (cm -1)";
                        break;
                    case 2:
                        x_underscoreunits = "Micrometers";
                        break;
                    case 3:
                        x_underscoreunits = "Nanometers";
                        break;
                    case 4:
                        x_underscoreunits = "Seconds";
                        break;
                    case 5:
                        x_underscoreunits = "Minuts";
                        break;
                    case 6:
                        x_underscoreunits = "Hertz";
                        break;
                    case 7:
                        x_underscoreunits = "Kilohertz";
                        break;
                    case 8:
                        x_underscoreunits = "Megahertz";
                        break;
                    case 9:
                        x_underscoreunits = "Mass (M/z)";
                        break;
                    case 10:
                        x_underscoreunits = "Parts per million";
                        break;
                    case 11:
                        x_underscoreunits = "Days";
                        break;
                    case 12:
                        x_underscoreunits = "Years";
                        break;
                    case 13:
                        x_underscoreunits = "Raman Shift (cm -1)";
                        break;
                    case 14:
                        x_underscoreunits = "Electron Volt (eV)";
                        break;
                    case 16:
                        x_underscoreunits = "Diode Number";
                        break;
                    case 17:
                        x_underscoreunits = "Channel";
                        break;
                    case 18:
                        x_underscoreunits = "Degrees";
                        break;
                    case 19:
                        x_underscoreunits = "Temperature (F)";
                        break;
                    case 20:
                        x_underscoreunits = "Temperature (C)";
                        break;
                    case 21:
                        x_underscoreunits = "Temperature (K)";
                        break;
                    case 22:
                        x_underscoreunits = "Data Points";
                        break;
                    case 23:
                        x_underscoreunits = "Milliseconds (mSec)";
                        break;
                    case 24:
                        x_underscoreunits = "Microseconds (uSec)";
                        break;
                    case 25:
                        x_underscoreunits = "Nanoseconds (nSec)";
                        break;
                    case 26:
                        x_underscoreunits = "Gigahertz (GHz)";
                        break;
                    case 27:
                        x_underscoreunits = "Centimeters (cm)";
                        break;
                    case 28:
                        x_underscoreunits = "Meters (m)";
                        break;
                    case 29:
                        x_underscoreunits = "Millimeters (mm)";
                        break;
                    case 30:
                        x_underscoreunits = "Hours";
                        break;
                    case -1:
                        x_underscoreunits = "(double interferogram)";
                        break;
                }
                byte fytype = fichier.readByte();
                switch(fytype) {
                    case 0:
                        y_underscoreunits = "Arbitrary Intensity";
                        break;
                    case 1:
                        y_underscoreunits = "Interfeogram";
                        break;
                    case 2:
                        y_underscoreunits = "Absorbance";
                        break;
                    case 3:
                        y_underscoreunits = "Kubelka-Munk";
                        break;
                    case 4:
                        y_underscoreunits = "Counts";
                        break;
                    case 5:
                        y_underscoreunits = "Volts";
                        break;
                    case 6:
                        y_underscoreunits = "Degrees";
                        break;
                    case 7:
                        y_underscoreunits = "Milliamps";
                        break;
                    case 8:
                        y_underscoreunits = "Millimeters";
                        break;
                    case 9:
                        y_underscoreunits = "Millivolts";
                        break;
                    case 10:
                        y_underscoreunits = "Log (1/R)";
                        break;
                    case 11:
                        y_underscoreunits = "Percent";
                        break;
                    case 12:
                        y_underscoreunits = "Intensity";
                        break;
                    case 13:
                        y_underscoreunits = "Relative Intensity";
                        break;
                    case 14:
                        y_underscoreunits = "Energy";
                        break;
                    case 16:
                        y_underscoreunits = "Decibel";
                        break;
                    case 19:
                        y_underscoreunits = "Temperature (F)";
                        break;
                    case 20:
                        y_underscoreunits = "Temperature (C)";
                        break;
                    case 21:
                        y_underscoreunits = "Temperature (K)";
                        break;
                    case 22:
                        y_underscoreunits = "Index of Refraction [N]";
                        break;
                    case 23:
                        y_underscoreunits = "Extinction Coeff. [K]";
                        break;
                    case 24:
                        y_underscoreunits = "Real";
                        break;
                    case 25:
                        y_underscoreunits = "Imaginary";
                        break;
                    case 26:
                        y_underscoreunits = "Complex";
                        break;
                    case -128:
                        y_underscoreunits = "Transmission";
                        break;
                    case -127:
                        y_underscoreunits = "Reflectance";
                        break;
                    case -126:
                        y_underscoreunits = "Arbitrary or Single Beam with Valley Peaks";
                        break;
                    case -125:
                        y_underscoreunits = "Emission";
                        break;
                }
                if (ftflgs == 0) {
                    fichier.skipBytes(512 - 30);
                } else {
                    fichier.skipBytes(188);
                    byte b;
                    int i = 0;
                    x_underscoreunits = "";
                    do {
                        b = fichier.readByte();
                        x_underscoreunits += (char) b;
                        i++;
                    } while (b != 0);
                    int j = 0;
                    y_underscoreunits = "";
                    do {
                        b = fichier.readByte();
                        y_underscoreunits += (char) b;
                        j++;
                    } while (b != 0);
                    fichier.skipBytes(512 - 30 - 188 - i - j);
                }
                fichier.skipBytes(32);
                My_underscoreZoneVisu.tableau_underscorepoints = new double[Nbpoints];
                if (fexp == 0x80) {
                    for (int i = 0; i < Nbpoints; i++) {
                        My_underscoreZoneVisu.tableau_underscorepoints[i] = NumericDataUtils.convToIntelFloat(fichier.readInt());
                    }
                } else {
                    for (int i = 0; i < Nbpoints; i++) {
                        My_underscoreZoneVisu.tableau_underscorepoints[i] = NumericDataUtils.convToIntelInt(fichier.readInt());
                    }
                }
            } catch (Exception e) {
                Current_underscoreError = "SPC file corrupted";
                return false;
            }
            Datatype = "XYDATA";
            return true;
        }
        try {
            URL url = new URL(getDocumentBase(), filename);
            InputStream stream = url.openStream();
            BufferedReader fichier = new BufferedReader(new InputStreamReader(stream));
            texte = new Vector();
            String s;
            while ((s = fichier.readLine()) != null) {
                texte.addElement(s);
            }
            nbLignes = texte.size();
        } catch (Exception e) {
            return false;
        }
        int My_underscoreCounter = 0;
        String uneligne = "";
        while (My_underscoreCounter < nbLignes) {
            try {
                StringTokenizer mon_underscoretoken;
                do {
                    uneligne = (String) texte.elementAt(My_underscoreCounter);
                    My_underscoreCounter++;
                    mon_underscoretoken = new StringTokenizer(uneligne, " ");
                } while (My_underscoreCounter < nbLignes && mon_underscoretoken.hasMoreTokens() == false);
                if (mon_underscoretoken.hasMoreTokens() == true) {
                    String keyword = mon_underscoretoken.nextToken();
                    if (StringDataUtils.compareStrings(keyword, "##TITLE=") == 0) TexteTitre = uneligne.substring(9);
                    if (StringDataUtils.compareStrings(keyword, "##FIRSTX=") == 0) Firstx = Double.valueOf(mon_underscoretoken.nextToken()).doubleValue();
                    if (StringDataUtils.compareStrings(keyword, "##LASTX=") == 0) Lastx = Double.valueOf(mon_underscoretoken.nextToken()).doubleValue();
                    if (StringDataUtils.compareStrings(keyword, "##YFACTOR=") == 0) YFactor = Double.valueOf(mon_underscoretoken.nextToken()).doubleValue();
                    if (StringDataUtils.compareStrings(keyword, "##NPOINTS=") == 0) Nbpoints = Integer.valueOf(mon_underscoretoken.nextToken()).intValue();
                    if (StringDataUtils.compareStrings(keyword, "##XUNITS=") == 0) x_underscoreunits = uneligne.substring(10);
                    if (StringDataUtils.compareStrings(keyword, "##YUNITS=") == 0) y_underscoreunits = uneligne.substring(10);
                    if (StringDataUtils.compareStrings(keyword, "##.OBSERVE") == 0 && StringDataUtils.compareStrings(mon_underscoretoken.nextToken(), "FREQUENCY=") == 0) nmr_underscoreobserve_underscorefrequency = Double.valueOf(mon_underscoretoken.nextToken()).doubleValue();
                    if (StringDataUtils.compareStrings(keyword, "##XYDATA=") == 0 && StringDataUtils.compareStrings(mon_underscoretoken.nextToken(), "(X++(Y..Y))") == 0) Datatype = "XYDATA";
                    if (StringDataUtils.compareStrings(keyword, "##XYDATA=(X++(Y..Y))") == 0) Datatype = "XYDATA";
                    if (StringDataUtils.compareStrings(keyword, "##PEAK") == 0 && StringDataUtils.compareStrings(mon_underscoretoken.nextToken(), "TABLE=") == 0 && StringDataUtils.compareStrings(mon_underscoretoken.nextToken(), "(XY..XY)") == 0) Datatype = "PEAK TABLE";
                    if (StringDataUtils.compareStrings(keyword, "##PEAK") == 0 && StringDataUtils.compareStrings(mon_underscoretoken.nextToken(), "TABLE=(XY..XY)") == 0) Datatype = "PEAK TABLE";
                }
            } catch (Exception e) {
            }
        }
        if (Datatype.compareTo("UNKNOWN") == 0) return false;
        if (Datatype.compareTo("PEAK TABLE") == 0 && x_underscoreunits.compareTo("?") == 0) x_underscoreunits = "M/Z";
        if (StringDataUtils.truncateEndBlanks(x_underscoreunits).compareTo("HZ") == 0 && nmr_underscoreobserve_underscorefrequency != shitty_underscorestarting_underscoreconstant) {
            Firstx /= nmr_underscoreobserve_underscorefrequency;
            Lastx /= nmr_underscoreobserve_underscorefrequency;
            x_underscoreunits = "PPM.";
        }
        String resultat_underscoremove_underscorepoints = Move_underscorePoints_underscoreTo_underscoreTableau();
        if (resultat_underscoremove_underscorepoints.compareTo("OK") != 0) {
            Current_underscoreError = resultat_underscoremove_underscorepoints;
            return false;
        }
        return true;
    }

