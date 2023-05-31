    public void _underscoregetPlotTypes() {
        boolean gotPlots = false;
        while (!gotPlots) {
            try {
                _underscoremyPlotTypes = new Vector[2];
                _underscoremyPlotTypes[0] = new Vector();
                _underscoremyPlotTypes[1] = new Vector();
                URL dataurl = new URL(getDocumentBase(), plotTypeFile);
                BufferedReader readme = new BufferedReader(new InputStreamReader(new GZIPInputStream(dataurl.openStream())));
                while (true) {
                    String S = readme.readLine();
                    if (S == null) break;
                    StringTokenizer st = new StringTokenizer(S);
                    _underscoremyPlotTypes[0].addElement(st.nextToken());
                    if (st.hasMoreTokens()) {
                        _underscoremyPlotTypes[1].addElement(st.nextToken());
                    } else {
                        _underscoremyPlotTypes[1].addElement((String) _underscoremyPlotTypes[0].lastElement());
                    }
                }
                gotPlots = true;
            } catch (IOException e) {
                _underscoremyPlotTypes[0].removeAllElements();
                _underscoremyPlotTypes[1].removeAllElements();
                gotPlots = false;
            }
        }
    }

