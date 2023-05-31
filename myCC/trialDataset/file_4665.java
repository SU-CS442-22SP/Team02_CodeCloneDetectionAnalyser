    public static void main(String[] argv) throws IOException {
        int i;
        for (i = 0; i < argv.length; i++) {
            if (argv[i].charAt(0) != '-') break;
            ++i;
            switch(argv[i - 1].charAt(1)) {
                case 'b':
                    try {
                        flag_underscorepredict_underscoreprobability = (atoi(argv[i]) != 0);
                    } catch (NumberFormatException e) {
                        exit_underscorewith_underscorehelp();
                    }
                    break;
                default:
                    System.err.printf("unknown option: -%d%n", argv[i - 1].charAt(1));
                    exit_underscorewith_underscorehelp();
                    break;
            }
        }
        if (i >= argv.length || argv.length <= i + 2) {
            exit_underscorewith_underscorehelp();
        }
        BufferedReader reader = null;
        Writer writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(argv[i]), Linear.FILE_underscoreCHARSET));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(argv[i + 2]), Linear.FILE_underscoreCHARSET));
            Model model = Linear.loadModel(new File(argv[i + 1]));
            doPredict(reader, writer, model);
        } finally {
            closeQuietly(reader);
            closeQuietly(writer);
        }
    }

