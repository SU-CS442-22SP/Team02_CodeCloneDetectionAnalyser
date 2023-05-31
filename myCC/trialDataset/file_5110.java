    private void sortWhats(String[] labels, int[] whats, String simplifyString) {
        int n = whats.length;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                int i0_underscorepos = simplifyString.indexOf(labels[whats[i]]);
                int i1_underscorepos = simplifyString.indexOf(labels[whats[i + 1]]);
                if (i0_underscorepos > i1_underscorepos) {
                    int temp = whats[i];
                    whats[i] = whats[i + 1];
                    whats[i + 1] = temp;
                    swapped = true;
                }
            }
        } while (swapped);
    }

