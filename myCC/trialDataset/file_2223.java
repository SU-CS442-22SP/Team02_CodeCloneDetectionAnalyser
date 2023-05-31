    private static void generateTIFF(Connection con, String category, String area_underscorecode, String topic_underscorecode, String timeseries, String diff_underscoretimeseries, Calendar time, String area_underscorelabel, String raster_underscorelabel, String image_underscorelabel, String note, Rectangle2D bounds, Rectangle2D raster_underscorebounds, String source_underscorefilename, String diff_underscorefilename, String legend_underscorefilename, String output_underscorefilename, int output_underscoremaximum_underscoresize) throws SQLException, IOException {
        Debug.println("ImageCropper.generateTIFF begin");
        MapContext map_underscorecontext = new MapContext("test", new Configuration());
        try {
            Map map = new Map(map_underscorecontext, area_underscorelabel, new Configuration());
            map.setCoordSys(ProjectionCategories.default_underscorecoordinate_underscoresystem);
            map.setPatternOutline(new XPatternOutline(new XPatternPaint(Color.white)));
            String type = null;
            RasterLayer rlayer = getRasterLayer(map, raster_underscorelabel, getLinuxPathEquivalent(source_underscorefilename), getLinuxPathEquivalent(diff_underscorefilename), type, getLinuxPathEquivalent(legend_underscorefilename));
            map.addLayer(rlayer, true);
            map.setBounds2DImage(bounds, true);
            Dimension image_underscoredim = null;
            image_underscoredim = new Dimension((int) rlayer.raster.getDeviceBounds().getWidth() + 1, (int) rlayer.raster.getDeviceBounds().getHeight() + 1);
            if (output_underscoremaximum_underscoresize > 0) {
                double width_underscorefactor = image_underscoredim.getWidth() / output_underscoremaximum_underscoresize;
                double height_underscorefactor = image_underscoredim.getHeight() / output_underscoremaximum_underscoresize;
                double factor = Math.max(width_underscorefactor, height_underscorefactor);
                if (factor > 1.0) {
                    image_underscoredim.setSize(image_underscoredim.getWidth() / factor, image_underscoredim.getHeight() / factor);
                }
            }
            map.setImageDimension(image_underscoredim);
            map.scale();
            image_underscoredim = new Dimension((int) map.getBounds2DImage().getWidth(), (int) map.getBounds2DImage().getHeight());
            Image image = null;
            Graphics gr = null;
            image = ImageCreator.getImage(image_underscoredim);
            gr = image.getGraphics();
            try {
                map.paint(gr);
            } catch (Exception e) {
                Debug.println("map.paint error: " + e.getMessage());
            }
            String tiff_underscorefilename = "";
            try {
                tiff_underscorefilename = formatPath(category, timeseries, output_underscorefilename);
                new File(new_underscorefilename).mkdirs();
                Debug.println("tiff_underscorefilename: " + tiff_underscorefilename);
                BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_underscoreBYTE_underscoreINDEXED);
                bi.createGraphics().drawImage(image, 0, 0, null);
                File f = new File(tiff_underscorefilename);
                FileOutputStream out = new FileOutputStream(f);
                TIFFEncodeParam param = new TIFFEncodeParam();
                param.setCompression(TIFFEncodeParam.COMPRESSION_underscorePACKBITS);
                TIFFImageEncoder encoder = (TIFFImageEncoder) TIFFCodec.createImageEncoder("tiff", out, param);
                encoder.encode(bi);
                out.close();
            } catch (IOException e) {
                Debug.println("ImageCropper.generateTIFF TIFFCodec e: " + e.getMessage());
                throw new IOException("GenerateTIFF.IOException: " + e);
            }
            PreparedStatement pstmt = null;
            try {
                String query = "select Proj_underscoreID, AccessType_underscoreCode from project " + "where Proj_underscoreCode= '" + area_underscorecode.trim() + "'";
                Statement stmt = null;
                ResultSet rs = null;
                int proj_underscoreid = -1;
                int access_underscorecode = -1;
                stmt = con.createStatement();
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    proj_underscoreid = rs.getInt(1);
                    access_underscorecode = rs.getInt(2);
                }
                rs.close();
                stmt.close();
                String delete_underscoreraster = "delete from rasterlayer where " + "Raster_underscoreName='" + tiff_underscorename.trim() + "' and Group_underscoreCode='" + category.trim() + "' and Proj_underscoreID =" + proj_underscoreid;
                Debug.println("***** delete_underscoreraster: " + delete_underscoreraster);
                pstmt = con.prepareStatement(delete_underscoreraster);
                boolean del = pstmt.execute();
                pstmt.close();
                String insert_underscoreraster = "insert into rasterlayer(Raster_underscoreName, " + "Group_underscoreCode, Proj_underscoreID, Raster_underscoreTimeCode, Raster_underscoreXmin, " + "Raster_underscoreYmin, Raster_underscoreArea_underscoreXmin, Raster_underscoreArea_underscoreYmin, " + "Raster_underscoreVisibility, Raster_underscoreOrder, Raster_underscorePath, " + "AccessType_underscoreCode, Raster_underscoreTimePeriod) values(?,?,?,?, " + "?,?,?,?,?,?,?,?,?)";
                pstmt = con.prepareStatement(insert_underscoreraster);
                pstmt.setString(1, tiff_underscorename);
                pstmt.setString(2, category);
                pstmt.setInt(3, proj_underscoreid);
                pstmt.setString(4, timeseries);
                pstmt.setDouble(5, raster_underscorebounds.getX());
                pstmt.setDouble(6, raster_underscorebounds.getY());
                pstmt.setDouble(7, raster_underscorebounds.getWidth());
                pstmt.setDouble(8, raster_underscorebounds.getHeight());
                pstmt.setString(9, "false");
                int sequence = 0;
                if (tiff_underscorename.endsWith("DP")) {
                    sequence = 1;
                } else if (tiff_underscorename.endsWith("DY")) {
                    sequence = 2;
                } else if (tiff_underscorename.endsWith("DA")) {
                    sequence = 3;
                }
                pstmt.setInt(10, sequence);
                pstmt.setString(11, tiff_underscorefilename);
                pstmt.setInt(12, access_underscorecode);
                if (time == null) {
                    pstmt.setNull(13, java.sql.Types.DATE);
                } else {
                    pstmt.setDate(13, new java.sql.Date(time.getTimeInMillis()));
                }
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Debug.println("SQLException occurred e: " + e.getMessage());
                con.rollback();
                throw new SQLException("GenerateTIFF.SQLException: " + e);
            } finally {
                pstmt.close();
            }
        } catch (Exception e) {
            Debug.println("ImageCropper.generateTIFF e: " + e.getMessage());
        }
        Debug.println("ImageCropper.generateTIFF end");
    }

