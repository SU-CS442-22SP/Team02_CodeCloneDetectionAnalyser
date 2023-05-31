    public void sortIndexes() {
        int i, j, count;
        int t;
        count = m_underscoreItemIndexes.length;
        for (i = 1; i < count; i++) {
            for (j = 0; j < count - i; j++) {
                if (m_underscoreItemIndexes[j] > m_underscoreItemIndexes[j + 1]) {
                    t = m_underscoreItemIndexes[j];
                    m_underscoreItemIndexes[j] = m_underscoreItemIndexes[j + 1];
                    m_underscoreItemIndexes[j + 1] = t;
                }
            }
        }
    }

