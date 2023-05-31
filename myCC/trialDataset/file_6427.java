    public static void toValueSAX(Property property, Value value, int valueType, ContentHandler contentHandler, AttributesImpl na, Context context) throws SAXException, RepositoryException {
        na.clear();
        String _underscorevalue = null;
        switch(valueType) {
            case PropertyType.DATE:
                DateFormat df = new SimpleDateFormat(BackupFormatConstants.DATE_underscoreFORMAT_underscoreSTRING);
                df.setTimeZone(value.getDate().getTimeZone());
                _underscorevalue = df.format(value.getDate().getTime());
                break;
            case PropertyType.BINARY:
                String outResourceName = property.getParent().getPath() + "/" + property.getName();
                OutputStream os = null;
                InputStream is = null;
                try {
                    os = context.getPersistenceManager().getOutResource(outResourceName, true);
                    is = value.getStream();
                    IOUtils.copy(is, os);
                    os.flush();
                } catch (Exception e) {
                    throw new SAXException("Could not backup binary value of property [" + property.getName() + "]", e);
                } finally {
                    if (null != is) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != os) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                na.addAttribute("", ATTACHMENT, (NAMESPACE.length() > 0 ? NAMESPACE + ":" : "") + ATTACHMENT, "string", outResourceName);
                break;
            case PropertyType.REFERENCE:
                _underscorevalue = value.getString();
                break;
            default:
                _underscorevalue = value.getString();
        }
        contentHandler.startElement("", VALUE, (NAMESPACE.length() > 0 ? NAMESPACE + ":" : "") + VALUE, na);
        if (null != _underscorevalue) contentHandler.characters(_underscorevalue.toCharArray(), 0, _underscorevalue.length());
        contentHandler.endElement("", VALUE, (NAMESPACE.length() > 0 ? NAMESPACE + ":" : "") + VALUE);
    }

