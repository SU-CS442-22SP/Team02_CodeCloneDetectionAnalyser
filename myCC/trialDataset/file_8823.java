    public static void main(String[] argv) throws IOException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        ;
        try {
            session.beginTransaction();
            Properties cfg = new Properties();
            URL url = SVM.class.getClassLoader().getResource(CFG_underscoreFILE);
            cfg.load(url.openStream());
            int runMode = Integer.valueOf(cfg.getProperty(KEY_underscoreRUN_underscoreMODE));
            switch(runMode) {
                case RUN_underscoreOPT:
                    new SVM().optimizeParameters(cfg);
                    break;
                case RUN_underscorePREDICT:
                    new SVM().trainAndPredict(cfg);
                    break;
                case RUN_underscoreOPT_underscoreAND_underscorePREDICT:
                    break;
            }
            session.getTransaction().commit();
        } catch (HibernateException he) {
            session.getTransaction().rollback();
            logger.error("Database error.", he);
            session.close();
        }
    }

