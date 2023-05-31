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
        importList.add("java.io.*;");
        importList.add("java.sql.Date;");
        importList.add("com.emeraldjb.runtime.patternXmlObj.*;");
        importList.add("javax.xml.parsers.*;");
        importList.add("java.text.ParseException;");
        importList.add("org.xml.sax.*;");
        importList.add("org.xml.sax.helpers.*;");
        importList.add(valgen.getPackageName(eb) + "." + values_underscoreclass_underscorename + ";");
        Iterator it = importList.iterator();
        while (it.hasNext()) {
            String importName = (String) it.next();
            sb.append("import " + importName + "\n");
        }
        sb.append("\n");
        String proto_underscoreversion = entity.getPatternValue(GeneratorConst.PATTERN_underscoreSTREAM_underscorePROTO_underscoreVERSION, "1");
        boolean short_underscoreversion = entity.getPatternBooleanValue(GeneratorConst.PATTERN_underscoreSTREAM_underscoreXML_underscoreSHORT, false);
        StringBuffer preface = new StringBuffer();
        StringBuffer consts = new StringBuffer();
        StringBuffer f_underscorewriter = new StringBuffer();
        StringBuffer f_underscorewriter_underscoreshort = new StringBuffer();
        StringBuffer f_underscorereader = new StringBuffer();
        StringBuffer end_underscoreelems = new StringBuffer();
        boolean end_underscoreelem_underscoreneeds_underscorecatch = false;
        consts.append("\n  public static final String EL_underscoreCLASS_underscoreTAG=\"" + values_underscoreclass_underscorename + "\";");
        preface.append("\n    xos.print(\"<!-- This format is optimised for space, below are the column mappings\");");
        boolean has_underscoretimes = false;
        boolean has_underscorestrings = false;
        it = entity.getMembers().iterator();
        int col_underscorenum = 0;
        while (it.hasNext()) {
            col_underscorenum++;
            Member member = (Member) it.next();
            String nm = member.getName();
            preface.append("\n    xos.print(\"c" + col_underscorenum + " = " + nm + "\");");
            String elem_underscorename = nm;
            String elem_underscorename_underscoreshort = "c" + col_underscorenum;
            String el_underscorename = nm.toUpperCase();
            if (member.getColLen() > 0 || !member.isNullAllowed()) {
                end_underscoreelem_underscoreneeds_underscorecatch = true;
            }
            String element_underscoreconst = "EL_underscore" + el_underscorename;
            String element_underscoreconst_underscoreshort = "EL_underscore" + el_underscorename + "_underscoreSHORT";
            consts.append("\n  public static final String " + element_underscoreconst + "=\"" + elem_underscorename + "\";" + "\n  public static final String " + element_underscoreconst_underscoreshort + "=\"" + elem_underscorename_underscoreshort + "\";");
            String getter = "obj." + methodGenerator.getMethodName(DaoGeneratorUtils.METHOD_underscoreGET, member);
            String setter = "values_underscore." + methodGenerator.getMethodName(DaoGeneratorUtils.METHOD_underscoreSET, member);
            String pad = "    ";
            JTypeBase gen_underscoretype = EmdFactory.getJTypeFactory().getJavaType(member.getType());
            f_underscorewriter.append(gen_underscoretype.getToXmlCode(pad, element_underscoreconst, getter + "()"));
            f_underscorewriter_underscoreshort.append(gen_underscoretype.getToXmlCode(pad, element_underscoreconst_underscoreshort, getter + "()"));
            end_underscoreelems.append(gen_underscoretype.getFromXmlCode(pad, element_underscoreconst, setter));
            end_underscoreelems.append("\n    //and also the short version");
            end_underscoreelems.append(gen_underscoretype.getFromXmlCode(pad, element_underscoreconst_underscoreshort, setter));
        }
        preface.append("\n    xos.print(\"-->\");");
        String body_underscorepart = f_underscorewriter.toString();
        String body_underscorepart_underscoreshort = preface.toString() + f_underscorewriter_underscoreshort.toString();
        String reader_underscorevars = "";
        String streamer_underscoreclass_underscorename = getClassName(entity);
        sb.append("public class " + streamer_underscoreclass_underscorename + "  extends DefaultHandler implements TSParser\n");
        sb.append("{" + consts + "\n  public static final int PROTO_underscoreVERSION=" + proto_underscoreversion + ";" + "\n  private transient StringBuffer cdata_underscore=new StringBuffer();" + "\n  private transient String endElement_underscore;" + "\n  private transient TSParser parentParser_underscore;" + "\n  private transient XMLReader theReader_underscore;\n" + "\n  private " + values_underscoreclass_underscorename + " values_underscore;");
        sb.append("\n\n");
        sb.append("\n  /**" + "\n   * This is really only here as an example.  It is very rare to write a single" + "\n   * object to a file - far more likely to have a collection or object graph.  " + "\n   * in which case you can write something similar - maybe using the writeXmlShort" + "\n   * version instread." + "\n   */" + "\n  public static void writeToFile(String file_underscorenm, " + values_underscoreclass_underscorename + " obj) throws IOException" + "\n  {" + "\n    if (file_underscorenm==null || file_underscorenm.length()==0) throw new IOException(\"Bad file name (null or zero length)\");" + "\n    if (obj==null) throw new IOException(\"Bad value object parameter, cannot write null object to file\");" + "\n    FileOutputStream fos = new FileOutputStream(file_underscorenm);" + "\n    XmlOutputFilter xos = new XmlOutputFilter(fos);" + "\n    xos.openElement(\"FILE_underscore\"+EL_underscoreCLASS_underscoreTAG);" + "\n    writeXml(xos, obj);" + "\n    xos.closeElement();" + "\n    fos.close();" + "\n  } // end of writeToFile" + "\n" + "\n  public static void readFromFile(String file_underscorenm, " + values_underscoreclass_underscorename + " obj) throws IOException, SAXException" + "\n  {" + "\n    if (file_underscorenm==null || file_underscorenm.length()==0) throw new IOException(\"Bad file name (null or zero length)\");" + "\n    if (obj==null) throw new IOException(\"Bad value object parameter, cannot write null object to file\");" + "\n    FileInputStream fis = new FileInputStream(file_underscorenm);" + "\n    DataInputStream dis = new DataInputStream(fis);" + "\n    marshalFromXml(dis, obj);" + "\n    fis.close();" + "\n  } // end of readFromFile" + "\n" + "\n  public static void writeXml(XmlOutputFilter xos, " + values_underscoreclass_underscorename + " obj) throws IOException" + "\n  {" + "\n    xos.openElement(EL_underscoreCLASS_underscoreTAG);" + body_underscorepart + "\n    xos.closeElement();" + "\n  } // end of writeXml" + "\n" + "\n  public static void writeXmlShort(XmlOutputFilter xos, " + values_underscoreclass_underscorename + " obj) throws IOException" + "\n  {" + "\n    xos.openElement(EL_underscoreCLASS_underscoreTAG);" + body_underscorepart_underscoreshort + "\n    xos.closeElement();" + "\n  } // end of writeXml" + "\n" + "\n  public " + streamer_underscoreclass_underscorename + "(" + values_underscoreclass_underscorename + " obj) {" + "\n    values_underscore = obj;" + "\n  } // end of ctor" + "\n");
        String xml_underscorebit = addXmlFunctions(streamer_underscoreclass_underscorename, values_underscoreclass_underscorename, end_underscoreelem_underscoreneeds_underscorecatch, end_underscoreelems, f_underscorereader);
        String close = "\n" + "\n} // end of classs" + "\n\n" + "\n//**************" + "\n// End of file" + "\n//**************";
        return sb.toString() + xml_underscorebit + close;
    }

