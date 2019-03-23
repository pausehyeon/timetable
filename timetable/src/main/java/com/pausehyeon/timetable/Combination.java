package com.pausehyeon.timetable;

public class Combination {
	public static void main(String[] ar) {
		Combination ex = new Combination();
		int[] arr = { 1, 2, 3 };
		int n = arr.length;
		int r = 2;
		int[] combArr = new int[n];

		ex.doCombination(combArr, n, r, 0, 0, arr);
	}

	public void doCombination(int[] combArr, int n, int r, int index, int target, int[] arr) {
//		System.out.println("=> " + n + " " + r + " " + index + " " + target);

		if (r == 0) {
			// 다 뽑은 경우
//			System.out.println(Arrays.toString(combArr));
			for (int i = 0; i < index; i++)
				System.out.print(arr[combArr[i]] + " ");

			System.out.println();
		} else if (target == n) {
			// 끝까지 다 검사한 경우

			return;

		} else {
			combArr[index] = target;
			// (i) 뽑는 경우
			doCombination(combArr, n, r - 1, index + 1, target + 1, arr);

			// (ii) 안 뽑는경우
			doCombination(combArr, n, r, index, target + 1, arr);
		}
	}
}
