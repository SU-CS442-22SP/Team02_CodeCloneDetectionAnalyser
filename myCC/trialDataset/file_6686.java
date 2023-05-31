    public static void main(String argv[]) {
        Matrix A, B, C, Z, O, I, R, S, X, SUB, M, T, SQ, DEF, SOL;
        int errorCount = 0;
        int warningCount = 0;
        double tmp, s;
        double[] columnwise = { 1., 2., 3., 4., 5., 6., 7., 8., 9., 10., 11., 12. };
        double[] rowwise = { 1., 4., 7., 10., 2., 5., 8., 11., 3., 6., 9., 12. };
        double[][] avals = { { 1., 4., 7., 10. }, { 2., 5., 8., 11. }, { 3., 6., 9., 12. } };
        double[][] rankdef = avals;
        double[][] tvals = { { 1., 2., 3. }, { 4., 5., 6. }, { 7., 8., 9. }, { 10., 11., 12. } };
        double[][] subavals = { { 5., 8., 11. }, { 6., 9., 12. } };
        double[][] rvals = { { 1., 4., 7. }, { 2., 5., 8., 11. }, { 3., 6., 9., 12. } };
        double[][] pvals = { { 1., 1., 1. }, { 1., 2., 3. }, { 1., 3., 6. } };
        double[][] ivals = { { 1., 0., 0., 0. }, { 0., 1., 0., 0. }, { 0., 0., 1., 0. } };
        double[][] evals = { { 0., 1., 0., 0. }, { 1., 0., 2.e-7, 0. }, { 0., -2.e-7, 0., 1. }, { 0., 0., 1., 0. } };
        double[][] square = { { 166., 188., 210. }, { 188., 214., 240. }, { 210., 240., 270. } };
        double[][] sqSolution = { { 13. }, { 15. } };
        double[][] condmat = { { 1., 3. }, { 7., 9. } };
        int rows = 3, cols = 4;
        int invalidld = 5;
        int raggedr = 0;
        int raggedc = 4;
        int validld = 3;
        int nonconformld = 4;
        int ib = 1, ie = 2, jb = 1, je = 3;
        int[] rowindexset = { 1, 2 };
        int[] badrowindexset = { 1, 3 };
        int[] columnindexset = { 1, 2, 3 };
        int[] badcolumnindexset = { 1, 2, 4 };
        double columnsummax = 33.;
        double rowsummax = 30.;
        double sumofdiagonals = 15;
        double sumofsquares = 650;
        print("\nTesting constructors and constructor-like methods...\n");
        try {
            A = new Matrix(columnwise, invalidld);
            errorCount = try_underscorefailure(errorCount, "Catch invalid length in packed constructor... ", "exception not thrown for invalid input");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("Catch invalid length in packed constructor... ", e.getMessage());
        }
        try {
            A = new Matrix(rvals);
            tmp = A.get(raggedr, raggedc);
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("Catch ragged input to default constructor... ", e.getMessage());
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            errorCount = try_underscorefailure(errorCount, "Catch ragged input to constructor... ", "exception not thrown in construction...ArrayIndexOutOfBoundsException thrown later");
        }
        try {
            A = Matrix.constructWithCopy(rvals);
            tmp = A.get(raggedr, raggedc);
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("Catch ragged input to constructWithCopy... ", e.getMessage());
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            errorCount = try_underscorefailure(errorCount, "Catch ragged input to constructWithCopy... ", "exception not thrown in construction...ArrayIndexOutOfBoundsException thrown later");
        }
        A = new Matrix(columnwise, validld);
        B = new Matrix(avals);
        tmp = B.get(0, 0);
        avals[0][0] = 0.0;
        C = B.minus(A);
        avals[0][0] = tmp;
        B = Matrix.constructWithCopy(avals);
        tmp = B.get(0, 0);
        avals[0][0] = 0.0;
        if ((tmp - B.get(0, 0)) != 0.0) {
            errorCount = try_underscorefailure(errorCount, "constructWithCopy... ", "copy not effected... data visible outside");
        } else {
            try_underscoresuccess("constructWithCopy... ", "");
        }
        avals[0][0] = columnwise[0];
        I = new Matrix(ivals);
        try {
            check(I, Matrix.identity(3, 4));
            try_underscoresuccess("identity... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "identity... ", "identity Matrix not successfully created");
        }
        print("\nTesting access methods...\n");
        B = new Matrix(avals);
        if (B.getRowDimension() != rows) {
            errorCount = try_underscorefailure(errorCount, "getRowDimension... ", "");
        } else {
            try_underscoresuccess("getRowDimension... ", "");
        }
        if (B.getColumnDimension() != cols) {
            errorCount = try_underscorefailure(errorCount, "getColumnDimension... ", "");
        } else {
            try_underscoresuccess("getColumnDimension... ", "");
        }
        B = new Matrix(avals);
        double[][] barray = B.getArray();
        if (barray != avals) {
            errorCount = try_underscorefailure(errorCount, "getArray... ", "");
        } else {
            try_underscoresuccess("getArray... ", "");
        }
        barray = B.getArrayCopy();
        if (barray == avals) {
            errorCount = try_underscorefailure(errorCount, "getArrayCopy... ", "data not (deep) copied");
        }
        try {
            check(barray, avals);
            try_underscoresuccess("getArrayCopy... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "getArrayCopy... ", "data not successfully (deep) copied");
        }
        double[] bpacked = B.getColumnPackedCopy();
        try {
            check(bpacked, columnwise);
            try_underscoresuccess("getColumnPackedCopy... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "getColumnPackedCopy... ", "data not successfully (deep) copied by columns");
        }
        bpacked = B.getRowPackedCopy();
        try {
            check(bpacked, rowwise);
            try_underscoresuccess("getRowPackedCopy... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "getRowPackedCopy... ", "data not successfully (deep) copied by rows");
        }
        try {
            tmp = B.get(B.getRowDimension(), B.getColumnDimension() - 1);
            errorCount = try_underscorefailure(errorCount, "get(int,int)... ", "OutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                tmp = B.get(B.getRowDimension() - 1, B.getColumnDimension());
                errorCount = try_underscorefailure(errorCount, "get(int,int)... ", "OutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("get(int,int)... OutofBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "get(int,int)... ", "OutOfBoundsException expected but not thrown");
        }
        try {
            if (B.get(B.getRowDimension() - 1, B.getColumnDimension() - 1) != avals[B.getRowDimension() - 1][B.getColumnDimension() - 1]) {
                errorCount = try_underscorefailure(errorCount, "get(int,int)... ", "Matrix entry (i,j) not successfully retreived");
            } else {
                try_underscoresuccess("get(int,int)... ", "");
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            errorCount = try_underscorefailure(errorCount, "get(int,int)... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        SUB = new Matrix(subavals);
        try {
            M = B.getMatrix(ib, ie + B.getRowDimension() + 1, jb, je);
            errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int,int)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                M = B.getMatrix(ib, ie, jb, je + B.getColumnDimension() + 1);
                errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int,int)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("getMatrix(int,int,int,int)... ArrayIndexOutOfBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int,int)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        }
        try {
            M = B.getMatrix(ib, ie, jb, je);
            try {
                check(SUB, M);
                try_underscoresuccess("getMatrix(int,int,int,int)... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int,int)... ", "submatrix not successfully retreived");
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int,int)... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        try {
            M = B.getMatrix(ib, ie, badcolumnindexset);
            errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int[])... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                M = B.getMatrix(ib, ie + B.getRowDimension() + 1, columnindexset);
                errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int[])... ", "ArrayIndexOutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("getMatrix(int,int,int[])... ArrayIndexOutOfBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int[])... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        }
        try {
            M = B.getMatrix(ib, ie, columnindexset);
            try {
                check(SUB, M);
                try_underscoresuccess("getMatrix(int,int,int[])... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int[])... ", "submatrix not successfully retreived");
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            errorCount = try_underscorefailure(errorCount, "getMatrix(int,int,int[])... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        try {
            M = B.getMatrix(badrowindexset, jb, je);
            errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int,int)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                M = B.getMatrix(rowindexset, jb, je + B.getColumnDimension() + 1);
                errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int,int)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("getMatrix(int[],int,int)... ArrayIndexOutOfBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int,int)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        }
        try {
            M = B.getMatrix(rowindexset, jb, je);
            try {
                check(SUB, M);
                try_underscoresuccess("getMatrix(int[],int,int)... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int,int)... ", "submatrix not successfully retreived");
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int,int)... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        try {
            M = B.getMatrix(badrowindexset, columnindexset);
            errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int[])... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                M = B.getMatrix(rowindexset, badcolumnindexset);
                errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int[])... ", "ArrayIndexOutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("getMatrix(int[],int[])... ArrayIndexOutOfBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int[])... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        }
        try {
            M = B.getMatrix(rowindexset, columnindexset);
            try {
                check(SUB, M);
                try_underscoresuccess("getMatrix(int[],int[])... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int[])... ", "submatrix not successfully retreived");
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            errorCount = try_underscorefailure(errorCount, "getMatrix(int[],int[])... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        try {
            B.set(B.getRowDimension(), B.getColumnDimension() - 1, 0.);
            errorCount = try_underscorefailure(errorCount, "set(int,int,double)... ", "OutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                B.set(B.getRowDimension() - 1, B.getColumnDimension(), 0.);
                errorCount = try_underscorefailure(errorCount, "set(int,int,double)... ", "OutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("set(int,int,double)... OutofBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "set(int,int,double)... ", "OutOfBoundsException expected but not thrown");
        }
        try {
            B.set(ib, jb, 0.);
            tmp = B.get(ib, jb);
            try {
                check(tmp, 0.);
                try_underscoresuccess("set(int,int,double)... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "set(int,int,double)... ", "Matrix element not successfully set");
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            errorCount = try_underscorefailure(errorCount, "set(int,int,double)... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        M = new Matrix(2, 3, 0.);
        try {
            B.setMatrix(ib, ie + B.getRowDimension() + 1, jb, je, M);
            errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int,int,Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                B.setMatrix(ib, ie, jb, je + B.getColumnDimension() + 1, M);
                errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int,int,Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("setMatrix(int,int,int,int,Matrix)... ArrayIndexOutOfBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int,int,Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        }
        try {
            B.setMatrix(ib, ie, jb, je, M);
            try {
                check(M.minus(B.getMatrix(ib, ie, jb, je)), M);
                try_underscoresuccess("setMatrix(int,int,int,int,Matrix)... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int,int,Matrix)... ", "submatrix not successfully set");
            }
            B.setMatrix(ib, ie, jb, je, SUB);
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int,int,Matrix)... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        try {
            B.setMatrix(ib, ie + B.getRowDimension() + 1, columnindexset, M);
            errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int[],Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                B.setMatrix(ib, ie, badcolumnindexset, M);
                errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int[],Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("setMatrix(int,int,int[],Matrix)... ArrayIndexOutOfBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int[],Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        }
        try {
            B.setMatrix(ib, ie, columnindexset, M);
            try {
                check(M.minus(B.getMatrix(ib, ie, columnindexset)), M);
                try_underscoresuccess("setMatrix(int,int,int[],Matrix)... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int[],Matrix)... ", "submatrix not successfully set");
            }
            B.setMatrix(ib, ie, jb, je, SUB);
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            errorCount = try_underscorefailure(errorCount, "setMatrix(int,int,int[],Matrix)... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        try {
            B.setMatrix(rowindexset, jb, je + B.getColumnDimension() + 1, M);
            errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int,int,Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                B.setMatrix(badrowindexset, jb, je, M);
                errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int,int,Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("setMatrix(int[],int,int,Matrix)... ArrayIndexOutOfBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int,int,Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        }
        try {
            B.setMatrix(rowindexset, jb, je, M);
            try {
                check(M.minus(B.getMatrix(rowindexset, jb, je)), M);
                try_underscoresuccess("setMatrix(int[],int,int,Matrix)... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int,int,Matrix)... ", "submatrix not successfully set");
            }
            B.setMatrix(ib, ie, jb, je, SUB);
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int,int,Matrix)... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        try {
            B.setMatrix(rowindexset, badcolumnindexset, M);
            errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int[],Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            try {
                B.setMatrix(badrowindexset, columnindexset, M);
                errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int[],Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
            } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
                try_underscoresuccess("setMatrix(int[],int[],Matrix)... ArrayIndexOutOfBoundsException... ", "");
            }
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int[],Matrix)... ", "ArrayIndexOutOfBoundsException expected but not thrown");
        }
        try {
            B.setMatrix(rowindexset, columnindexset, M);
            try {
                check(M.minus(B.getMatrix(rowindexset, columnindexset)), M);
                try_underscoresuccess("setMatrix(int[],int[],Matrix)... ", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int[],Matrix)... ", "submatrix not successfully set");
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
            errorCount = try_underscorefailure(errorCount, "setMatrix(int[],int[],Matrix)... ", "Unexpected ArrayIndexOutOfBoundsException");
        }
        print("\nTesting array-like methods...\n");
        S = new Matrix(columnwise, nonconformld);
        R = Matrix.random(A.getRowDimension(), A.getColumnDimension());
        A = R;
        try {
            S = A.minus(S);
            errorCount = try_underscorefailure(errorCount, "minus conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("minus conformance check... ", "");
        }
        if (A.minus(R).norm1() != 0.) {
            errorCount = try_underscorefailure(errorCount, "minus... ", "(difference of identical Matrices is nonzero,\nSubsequent use of minus should be suspect)");
        } else {
            try_underscoresuccess("minus... ", "");
        }
        A = R.copy();
        A.minusEquals(R);
        Z = new Matrix(A.getRowDimension(), A.getColumnDimension());
        try {
            A.minusEquals(S);
            errorCount = try_underscorefailure(errorCount, "minusEquals conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("minusEquals conformance check... ", "");
        }
        if (A.minus(Z).norm1() != 0.) {
            errorCount = try_underscorefailure(errorCount, "minusEquals... ", "(difference of identical Matrices is nonzero,\nSubsequent use of minus should be suspect)");
        } else {
            try_underscoresuccess("minusEquals... ", "");
        }
        A = R.copy();
        B = Matrix.random(A.getRowDimension(), A.getColumnDimension());
        C = A.minus(B);
        try {
            S = A.plus(S);
            errorCount = try_underscorefailure(errorCount, "plus conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("plus conformance check... ", "");
        }
        try {
            check(C.plus(B), A);
            try_underscoresuccess("plus... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "plus... ", "(C = A - B, but C + B != A)");
        }
        C = A.minus(B);
        C.plusEquals(B);
        try {
            A.plusEquals(S);
            errorCount = try_underscorefailure(errorCount, "plusEquals conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("plusEquals conformance check... ", "");
        }
        try {
            check(C, A);
            try_underscoresuccess("plusEquals... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "plusEquals... ", "(C = A - B, but C = C + B != A)");
        }
        A = R.uminus();
        try {
            check(A.plus(R), Z);
            try_underscoresuccess("uminus... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "uminus... ", "(-A + A != zeros)");
        }
        A = R.copy();
        O = new Matrix(A.getRowDimension(), A.getColumnDimension(), 1.0);
        C = A.arrayLeftDivide(R);
        try {
            S = A.arrayLeftDivide(S);
            errorCount = try_underscorefailure(errorCount, "arrayLeftDivide conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("arrayLeftDivide conformance check... ", "");
        }
        try {
            check(C, O);
            try_underscoresuccess("arrayLeftDivide... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "arrayLeftDivide... ", "(M.\\M != ones)");
        }
        try {
            A.arrayLeftDivideEquals(S);
            errorCount = try_underscorefailure(errorCount, "arrayLeftDivideEquals conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("arrayLeftDivideEquals conformance check... ", "");
        }
        A.arrayLeftDivideEquals(R);
        try {
            check(A, O);
            try_underscoresuccess("arrayLeftDivideEquals... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "arrayLeftDivideEquals... ", "(M.\\M != ones)");
        }
        A = R.copy();
        try {
            A.arrayRightDivide(S);
            errorCount = try_underscorefailure(errorCount, "arrayRightDivide conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("arrayRightDivide conformance check... ", "");
        }
        C = A.arrayRightDivide(R);
        try {
            check(C, O);
            try_underscoresuccess("arrayRightDivide... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "arrayRightDivide... ", "(M./M != ones)");
        }
        try {
            A.arrayRightDivideEquals(S);
            errorCount = try_underscorefailure(errorCount, "arrayRightDivideEquals conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("arrayRightDivideEquals conformance check... ", "");
        }
        A.arrayRightDivideEquals(R);
        try {
            check(A, O);
            try_underscoresuccess("arrayRightDivideEquals... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "arrayRightDivideEquals... ", "(M./M != ones)");
        }
        A = R.copy();
        B = Matrix.random(A.getRowDimension(), A.getColumnDimension());
        try {
            S = A.arrayTimes(S);
            errorCount = try_underscorefailure(errorCount, "arrayTimes conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("arrayTimes conformance check... ", "");
        }
        C = A.arrayTimes(B);
        try {
            check(C.arrayRightDivideEquals(B), A);
            try_underscoresuccess("arrayTimes... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "arrayTimes... ", "(A = R, C = A.*B, but C./B != A)");
        }
        try {
            A.arrayTimesEquals(S);
            errorCount = try_underscorefailure(errorCount, "arrayTimesEquals conformance check... ", "nonconformance not raised");
        } catch (IllegalArgumentException e) {
            try_underscoresuccess("arrayTimesEquals conformance check... ", "");
        }
        A.arrayTimesEquals(B);
        try {
            check(A.arrayRightDivideEquals(B), R);
            try_underscoresuccess("arrayTimesEquals... ", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "arrayTimesEquals... ", "(A = R, A = A.*B, but A./B != R)");
        }
        print("\nTesting I/O methods...\n");
        try {
            DecimalFormat fmt = new DecimalFormat("0.0000E00");
            fmt.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
            PrintWriter FILE = new PrintWriter(new FileOutputStream("JamaTestMatrix.out"));
            A.print(FILE, fmt, 10);
            FILE.close();
            R = Matrix.read(new BufferedReader(new FileReader("JamaTestMatrix.out")));
            if (A.minus(R).norm1() < .001) {
                try_underscoresuccess("print()/read()...", "");
            } else {
                errorCount = try_underscorefailure(errorCount, "print()/read()...", "Matrix read from file does not match Matrix printed to file");
            }
        } catch (java.io.IOException ioe) {
            warningCount = try_underscorewarning(warningCount, "print()/read()...", "unexpected I/O error, unable to run print/read test;  check write permission in current directory and retry");
        } catch (Exception e) {
            try {
                e.printStackTrace(System.out);
                warningCount = try_underscorewarning(warningCount, "print()/read()...", "Formatting error... will try JDK1.1 reformulation...");
                DecimalFormat fmt = new DecimalFormat("0.0000");
                PrintWriter FILE = new PrintWriter(new FileOutputStream("JamaTestMatrix.out"));
                A.print(FILE, fmt, 10);
                FILE.close();
                R = Matrix.read(new BufferedReader(new FileReader("JamaTestMatrix.out")));
                if (A.minus(R).norm1() < .001) {
                    try_underscoresuccess("print()/read()...", "");
                } else {
                    errorCount = try_underscorefailure(errorCount, "print()/read() (2nd attempt) ...", "Matrix read from file does not match Matrix printed to file");
                }
            } catch (java.io.IOException ioe) {
                warningCount = try_underscorewarning(warningCount, "print()/read()...", "unexpected I/O error, unable to run print/read test;  check write permission in current directory and retry");
            }
        }
        R = Matrix.random(A.getRowDimension(), A.getColumnDimension());
        String tmpname = "TMPMATRIX.serial";
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tmpname));
            out.writeObject(R);
            ObjectInputStream sin = new ObjectInputStream(new FileInputStream(tmpname));
            A = (Matrix) sin.readObject();
            try {
                check(A, R);
                try_underscoresuccess("writeObject(Matrix)/readObject(Matrix)...", "");
            } catch (java.lang.RuntimeException e) {
                errorCount = try_underscorefailure(errorCount, "writeObject(Matrix)/readObject(Matrix)...", "Matrix not serialized correctly");
            }
        } catch (java.io.IOException ioe) {
            warningCount = try_underscorewarning(warningCount, "writeObject()/readObject()...", "unexpected I/O error, unable to run serialization test;  check write permission in current directory and retry");
        } catch (Exception e) {
            errorCount = try_underscorefailure(errorCount, "writeObject(Matrix)/readObject(Matrix)...", "unexpected error in serialization test");
        }
        print("\nTesting linear algebra methods...\n");
        A = new Matrix(columnwise, 3);
        T = new Matrix(tvals);
        T = A.transpose();
        try {
            check(A.transpose(), T);
            try_underscoresuccess("transpose...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "transpose()...", "transpose unsuccessful");
        }
        A.transpose();
        try {
            check(A.norm1(), columnsummax);
            try_underscoresuccess("norm1...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "norm1()...", "incorrect norm calculation");
        }
        try {
            check(A.normInf(), rowsummax);
            try_underscoresuccess("normInf()...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "normInf()...", "incorrect norm calculation");
        }
        try {
            check(A.normF(), Math.sqrt(sumofsquares));
            try_underscoresuccess("normF...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "normF()...", "incorrect norm calculation");
        }
        try {
            check(A.trace(), sumofdiagonals);
            try_underscoresuccess("trace()...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "trace()...", "incorrect trace calculation");
        }
        try {
            check(A.getMatrix(0, A.getRowDimension() - 1, 0, A.getRowDimension() - 1).det(), 0.);
            try_underscoresuccess("det()...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "det()...", "incorrect determinant calculation");
        }
        SQ = new Matrix(square);
        try {
            check(A.times(A.transpose()), SQ);
            try_underscoresuccess("times(Matrix)...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "times(Matrix)...", "incorrect Matrix-Matrix product calculation");
        }
        try {
            check(A.times(0.), Z);
            try_underscoresuccess("times(double)...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "times(double)...", "incorrect Matrix-scalar product calculation");
        }
        A = new Matrix(columnwise, 4);
        QRDecomposition QR = A.qr();
        R = QR.getR();
        try {
            check(A, QR.getQ().times(R));
            try_underscoresuccess("QRDecomposition...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "QRDecomposition...", "incorrect QR decomposition calculation");
        }
        SingularValueDecomposition SVD = A.svd();
        try {
            check(A, SVD.getU().times(SVD.getS().times(SVD.getV().transpose())));
            try_underscoresuccess("SingularValueDecomposition...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "SingularValueDecomposition...", "incorrect singular value decomposition calculation");
        }
        DEF = new Matrix(rankdef);
        try {
            check(DEF.rank(), Math.min(DEF.getRowDimension(), DEF.getColumnDimension()) - 1);
            try_underscoresuccess("rank()...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "rank()...", "incorrect rank calculation");
        }
        B = new Matrix(condmat);
        SVD = B.svd();
        double[] singularvalues = SVD.getSingularValues();
        try {
            check(B.cond(), singularvalues[0] / singularvalues[Math.min(B.getRowDimension(), B.getColumnDimension()) - 1]);
            try_underscoresuccess("cond()...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "cond()...", "incorrect condition number calculation");
        }
        int n = A.getColumnDimension();
        A = A.getMatrix(0, n - 1, 0, n - 1);
        A.set(0, 0, 0.);
        LUDecomposition LU = A.lu();
        try {
            check(A.getMatrix(LU.getPivot(), 0, n - 1), LU.getL().times(LU.getU()));
            try_underscoresuccess("LUDecomposition...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "LUDecomposition...", "incorrect LU decomposition calculation");
        }
        X = A.inverse();
        try {
            check(A.times(X), Matrix.identity(3, 3));
            try_underscoresuccess("inverse()...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "inverse()...", "incorrect inverse calculation");
        }
        O = new Matrix(SUB.getRowDimension(), 1, 1.0);
        SOL = new Matrix(sqSolution);
        SQ = SUB.getMatrix(0, SUB.getRowDimension() - 1, 0, SUB.getRowDimension() - 1);
        try {
            check(SQ.solve(SOL), O);
            try_underscoresuccess("solve()...", "");
        } catch (java.lang.IllegalArgumentException e1) {
            errorCount = try_underscorefailure(errorCount, "solve()...", e1.getMessage());
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "solve()...", e.getMessage());
        }
        A = new Matrix(pvals);
        CholeskyDecomposition Chol = A.chol();
        Matrix L = Chol.getL();
        try {
            check(A, L.times(L.transpose()));
            try_underscoresuccess("CholeskyDecomposition...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "CholeskyDecomposition...", "incorrect Cholesky decomposition calculation");
        }
        X = Chol.solve(Matrix.identity(3, 3));
        try {
            check(A.times(X), Matrix.identity(3, 3));
            try_underscoresuccess("CholeskyDecomposition solve()...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "CholeskyDecomposition solve()...", "incorrect Choleskydecomposition solve calculation");
        }
        EigenvalueDecomposition Eig = A.eig();
        Matrix D = Eig.getD();
        Matrix V = Eig.getV();
        try {
            check(A.times(V), V.times(D));
            try_underscoresuccess("EigenvalueDecomposition (symmetric)...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "EigenvalueDecomposition (symmetric)...", "incorrect symmetric Eigenvalue decomposition calculation");
        }
        A = new Matrix(evals);
        Eig = A.eig();
        D = Eig.getD();
        V = Eig.getV();
        try {
            check(A.times(V), V.times(D));
            try_underscoresuccess("EigenvalueDecomposition (nonsymmetric)...", "");
        } catch (java.lang.RuntimeException e) {
            errorCount = try_underscorefailure(errorCount, "EigenvalueDecomposition (nonsymmetric)...", "incorrect nonsymmetric Eigenvalue decomposition calculation");
        }
        print("\nTestMatrix completed.\n");
        print("Total errors reported: " + Integer.toString(errorCount) + "\n");
        print("Total warnings reported: " + Integer.toString(warningCount) + "\n");
    }

