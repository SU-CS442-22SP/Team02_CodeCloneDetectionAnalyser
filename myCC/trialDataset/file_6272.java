    private void DrawModel(Graphics offg, int obj_underscorenum, boolean object, float h, float s, int vt_underscorenum, int fc_underscorenum) {
        int px[] = new int[3];
        int py[] = new int[3];
        int count = 0;
        int tmp[] = new int[fc_underscorenum];
        double tmp_underscoredepth[] = new double[fc_underscorenum];
        rotate(vt_underscorenum);
        offg.setColor(Color.black);
        for (int i = 0; i < fc_underscorenum; i++) {
            double a1 = fc[i].vt1.x - fc[i].vt0.x;
            double a2 = fc[i].vt1.y - fc[i].vt0.y;
            double a3 = fc[i].vt1.z - fc[i].vt0.z;
            double b1 = fc[i].vt2.x - fc[i].vt1.x;
            double b2 = fc[i].vt2.y - fc[i].vt1.y;
            double b3 = fc[i].vt2.z - fc[i].vt1.z;
            fc[i].nx = a2 * b3 - a3 * b2;
            fc[i].ny = a3 * b1 - a1 * b3;
            fc[i].nz = a1 * b2 - a2 * b1;
            if (fc[i].nz < 0) {
                fc[i].nx = a2 * b3 - a3 * b2;
                fc[i].ny = a3 * b1 - a1 * b3;
                tmp[count] = i;
                tmp_underscoredepth[count] = fc[i].getDepth();
                count++;
            }
        }
        int lim = count - 1;
        do {
            int m = 0;
            for (int n = 0; n <= lim - 1; n++) {
                if (tmp_underscoredepth[n] < tmp_underscoredepth[n + 1]) {
                    double t = tmp_underscoredepth[n];
                    tmp_underscoredepth[n] = tmp_underscoredepth[n + 1];
                    tmp_underscoredepth[n + 1] = t;
                    int ti = tmp[n];
                    tmp[n] = tmp[n + 1];
                    tmp[n + 1] = ti;
                    m = n;
                }
            }
            lim = m;
        } while (lim != 0);
        for (int m = 0; m < count; m++) {
            int i = tmp[m];
            double l = Math.sqrt(fc[i].nx * fc[i].nx + fc[i].ny * fc[i].ny + fc[i].nz * fc[i].nz);
            test(offg, i, l, h, s);
            px[0] = (int) (fc[i].vt0.x * m_underscoreScale + centerp.x);
            py[0] = (int) (-fc[i].vt0.y * m_underscoreScale + centerp.y);
            px[1] = (int) (fc[i].vt1.x * m_underscoreScale + centerp.x);
            py[1] = (int) (-fc[i].vt1.y * m_underscoreScale + centerp.y);
            px[2] = (int) (fc[i].vt2.x * m_underscoreScale + centerp.x);
            py[2] = (int) (-fc[i].vt2.y * m_underscoreScale + centerp.y);
            offg.fillPolygon(px, py, 3);
        }
        if (labelFlag && object) {
            offg.setFont(Fonts.FONT_underscoreREAL);
            offg.drawString(d_underscorecon.getPointerData().getRealObjName(obj_underscorenum), (int) ((fc[0].vt0.x + 10) * m_underscoreScale + centerp.x), (int) (-(fc[0].vt0.y + 10) * m_underscoreScale + centerp.y));
        }
    }

