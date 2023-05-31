    public String getClass(EmeraldjbBean eb) throws EmeraldjbException {
        Entity entity = (Entity) eb;
        StringBuffer sb = new StringBuffer();
        String myPackage = getPackageName(eb);
        sb.append("package " + myPackage + ";\n");
        sb.append("\n");
        DaoValuesGenerator valgen = new DaoValuesGenerator();
        String values_underscoreclass_underscorename = valgen.getClassName(entity);
        sb.append("\n");
        List importList = new Vector();
        importList.add("java.io.FileOutputStream;");
        importList.add("java.io.FileInputStream;");
        importList.add("java.io.DataInputStream;");
        importList.add("java.io.DataOutputStream;");
        importList.add("java.io.IOException;");
        importList.add("java.sql.Date;");
        importList.add(valgen.getPackageName(eb) + "." + values_underscoreclass_underscorename + ";");
        Iterator it = importList.iterator();
        while (it.hasNext()) {
            String importName = (String) it.next();
            sb.append("import " + importName + "\n");
        }
        sb.append("\n");
        String proto_underscoreversion = entity.getPatternValue(GeneratorConst.PATTERN_underscoreSTREAM_underscorePROTO_underscoreVERSION, "1");
        String streamer_underscoreclass_underscorename = getClassName(entity);
        sb.append("public class " + streamer_underscoreclass_underscorename + "\n");
        sb.append("{" + "\n  public static final int PROTO_underscoreVERSION=" + proto_underscoreversion + ";");
        sb.append("\n\n");
        StringBuffer f_underscorewriter = new StringBuffer();
        StringBuffer f_underscorereader = new StringBuffer();
        boolean has_underscoretimes = false;
        boolean has_underscorestrings = false;
        it = entity.getMembers().iterator();
        while (it.hasNext()) {
            Member member = (Member) it.next();
            String nm = member.getName();
            String getter = "obj." + methodGenerator.getMethodName(DaoGeneratorUtils.METHOD_underscoreGET, member);
            String setter = "obj." + methodGenerator.getMethodName(DaoGeneratorUtils.METHOD_underscoreSET, member);
            String pad = "    ";
            JTypeBase gen_underscoretype = EmdFactory.getJTypeFactory().getJavaType(member.getType());
            f_underscorewriter.append(gen_underscoretype.getToBinaryCode(pad, "dos", getter + "()"));
            f_underscorereader.append(gen_underscoretype.getFromBinaryCode(pad, "din", setter));
        }
        String reader_underscorevars = "";
        sb.append("\n  public static void writeToFile(String file_underscorenm, " + values_underscoreclass_underscorename + " obj) throws IOException" + "\n  {" + "\n    if (file_underscorenm==null || file_underscorenm.length()==0) throw new IOException(\"Bad file name (null or zero length)\");" + "\n    if (obj==null) throw new IOException(\"Bad value object parameter, cannot write null object to file\");" + "\n    FileOutputStream fos = new FileOutputStream(file_underscorenm);" + "\n    DataOutputStream dos = new DataOutputStream(fos);" + "\n    writeStream(dos, obj);" + "\n    fos.close();" + "\n  } // end of writeToFile" + "\n" + "\n  public static void readFromFile(String file_underscorenm, " + values_underscoreclass_underscorename + " obj) throws IOException" + "\n  {" + "\n    if (file_underscorenm==null || file_underscorenm.length()==0) throw new IOException(\"Bad file name (null or zero length)\");" + "\n    if (obj==null) throw new IOException(\"Bad value object parameter, cannot write null object to file\");" + "\n    FileInputStream fis = new FileInputStream(file_underscorenm);" + "\n    DataInputStream dis = new DataInputStream(fis);" + "\n    readStream(dis, obj);" + "\n    fis.close();" + "\n  } // end of readFromFile" + "\n" + "\n  public static void writeStream(DataOutputStream dos, " + values_underscoreclass_underscorename + " obj) throws IOException" + "\n  {" + "\n    dos.writeByte(PROTO_underscoreVERSION);" + "\n    " + f_underscorewriter + "\n  } // end of writeStream" + "\n" + "\n  public static void readStream(DataInputStream din, " + values_underscoreclass_underscorename + " obj) throws IOException" + "\n  {" + "\n    int proto_underscoreversion = din.readByte();" + "\n    if (proto_underscoreversion==" + proto_underscoreversion + ") readStreamV1(din,obj);" + "\n  } // end of readStream" + "\n" + "\n  public static void readStreamV1(DataInputStream din, " + values_underscoreclass_underscorename + " obj) throws IOException" + "\n  {" + reader_underscorevars + f_underscorereader + "\n  } // end of readStreamV1" + "\n" + "\n} // end of classs" + "\n\n" + "\n//**************" + "\n// End of file" + "\n//**************");
        return sb.toString();
    }

