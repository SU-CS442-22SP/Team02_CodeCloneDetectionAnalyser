    private static void generateGIF(Connection con, String category, String area_underscorecode, String topic_underscorecode, String timeseries, String diff_underscoretimeseries, Calendar time, String area_underscorelabel, String raster_underscorelabel, String image_underscorelabel, String note, Rectangle2D bounds, Rectangle2D raster_underscorebounds, String source_underscorefilename, String diff_underscorefilename, String legend_underscorefilename, String output_underscorefilename, int output_underscoremaximum_underscoresize) throws SQLException, IOException {
        System.out.println("ImageCropper.generateGIF begin");
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
            String gif_underscorefilename = "";
            try {
                gif_underscorefilename = formatPath(category, timeseries, output_underscorefilename);
                new File(new_underscorefilename).mkdirs();
                new GifEncoder(image, new FileOutputStream(gif_underscorefilename)).encode();
            } catch (IOException e) {
                Debug.println("ImageCropper.generateGIF e: " + e.getMessage());
                throw new IOException("GenerateGIF.IOException: " + e);
            }
            PreparedStatement pstmt = null;
            try {
                String delete_underscoreraster = "delete raster_underscorelayer where " + "label='" + gif_underscorename.trim() + "' and category='" + category.trim() + "' and area_underscorecode=' " + area_underscorecode.trim() + "'";
                pstmt = con.prepareStatement(delete_underscoreraster);
                boolean del = pstmt.execute();
                pstmt.close();
                String insert_underscoreraster = "insert into RASTER_underscoreLAYER " + "values(RASTER_underscoreLAYER_underscoreID.nextval, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "SYSDATE, ?)";
                pstmt = con.prepareStatement(insert_underscoreraster);
                pstmt.setString(1, gif_underscorename);
                pstmt.setString(2, category);
                pstmt.setString(3, area_underscorecode);
                pstmt.setString(4, topic_underscorecode);
                if (time == null) {
                    pstmt.setNull(5, java.sql.Types.DATE);
                } else {
                    pstmt.setDate(5, new java.sql.Date(time.getTimeInMillis()));
                }
                pstmt.setString(6, timeseries);
                pstmt.setString(7, gif_underscorefilename);
                pstmt.setNull(8, java.sql.Types.INTEGER);
                pstmt.setNull(9, java.sql.Types.INTEGER);
                pstmt.setDouble(10, raster_underscorebounds.getX());
                pstmt.setDouble(11, raster_underscorebounds.getY());
                pstmt.setDouble(12, raster_underscorebounds.getWidth());
                pstmt.setDouble(13, raster_underscorebounds.getHeight());
                pstmt.setString(14, note);
                int sequence = 0;
                if (gif_underscorename.endsWith("DP")) {
                    sequence = 1;
                } else if (gif_underscorename.endsWith("DY")) {
                    sequence = 2;
                } else if (gif_underscorename.endsWith("DA")) {
                    sequence = 3;
                }
                pstmt.setInt(15, sequence);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Debug.println("SQLException occurred e: " + e.getMessage());
                con.rollback();
                throw new SQLException("GenerateGIF.SQLException: " + e);
            } finally {
                pstmt.close();
            }
        } catch (Exception e) {
            Debug.println("ImageCropper.generateGIF e: " + e.getMessage());
        }
        System.out.println("ImageCropper.generateGIF end");
    }

