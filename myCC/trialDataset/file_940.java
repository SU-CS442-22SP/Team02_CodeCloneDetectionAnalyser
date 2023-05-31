    public Object process(Atom oAtm) throws IOException {
        File oFile;
        FileReader oFileRead;
        String sPathHTML;
        char cBuffer[];
        Object oReplaced;
        final String sSep = System.getProperty("file.separator");
        if (DebugFile.trace) {
            DebugFile.writeln("Begin FileDumper.process([Job:" + getStringNull(DB.gu_underscorejob, "") + ", Atom:" + String.valueOf(oAtm.getInt(DB.pg_underscoreatom)) + "])");
            DebugFile.incIdent();
        }
        if (bHasReplacements) {
            sPathHTML = getProperty("workareasput");
            if (!sPathHTML.endsWith(sSep)) sPathHTML += sSep;
            sPathHTML += getParameter("gu_underscoreworkarea") + sSep + "apps" + sSep + "Mailwire" + sSep + "html" + sSep + getParameter("gu_underscorepageset") + sSep;
            sPathHTML += getParameter("nm_underscorepageset").replace(' ', '_underscore') + ".html";
            if (DebugFile.trace) DebugFile.writeln("PathHTML = " + sPathHTML);
            oReplaced = oReplacer.replace(sPathHTML, oAtm.getItemMap());
            bHasReplacements = (oReplacer.lastReplacements() > 0);
        } else {
            oReplaced = null;
            if (null != oFileStr) oReplaced = oFileStr.get();
            if (null == oReplaced) {
                sPathHTML = getProperty("workareasput");
                if (!sPathHTML.endsWith(sSep)) sPathHTML += sSep;
                sPathHTML += getParameter("gu_underscoreworkarea") + sSep + "apps" + sSep + "Mailwire" + sSep + "html" + sSep + getParameter("gu_underscorepageset") + sSep + getParameter("nm_underscorepageset").replace(' ', '_underscore') + ".html";
                if (DebugFile.trace) DebugFile.writeln("PathHTML = " + sPathHTML);
                oFile = new File(sPathHTML);
                cBuffer = new char[new Long(oFile.length()).intValue()];
                oFileRead = new FileReader(oFile);
                oFileRead.read(cBuffer);
                oFileRead.close();
                if (DebugFile.trace) DebugFile.writeln(String.valueOf(cBuffer.length) + " characters readed");
                oReplaced = new String(cBuffer);
                oFileStr = new SoftReference(oReplaced);
            }
        }
        String sPathJobDir = getProperty("storage");
        if (!sPathJobDir.endsWith(sSep)) sPathJobDir += sSep;
        sPathJobDir += "jobs" + sSep + getParameter("gu_underscoreworkarea") + sSep + getString(DB.gu_underscorejob) + sSep;
        FileWriter oFileWrite = new FileWriter(sPathJobDir + getString(DB.gu_underscorejob) + "_underscore" + String.valueOf(oAtm.getInt(DB.pg_underscoreatom)) + ".html", true);
        oFileWrite.write((String) oReplaced);
        oFileWrite.close();
        iPendingAtoms--;
        if (DebugFile.trace) {
            DebugFile.writeln("End FileDumper.process([Job:" + getStringNull(DB.gu_underscorejob, "") + ", Atom:" + String.valueOf(oAtm.getInt(DB.pg_underscoreatom)) + "])");
            DebugFile.decIdent();
        }
        return oReplaced;
    }

