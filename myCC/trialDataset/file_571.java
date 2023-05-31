    public void init(VerwaltungClient verClient) {
        this.setLayout(new BorderLayout());
        pnl1 = new JPanel();
        pnl1.setLayout(new FlowLayout());
        pnl1.add(new JLabel(Localization.getInstance().getString("GameOver")));
        pnl1.setBounds(10, 10, 200, 10);
        pnl2 = new JPanel();
        int a_underscorepunkte[];
        int punkte = 0;
        String name;
        String[] a_underscorenamen;
        a_underscorepunkte = verClient.getA_underscorespielEndpunkte();
        a_underscorenamen = verClient.getA_underscoreSpielernamen();
        zeilen = new Object[a_underscorepunkte.length][2];
        for (int i = 0; i < a_underscorepunkte.length; i++) {
            for (int j = 0; j < a_underscorepunkte.length - 1 - i; j++) {
                if (a_underscorepunkte[j] < a_underscorepunkte[j + 1]) {
                    punkte = a_underscorepunkte[j];
                    a_underscorepunkte[j] = a_underscorepunkte[j + 1];
                    a_underscorepunkte[j + 1] = punkte;
                    name = a_underscorenamen[j];
                    a_underscorenamen[j] = a_underscorenamen[j + 1];
                    a_underscorenamen[j + 1] = name;
                }
            }
        }
        for (int i = 0; i < a_underscorepunkte.length; i++) {
            zeilen[i][0] = a_underscorenamen[i];
            zeilen[i][1] = new String("" + a_underscorepunkte[i]);
        }
        tabelle = new JTable(zeilen, spalten);
        tabelle.setEnabled(false);
        pane = new JScrollPane(tabelle);
        pnl2.add(pane);
        pnl3 = new JPanel();
        JButton btn = new JButton(Localization.getInstance().getString("OK"));
        btn.addActionListener(this);
        pnl3.add(btn);
        this.add(BorderLayout.CENTER, pnl2);
        this.add(BorderLayout.NORTH, pnl1);
        this.add(BorderLayout.SOUTH, pnl3);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

