package t;

import com.github.zhuaidadaya.rikaishinikui.handler.times.*;

import java.math.*;

public class Test {
    public static void main(String[] args) {
        new Thread(() -> {
            long nano = TimeUtil.nano();

            //        System.out.println(Solution.isPalindrome(1110111));
            //
            //        int[] nums1 = new int[]{0, 0, 3, 0, 0, 0, 0, 0, 0};
            //        int[] nums2 = new int[]{- 1, 1, 1, 1, 2, 3};

            //        Solution.merge(nums1, 3, nums2, 6);

            System.out.println(String.valueOf(Solution.pow(new BigInteger(String.valueOf(Solution.pow(new BigInteger("160"), 2))), 10)));

            System.out.println((TimeUtil.nano() - nano) / 1000000d + "ms");
        }).start();

        //        ArrayList<Integer> integers = new ArrayList<>();
        //        integers.add(0,1);
        //        integers.add(1,0);
        //        integers.add(2,1);
        //        System.out.println(Arrays.toString(integers.stream().sorted().toList().toArray(new Integer[0])));
    }
}

class Solution {
    public static BigInteger pow(BigInteger source, Integer count) {
        source = source.pow(count);
        return source;
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        if (m == 0) {
            int ref = 0;
            for (int num : nums2) {
                nums1[ref++] = num;
            }
            return;
        }

        if (n == 0) {
            return;
        }

        computing(nums2, nums1, n, m);

        sort(nums1);
    }

    public static void computing(int[] nums, int[] computing, int n, int ref) {
        for (int num : nums) {
            computing[ref++] = num;
            if (-- n == 0) {
                return;
            }
        }
    }

    public static void sort(int[] array) {
        for (int i = (array.length - 1) / 2; i >= 0; i--) {
            toBigRootHeap(array, array.length, i);
        }
        for (int i = array.length - 1; i > 0; i--) {
            swapValue(array, i, 0);
            toBigRootHeap(array, i, 0);
        }
    }

    private static void toBigRootHeap(int[] array, int size, int index) {
        int leftChildIndex = index * 2 + 1;
        int rightChildIndex = index * 2 + 2;
        int maxIndex = index;
        if ((leftChildIndex < size) && (array[leftChildIndex] > array[maxIndex])) {
            maxIndex = leftChildIndex;
        }
        if ((rightChildIndex < size) && (array[rightChildIndex] > array[maxIndex])) {
            maxIndex = rightChildIndex;
        }
        if (maxIndex == index) {
            return;
        }
        swapValue(array, index, maxIndex);
        toBigRootHeap(array, size, maxIndex);
    }

    private static void swapValue(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        int[] ints = new int[14];
        int num;
        int count = 0;
        while (x != 0) {
            num = x % 10;
            x /= 10;
            ints[count] = num;
            count++;
        }

        int left = 0, right = count - 1;

        while (left < right) {
            if (ints[left++] == ints[right--]) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static double myPow(double x, int n) {
        if (n == 0) {
            return 1;
        }
        boolean powAbv = n > 0;
        boolean onceAbv = ! (powAbv && x > 0);
        if (x == 1 || x == - 1) {
            if ((n % 2) == 0) {
                return 1;
            } else {
                return onceAbv ? - 1 : 1;
            }
        }
        double c = x;
        int times = (n < 0) ? - n : n;
        while (-- times > 0) {
            x *= c;
            if (x == Double.POSITIVE_INFINITY) {
                return 0;
            }
        }
        return powAbv ? x : 1 / x;
    }

    public static int[] plusOne(int[] digits) {
        int[] result = new int[digits.length + 2];
        int index = 0, insert = 2;
        while (index < digits.length)
            result[insert++] = digits[index++];
        result[result.length - 1]++;
        int innerIndex = result.length - 1;
        while (result[innerIndex] > 9) {
            result[innerIndex--] = 0;
            result[innerIndex]++;
        }
        if (result[1] > 0) {
            result = new int[result.length - 1];
            result[0] = 1;
            return result;
        }
        index = 2;
        insert = 0;
        int[] cached = new int[result.length - 2];
        while (index < result.length)
            cached[insert++] = result[index++];
        return cached;
    }

    public static int singleNumber(int[] nums) {
        int result = nums[0];
        for (int i = 1; i < nums.length; )
            result ^= nums[i++];
        return result;
    }

    public static int reverse(int x) {
        if (x < 10 && - 10 < x) {
            return x;
        }
        String str = String.valueOf(Math.abs(x));
        if (str.length() > 10) {
            return 0;
        }
        long value = Long.parseLong(new StringBuilder(str).reverse().toString());
        if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
            return 0;
        }
        return (int) (x > - 1 ? value : - value);
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s.length() < 2)
            return s.length();
        int base = 0, ret = 0, end = 0;
        char[] chars = s.toCharArray();
        for (; end < s.length(); end++) {
            for (int start = base; start < end; start++) {
                if (chars[start] == chars[end]) {
                    ret = ret > end - base ? ret : end - base;
                    base = start + 1;
                }
            }
        }
        return Math.max(ret, end - base);
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 1) {
            return strs[0];
        }
        int[] area = new int[256];
        char[] characters = new char[256];
        int index, max = 0;
        String lastStr = "";
        out:
        for (String str : strs) {
            if (str.equals("")) {
                return "";
            }
            if (str.equals(lastStr)) {
                continue;
            }
            max++;
            index = 0;
            for (char c : str.toCharArray()) {
                area[index] = area[index] == 0 ? 1 : characters[index] == c ? area[index] + 1 : - 1;
                characters[index++] = c;
                if (characters[index - 1] != c) {
                    break out;
                }
            }
            lastStr = str;
        }
        StringBuilder builder = new StringBuilder();
        for (index = 0; area[index] == max; ) {
            builder.append(characters[index++]);
        }
        return builder.toString();
    }

    public static int[] twoSum(int[] nums, int target) {
        switch (nums.length) {
            case 2 -> {
                return new int[]{0, 1};
            }
            case 3 -> {
                return nums[0] + nums[1] == target ? new int[]{0, 1} : nums[0] + nums[2] == target ? new int[]{0, 2} : new int[]{1, 2};
            }
        }
        int fromLeft = 0;
        int fromRight = nums.length - 1;
        while (fromLeft < fromRight) {
            int leftIndex = fromLeft + 1;
            int rightIndex = fromRight - 1;
            while (leftIndex <= rightIndex) {
                if (nums[leftIndex] + nums[fromLeft] == target)
                    return new int[]{fromLeft, leftIndex};
                if (nums[rightIndex] + nums[fromRight] == target)
                    return new int[]{rightIndex, fromRight};
                if (nums[leftIndex] + nums[fromRight] == target)
                    return new int[]{leftIndex, fromRight};
                if (nums[rightIndex] + nums[fromLeft] == target)
                    return new int[]{fromLeft, rightIndex};
                if (nums[fromLeft] + nums[fromRight] == target)
                    return new int[]{fromLeft, fromRight};
                if (nums[leftIndex] + nums[rightIndex] == target)
                    return new int[]{leftIndex, rightIndex};
                ++ leftIndex;
                -- rightIndex;
            }

            ++ fromLeft;
            -- fromRight;
        }

        return new int[]{0, 1};
    }

    public static int searchInsert(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= target) {
                return i;
            }
        }
        return nums.length;
    }

    public static int[] runningSum(int[] nums) {
        int[] counted = new int[nums.length];
        int countedIndex = - 1;
        for (int i = 0; i < nums.length; ) {
            counted[i] = counted[Math.max(0, countedIndex++)] + nums[i++];
        }
        return counted;
    }
}

