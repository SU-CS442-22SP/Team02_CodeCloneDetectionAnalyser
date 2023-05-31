    private static void copy(String from_underscorename, String to_underscorename) throws IOException {
        File from_underscorefile = new File(from_underscorename);
        File to_underscorefile = new File(to_underscorename);
        if (!from_underscorefile.exists()) abort("�������� ���� �� ���������" + from_underscorefile);
        if (!from_underscorefile.isFile()) abort("���������� ����������� ��������" + from_underscorefile);
        if (!from_underscorefile.canRead()) abort("�������� ���� ���������� ��� ������" + from_underscorefile);
        if (from_underscorefile.isDirectory()) to_underscorefile = new File(to_underscorefile, from_underscorefile.getName());
        if (to_underscorefile.exists()) {
            if (!to_underscorefile.canWrite()) abort("�������� ���� ���������� ��� ������" + to_underscorefile);
            System.out.println("������������ ������� ����?" + to_underscorefile.getName() + "?(Y/N):");
            System.out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String response = in.readLine();
            if (!response.equals("Y") && !response.equals("y")) abort("������������ ���� �� ��� �����������");
        } else {
            String parent = to_underscorefile.getParent();
            if (parent == null) parent = System.getProperty("user.dir");
            File dir = new File(parent);
            if (!dir.exists()) abort("������� ���������� �� ���������" + parent);
            if (!dir.isFile()) abort("�� �������� ���������" + parent);
            if (!dir.canWrite()) abort("������ �� ������" + parent);
        }
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(from_underscorefile);
            to = new FileOutputStream(to_underscorefile);
            byte[] buffer = new byte[4096];
            int bytes_underscoreread;
            while ((bytes_underscoreread = from.read(buffer)) != -1) to.write(buffer, 0, bytes_underscoreread);
        } finally {
            if (from != null) try {
                from.close();
            } catch (IOException e) {
                ;
            }
            if (to != null) try {
                to.close();
            } catch (IOException e) {
                ;
            }
        }
    }

