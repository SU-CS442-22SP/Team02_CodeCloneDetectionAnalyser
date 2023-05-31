    public static void sort(float norm_underscoreabst[]) {
        float temp;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (norm_underscoreabst[j] > norm_underscoreabst[j + 1]) {
                    temp = norm_underscoreabst[j];
                    norm_underscoreabst[j] = norm_underscoreabst[j + 1];
                    norm_underscoreabst[j + 1] = temp;
                }
            }
        }
        printFixed(norm_underscoreabst[0]);
        print(" ");
        printFixed(norm_underscoreabst[1]);
        print(" ");
        printFixed(norm_underscoreabst[2]);
        print(" ");
        printFixed(norm_underscoreabst[3]);
        print(" ");
        printFixed(norm_underscoreabst[4]);
        print(" ");
        printFixed(norm_underscoreabst[5]);
        print(" ");
        printFixed(norm_underscoreabst[6]);
        print(" ");
        printFixed(norm_underscoreabst[7]);
        print("\n");
    }

