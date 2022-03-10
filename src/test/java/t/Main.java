package t;

public class Main {
    public static void main(String[] args) {

        int[] mazeX1 = {0, 0, 0};
        int[] mazeX2 = {0, 1, 0};
        int[] mazeX3 = {0, 0, 0};
        int[][] mazeAll = {mazeX1, mazeX2, mazeX3};

        printMaze(mazeAll);
        solveMaze(mazeAll);
    }

    public static void solveMaze(final int[][] mz) {
        //内部类position
        class Position {
            final int[][] map = mz;
            int x = 0;
            int y = 0;

            public Position() {
            }

            public Position(boolean xAdd, boolean yAdd, Position p) {
                this.x = p.x;
                this.y = p.y;
                if (xAdd)
                    this.x = ++p.x;
                if (yAdd)
                    this.y = ++p.y;
            }

            //检测x+1是否为障碍
            public boolean xAddDet() {
                return mz[y - 1][x] == 1;
            }

            public boolean yAddDet() {
                return mz[y][x - 1] == 1;
            }

            //打印当前迷宫
            public void printMap() {
                System.out.println("当前迷宫如下：");
                for (int[] i : map) {
                    for (int j : i) {
                        System.out.print(j + " ");
                    }
                    System.out.println();
                }
            }
        }
        //solveMaze
        int MazeMaxX = mz[0].length;
        int mazeMaxY = mz.length;
        Position p = new Position();

        //x++

        if (p.xAddDet()) {
            //	p.map[p.y][p.x]=2;
            //	p.x++;
            p.printMap();
        }


    }

    public static void printMaze(int[][] mz) {
        System.out.println("迷宫如下：");
        for (int[] i : mz) {
            for (int j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }
}
