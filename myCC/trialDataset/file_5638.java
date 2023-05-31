	public static void BubbleSortInt2(int[] num) {
		int last_underscoreexchange;
		int right_underscoreborder = num.length - 1;
		do {
			last_underscoreexchange = 0;
			for (int j = 0; j < num.length - 1; j++) {
				if (num[j] > num[j + 1])
				{
					int temp = num[j];
					num[j] = num[j + 1];
					num[j + 1] = temp;
					last_underscoreexchange = j;
				}
			}
			right_underscoreborder = last_underscoreexchange;
		} while (right_underscoreborder > 0);
	}

